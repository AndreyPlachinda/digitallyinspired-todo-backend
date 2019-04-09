package com.list.todo.services;

import com.list.todo.entity.Tag;
import com.list.todo.entity.TaggedTask;
import com.list.todo.entity.Task;
import com.list.todo.entity.TodoList;
import com.list.todo.repositories.TagRepository;
import com.list.todo.repositories.TaggedTaskRepository;
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
public class TaggedTaskService {

    private final TaggedTaskRepository taggedTaskRepository;
    private final TaskRepository taskRepository;
    private final TodoListRepository todoListRepository;
    private final TagRepository tagRepository;

    Optional<TaggedTask> addTaggedTask(TaggedTask taggedTask) {
        return Optional.of(taggedTaskRepository.save(taggedTask));
    }

    public Set<TaggedTask> getMyTaggedTask(UserPrincipal currentUser, Pageable pageable) {
        Set<TaggedTask> myTaggedTask = new HashSet<>();

        Iterable<TodoList> todoListsByCreatedBy = todoListRepository.findByCreatedBy(currentUser.getUsername(), pageable);

        todoListsByCreatedBy
                .forEach(todoList -> todoList.getTasks()
                        .forEach(task -> myTaggedTask.addAll(taggedTaskRepository.findByTaskId(task.getId()))));

        return myTaggedTask;
    }

    List<TaggedTask> getTaggedTasksByTag(Tag tag) {
        return taggedTaskRepository.findByTag(tag);
    }

    Set<Task> getTasksByTags(List<Long> tagsIds, Long currentUserId) {
        Set<Task> tasksByTags = new HashSet<>();

        tagsIds.forEach(tagId -> tagRepository.findById(tagId).
                ifPresent(tag -> {
                    if (tag.getOwnerId().equals(currentUserId)) {
                        tasksByTags.addAll(taggedTaskRepository.findByTag(tag)
                                .stream()
                                .map(taggedTask -> taskRepository.getOne(taggedTask.getTaskId()))
                                .collect(Collectors.toSet()));
                    }
                }));

        return tasksByTags;
    }

    void deleteTaggedTask(Long taskId, Tag tag) {
        taggedTaskRepository.findByTaskIdAndTag(taskId, tag)
                .ifPresent(taggedTaskRepository::delete);
    }

    void deleteTaggedTask(TaggedTask taggedTask) {
        taggedTaskRepository.delete(taggedTask);
    }

}
