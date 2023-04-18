package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findByRequester(User user);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Modifying
    @Query(value = "UPDATE ParticipationRequest SET status = 'REJECTED' WHERE event.id = :eventId AND status <> 'CONFIRMED'")
    void rejectNotConfirmedRequests(Long eventId);
}
