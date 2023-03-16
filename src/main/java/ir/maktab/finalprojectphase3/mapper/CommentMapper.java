package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.CommentDTO;
import ir.maktab.finalprojectphase3.data.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment dtoToModel(CommentDTO commentDTO);

    @Mapping(target = "expertUserName", expression = "java(comment.getExpert().getUsername())")
    @Mapping(target = "customerUserName", expression = "java(comment.getCustomer().getUsername())")
    CommentDTO modelToDto(Comment comment);
}
