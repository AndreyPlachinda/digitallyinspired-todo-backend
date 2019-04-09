package com.list.todo.services;

import com.list.todo.entity.Tag;
import com.list.todo.entity.TagTaskKey;
import com.list.todo.entity.Task;
import com.list.todo.entity.TodoList;
import com.list.todo.repositories.TagRepository;
import com.list.todo.repositories.TagTaskKeyRepository;
import com.list.todo.repositories.TaskRepository;
import com.list.todo.repositories.TodoListRepository;
import com.list.todo.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagTaskKeyService {

    private final TagTaskKeyRepository tagTaskKeyRepository;
    private final TaskRepository taskRepository;
    private final TodoListRepository todoListRepository;
    private final TagRepository tagRepository;

    Optional<TagTaskKey> addTaggedTask(TagTaskKey tagTaskKey) {
        return Optional.of(tagTaskKeyRepository.save(tagTaskKey));
    }

    public Set<TagTaskKey> getMyTaggedTask(UserPrincipal currentUser, Pageable pageable) {
        Set<TagTaskKey> myTagTaskKey = new HashSet<>();

        Iterable<TodoList> todoListsByCreatedBy = todoListRepository.findByCreatedBy(currentUser.getUsername(), pageable);

        todoListsByCreatedBy
                .forEach(todoList -> todoList.getTasks()
                        .forEach(task -> myTagTaskKey.addAll(tagTaskKeyRepository.findByTaskId(task.getId()))));

        return myTagTaskKey;
    }

    List<TagTaskKey> getTaggedTasksByTag(Tag tag) {
        return tagTaskKeyRepository.findByTag(tag);
    }

    Set<Task> getTasksByTags(List<Long> tagsIds, Long currentUserId) {
        Set<Task> tasksByTags = new HashSet<>();

        tagsIds.forEach(tagId -> tagRepository.findById(tagId).
                ifPresent(tag -> {
                    if (tag.getOwnerId().equals(currentUserId)) {
                        tasksByTags.addAll(tagTaskKeyRepository.findByTag(tag)
                                .stream()
                                .map(taggedTask -> taskRepository.getOne(taggedTask.getTaskId()))
                                .collect(Collectors.toSet()));
                    }
                }));

        return tasksByTags;
    }

    void deleteTaggedTask(Long taskId, Tag tag) {
        tagTaskKeyRepository.findByTaskIdAndTag(taskId, tag)
                .ifPresent(tagTaskKeyRepository::delete);
    }

    void deleteTaggedTask(TagTaskKey tagTaskKey) {
        tagTaskKeyRepository.delete(tagTaskKey);
    }

}