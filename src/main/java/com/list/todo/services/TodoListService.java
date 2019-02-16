package com.list.todo.services;

import com.list.todo.entity.Share;
import com.list.todo.entity.TodoList;
import com.list.todo.entity.User;
import com.list.todo.payload.ApiResponse;
import com.list.todo.payload.TodoListInput;
import com.list.todo.repositories.TodoListRepository;
import com.list.todo.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;

    private FollowerService followerService;
    private UserService userService;
    private ShareService shareService;

    public Optional<TodoList> getTodoListById(Long todoListId) {
        return todoListRepository.findById(todoListId);
    }

    public Iterable<TodoList> getTodoListsByUser(Long userId) {
        return todoListRepository.findTodoListsByUserOwnerId(userId);
    }

    public Optional<TodoList> addTodoList(TodoListInput todoListInput, Long userId) {

        TodoList todoList = new TodoList();
        todoList.setUserOwnerId(userId);
        todoList.setTodoListName(todoListInput.getTodoListName());

        userService.getUserById(userId)
                .ifPresent(user -> followerService.notifyFollowersAboutAddTodoList(user, todoList));

        return Optional.of(todoListRepository.save(todoList));
    }

    public Optional<TodoList> updateTodoList(Long todoListId, TodoListInput todoListInput, Long userId) {

        Optional<TodoList> todoList = todoListRepository.findById(todoListId)
                .map(tl -> {
                    tl.setTodoListName(todoListInput.getTodoListName());
                    return todoListRepository.save(tl);
                });

        todoList.ifPresent(todoList1 -> userService.getUserById(userId)
                .ifPresent(user -> followerService.notifyFollowersAboutUpdatingTodoList(user, todoList1)));

        return todoList;
    }

    public void deleteTodoList(Long todoListId, Long userId) {

        Optional<TodoList> todoList = todoListRepository.findById(todoListId);

        if (todoList.isPresent()) {
            todoListRepository.deleteById(todoListId);
            userService.getUserById(userId)
                    .ifPresent(user -> followerService.notifyFollowersAboutDeletingTodoList(user, todoList.get()));
        }
    }

    public ApiResponse shareTodoList(String sharedUsername, Long sharedTodoListId, Long userId) {

        Optional<User> sharedUser = userService.getUserByUsername(sharedUsername);
        Optional<TodoList> sharedTodoList = todoListRepository.findById(sharedTodoListId);

        if (sharedUser.isPresent() && sharedTodoList.isPresent()) {
            Share share = new Share(sharedUser.get().getId(), sharedTodoList.get());
            shareService.addShare(share);

            userService.getUserById(userId)
                    .ifPresent(user -> {
                        followerService.notifyFollowersAboutSharingTodoList(user, sharedUser.get(), sharedTodoList.get());
                        shareService.sendNotificationAboutShareTodoList(sharedUser.get(), user, sharedTodoList.get());
                    });
        }
        return new ApiResponse(true, "You shared your todoList to " + sharedUsername + "!");
    }
}
