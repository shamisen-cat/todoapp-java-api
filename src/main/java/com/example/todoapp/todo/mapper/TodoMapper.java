package com.example.todoapp.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.dto.TodoResponseWithETagDto;
import com.example.todoapp.todo.model.Todo;

/**
 * ToDoエンティティとDTOの変換を行うマッパーインタフェース
 */
@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface TodoMapper {

    /**
     * ユニットテスト用のマッパーインスタンス
     */
    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    /**
     * ToDoリクエストDTOをToDoエンティティに変換する。
     *
     * @param dto ToDoリクエストDTO
     * @return ToDoエンティティ
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "completed", source = "completed")
    Todo toEntity(TodoRequestDto dto);

    /**
     * ToDoエンティティをToDoレスポンスDTOに変換する。
     *
     * @param todo ToDoエンティティ
     * @return ToDoレスポンスDTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "completed", source = "completed")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TodoResponseDto toDto(Todo todo);

    /**
     * ToDoエンティティとETagをToDoレスポンスDTOに変換する。
     *
     * @param todo ToDoエンティティ
     * @param eTag ETagの文字列
     * @return ETagを含むToDoレスポンスDTO
     */
    @Mapping(target = "id", source = "todo.id")
    @Mapping(target = "title", source = "todo.title")
    @Mapping(target = "completed", source = "todo.completed")
    @Mapping(target = "createdAt", source = "todo.createdAt")
    @Mapping(target = "updatedAt", source = "todo.updatedAt")
    @Mapping(target = "eTag", source = "eTag")
    TodoResponseWithETagDto toDto(Todo todo, String eTag);
}
