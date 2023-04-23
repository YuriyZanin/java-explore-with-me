package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.utils.DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final StatsClient client;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;

    @Value("${app.name}")
    private String appName;

    @Override
    public Collection<EventFullDto> getAll(EventRequestParamsDto params) {
        PageRequest page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());
        List<Event> events = eventRepository.findAll(buildSpecificationByParams(params), page).getContent();
        return EventMapper.MAPPER.toFullDtos(events);
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventRequestDto adminRequest) {
        Event event = getEvent(eventId);

        validateEventDate(adminRequest.getEventDate());

        if (adminRequest.getStateActionDto() != null) {
            if (adminRequest.getStateActionDto().equals(StateActionDto.PUBLISH_EVENT)
                    && event.getState().equals(EventState.PENDING)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (adminRequest.getStateActionDto().equals(StateActionDto.REJECT_EVENT)
                    && !event.getState().equals(EventState.PUBLISHED)) {
                event.setState(EventState.CANCELED);
            } else {
                throw new IllegalArgumentException("Нельзя изменить состояние события: " + event.getState().name());
            }
        }

        updateEventByRequest(event, adminRequest);

        return EventMapper.MAPPER.toFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<EventShortDto> getAllPublic(EventRequestParamsDto params, String uri, String ip) {
        client.saveRequest(appName, uri, ip);

        Specification<Event> byState = (root, query, builder) -> builder.equal(
                root.get("state"), EventState.PUBLISHED);

        Specification<Event> specification = buildSpecificationByParams(params);
        specification = specification == null ? byState : specification.and(byState);

        Sort sort = Sort.by("eventDate");
        PageRequest page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize(), sort);
        List<Event> events = eventRepository.findAll(specification, page).getContent();

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> uris = events.stream().map(e -> uri + "/" + e.getId()).collect(Collectors.toList());
        LocalDateTime start = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.of(0, 0));
        List<ViewStatsDto> stats = client.getStats(start, LocalDateTime.now(), uris, false);
        fillEventViews(events, stats, uri);

        if (params.getSort() != null && params.getSort().equals("VIEWS")) {
            events.sort((a, b) -> Integer.compare(b.getViews(), a.getViews()));
        }

        return EventMapper.MAPPER.toShortDtos(events);
    }

    @Override
    public EventFullDto getPublicById(Long id, String uri, String ip) {
        client.saveRequest(appName, uri, ip);

        Event event = eventRepository.findByIdAndStateIs(id, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Событие не найдено либо не опубликовано"));

        LocalDateTime start = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.of(0, 0));
        List<ViewStatsDto> stats = client.getStats(start, LocalDateTime.now(),
                List.of(uri), false);
        Integer hits = stats.stream().map(s -> s.getHits().intValue()).reduce(0, Integer::sum);
        event.setViews(hits);

        return EventMapper.MAPPER.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto creationDto) {
        User user = getUser(userId);

        Event event = EventMapper.MAPPER.toEvent(creationDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Дата события не должна быть раньше чем через 2 часа");
        }
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        return EventMapper.MAPPER.toFullDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventRequestDto userRequest) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new IllegalArgumentException("Редактировать событие может только его инициатор");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new IllegalArgumentException("Опубликованные события редактировать невозможно");
        }

        validateEventDate(userRequest.getEventDate());

        if (userRequest.getStateActionDto() != null) {
            if (userRequest.getStateActionDto().equals(StateActionDto.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (userRequest.getStateActionDto().equals(StateActionDto.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }

        updateEventByRequest(event, userRequest);

        return EventMapper.MAPPER.toFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<EventShortDto> getByUser(Long userId, Integer from, Integer size) {
        User user = getUser(userId);
        PageRequest page = PageRequest.of(from / size, size);

        return EventMapper.MAPPER.toShortDtos(eventRepository.findByInitiator(user, page));
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = eventRepository.findByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или не принадлежит пользователю"));

        return EventMapper.MAPPER.toFullDto(event);
    }

    @Override
    public Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("Пользователь не является инициатором");
        }

        return ParticipationRequestMapper.MAPPER.toDtos(requestRepository.findAllByEventId(eventId));
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResultDto updateRequestsStatus(Long userId, Long eventId,
                                                                  EventRequestStatusUpdateRequestDto updateRequest) {
        getUser(userId);
        Event event = getEvent(eventId);
        Status newStatus = Status.valueOf(updateRequest.getStatus());
        List<ParticipationRequest> requests = requestRepository.findAllById(updateRequest.getRequestIds());

        List<ParticipationRequest> confirmedRequests = new LinkedList<>();
        List<ParticipationRequest> rejectedRequests = new LinkedList<>();
        if (event.getParticipantLimit() != 0 || event.getRequestModeration()) {
            for (ParticipationRequest request : requests) {
                if (!request.getStatus().equals(Status.PENDING)) {
                    throw new IllegalArgumentException("Заявка не находится в статусе ожидания");
                }
                if (newStatus.equals(Status.CONFIRMED)) {
                    if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                        throw new IllegalArgumentException("Достигнут лимит участников");
                    }
                    confirmedRequests.add(request);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    if (event.getConfirmedRequests() > 0) {
                        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
                    }
                    rejectedRequests.add(request);
                }
                request.setStatus(newStatus);
            }

            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                requestRepository.rejectNotConfirmedRequests(eventId);
            }
            eventRepository.save(event);
            requestRepository.saveAll(requests);
        }

        return new EventRequestStatusUpdateResultDto(ParticipationRequestMapper.MAPPER.toDtos(confirmedRequests),
                ParticipationRequestMapper.MAPPER.toDtos(rejectedRequests));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
    }

    private void updateEventByRequest(Event event, UpdateEventRequestDto request) {
        Optional.ofNullable(request.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(request.getCategoryId())
                .ifPresent(c -> event.setCategory(Category.builder().id(request.getCategoryId()).build()));
        Optional.ofNullable(request.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(request.getEventDate()).ifPresent(s -> {
            event.setEventDate(LocalDateTime.parse(s, DEFAULT_DATE_TIME_FORMATTER));
        });
        Optional.ofNullable(request.getLocation()).ifPresent(l ->
                event.setLocation(Location.builder().lat(l.getLat()).lon(l.getLon()).build()));
        Optional.ofNullable(request.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(request.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(request.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(request.getTitle()).ifPresent(event::setTitle);
    }

    private Specification<Event> buildSpecificationByParams(EventRequestParamsDto params) {
        Specification<Event> specification = null;

        if (params.getUsers() != null) {
            specification = (root, query, builder) -> root.get("initiator").in(Arrays.asList(params.getUsers()));
        }

        if (params.getStates() != null) {
            List<EventState> states = Arrays.stream(params.getStates()).map(EventState::valueOf).collect(Collectors.toList());
            Specification<Event> byState = (root, query, builder) -> root.get("state").in(states);
            specification = specification == null ? byState : specification.and(byState);
        }

        if (params.getCategories() != null) {
            Specification<Event> byCategories = (root, query, builder) -> root.get("category").in(Arrays.asList(params.getCategories()));
            specification = specification == null ? byCategories : specification.and(byCategories);
        }

        if (params.getRangeStart() == null && params.getRangeEnd() == null) {
            Specification<Event> byDate = (root, query, builder) ->
                    builder.greaterThan(root.get("eventDate"), LocalDateTime.now());
            specification = specification == null ? byDate : specification.and(byDate);
        } else {
            if (params.getRangeStart() != null) {
                LocalDateTime start = LocalDateTime.parse(params.getRangeStart(), DEFAULT_DATE_TIME_FORMATTER);
                Specification<Event> byStartDate = (root, query, builder) ->
                        builder.greaterThanOrEqualTo(root.get("eventDate"), start);
                specification = specification == null ? byStartDate : specification.and(byStartDate);
            }

            if (params.getRangeEnd() != null) {
                LocalDateTime end = LocalDateTime.parse(params.getRangeEnd(), DEFAULT_DATE_TIME_FORMATTER);
                Specification<Event> byEndDate = (root, query, builder) ->
                        builder.lessThan(root.get("eventDate"), end);
                specification = specification == null ? byEndDate : specification.and(byEndDate);
            }
        }

        if (params.getText() != null) {
            Specification<Event> byText = (root, query, builder) -> builder.like(
                    builder.lower(root.get("annotation")), "%" + params.getText().toLowerCase() + "%");

            specification = specification == null ? byText : specification.and(byText);
            specification.or(((root, query, builder) -> builder.like(
                    builder.lower(root.get("description")), "%" + params.getText().toLowerCase() + "%")
            ));
        }

        if (params.getPaid() != null) {
            Specification<Event> byPaid = (root, query, builder) -> builder.equal(root.get("paid"), params.getPaid());
            specification = specification == null ? byPaid : specification.and(byPaid);
        }

        if (params.isOnlyAvailable()) {
            Specification<Event> byConfirm = (root, query, builder) ->
                    builder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"));
            specification = specification == null ? byConfirm : specification.and(byConfirm);
        }

        return specification;
    }

    private void validateEventDate(String eventDateStr) {
        if (eventDateStr != null) {
            LocalDateTime eventDate = LocalDateTime.parse(eventDateStr, DEFAULT_DATE_TIME_FORMATTER);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new IllegalArgumentException("Дата начала должна быть не ранее чем за час");
            }
        }
    }

    private void fillEventViews(List<Event> events, List<ViewStatsDto> stats, String baseUri) {
        if (!stats.isEmpty()) {
            Map<String, Integer> statsByUri = stats.stream()
                    .collect(Collectors.groupingBy(ViewStatsDto::getUri, Collectors.summingInt(v -> v.getHits().intValue())));
            events.forEach(e -> {
                Integer views = statsByUri.get(baseUri + "/" + e.getId());
                if (views != null) {
                    e.setViews(views);
                }
            });
        }
    }
}
