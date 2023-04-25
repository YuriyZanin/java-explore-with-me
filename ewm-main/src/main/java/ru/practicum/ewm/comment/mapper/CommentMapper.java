package ru.practicum.ewm.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.utils.DateTimeUtils;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "createdOn", target = "createdOn", dateFormat = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtos(List<Comment> comments);
}
