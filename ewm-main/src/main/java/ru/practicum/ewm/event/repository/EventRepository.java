package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<Event> findByIdAndStateIs(Long id, EventState state);

    Optional<Event> findByIdAndInitiator(Long eventId, User user);

    List<Event> findByInitiator(User user, PageRequest page);
}
