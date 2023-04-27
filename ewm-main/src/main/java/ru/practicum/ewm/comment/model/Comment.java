package ru.practicum.ewm.comment.model;

import lombok.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "comment_entity_graph", attributeNodes = {
        @NamedAttributeNode("user"),
        @NamedAttributeNode(value = "event", subgraph = "comment_entity_subgraph")
}, subgraphs = {
        @NamedSubgraph(name = "comment_entity_subgraph", attributeNodes = {
                @NamedAttributeNode(value = "category"),
                @NamedAttributeNode(value = "initiator")
        }),
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    private String message;
    @Column(name = "created_date")
    private LocalDateTime createdOn;
}
