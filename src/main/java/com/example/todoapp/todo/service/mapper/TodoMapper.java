package com.example.todoapp.todo.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.example.todoapp.todo.dto.TodoRequest;
import com.example.todoapp.todo.dto.TodoResponse;
import com.example.todoapp.todo.model.TodoEntity;

/**
 * {@link TodoEntity} を変換するマッパーインタフェース
 */
@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface TodoMapper {

    /** テスト用インスタンス */
    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    /**
     * {@link TodoRequest} を {@link TodoEntity} に変換する。
     *
     * @param request {@link TodoRequest}
     * @return {@link TodoEntity}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "completed", source = "completed")
    TodoEntity toEntity(TodoRequest request);

    /**
     * {@link TodoEntity} を {@link TodoResponse} に変換する。
     *
     * @param todo {@link TodoEntity}
     * @return {@link TodoResponse}
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "completed", source = "completed")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TodoResponse toResponse(TodoEntity todo);
}
