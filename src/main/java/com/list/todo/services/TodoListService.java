package com.list.todo.services;

import com.list.todo.entity.TodoList;
import com.list.todo.repositories.TodoListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TodoListService {
	
    private final TodoListRepository repository;

    public Set<TodoList> getAllTodoListsByUser(Long userId){
        return repository.findTodoListsByUserOwnerId(userId);
    }
    
    public TodoList getTodoList(Long id){
    	Optional<TodoList> todoList = repository.findById(id);
        return todoList.orElse(null);
    }

    public void addTodoList(TodoList todoList){
        repository.save(todoList);
    }
    
    public void updateTodoList(TodoList todoList) {
    	repository.save(todoList);
    }
    
    public void deleteTodoList(TodoList todoList) {
    	repository.delete(todoList);
    }
}
