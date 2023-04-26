package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "comment_entity_graph")
    List<Comment> findByUser(User user, PageRequest page);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "comment_entity_graph")
    List<Comment> findByEvent(Event event, PageRequest page);
}
