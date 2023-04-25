package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.event " +
            "WHERE c.user = :user ")
    List<Comment> findByUser(User user, PageRequest page);

    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.event " +
            "WHERE c.event = :event ")
    List<Comment> findByEvent(Event event, PageRequest page);
}
