package com.example.todoapp.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.model.Todo;

/**
 * {@link Todo} とDTOを変換するマッパー
 */
@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface TodoMapper {

    /**
     * テスト用インスタンス
     */
    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    /**
     * {@link TodoRequestDto} を {@link Todo} に変換する。
     *
     * @param requestDto {@link TodoRequestDto}
     * @return {@link Todo}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "completed", source = "completed")
    Todo toTodo(TodoRequestDto requestDto);

    /**
     * {@link Todo} を {@link TodoResponseDto} に変換する。
     *
     * @param todo {@link Todo}
     * @return {@link TodoResponseDto}
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "completed", source = "completed")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TodoResponseDto toResponseDto(Todo todo);
}
