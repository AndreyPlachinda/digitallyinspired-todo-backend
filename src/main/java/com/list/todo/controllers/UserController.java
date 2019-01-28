package com.list.todo.controllers;

import java.util.List;

import com.list.todo.entity.Follower;
import com.list.todo.services.FollowerService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.list.todo.entity.TodoList;
import com.list.todo.entity.User;
import com.list.todo.payload.UserStats;
import com.list.todo.payload.UserSummary;
import com.list.todo.security.UserPrincipal;
import com.list.todo.services.ShareService;
import com.list.todo.services.TodoListService;
import com.list.todo.services.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {
	
	private final UserService userService;
	private final TodoListService todoListService;
	private final ShareService shareService;
	private final FollowerService followerService;

	@GetMapping("/me")
    public UserSummary getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
		User user = userService.getUserById(currentUser.getId());

		return new UserSummary(user.getId(), user.getUsername(), user.getName());
    }
	
	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUserByUsername(@RequestParam("username") String username) {

		List<User> users = userService.getUsersByPartOfUsername(username);
		
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@GetMapping("/userStats")
	public ResponseEntity<UserStats> getUserStats(@AuthenticationPrincipal UserPrincipal currentUser) {

		UserStats userStats = new UserStats();
		
		List<TodoList> myTodoLists = todoListService.getTodoListsByUser(currentUser.getId());
		userStats.setMyTodoLists(myTodoLists);
		
		List<TodoList> sharedTodoLists = shareService.getSharedTodoListsByUser(currentUser.getId());
		userStats.setSharedTodoLists(sharedTodoLists);

		return new ResponseEntity<UserStats>(userStats, HttpStatus.OK);
	}
	
	@PutMapping("/editProfile")
	public ResponseEntity<User> updateUser(@AuthenticationPrincipal UserPrincipal currentUser,
										   @RequestBody User user) {
		User currUser = userService.getUserById(currentUser.getId());
		ResponseEntity<User> responseEntity;

		if (currUser == null){
			responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			BeanUtils.copyProperties(user, currUser, "id", "roles");
			userService.updateUser(currUser);
			responseEntity = new ResponseEntity<>(currUser, HttpStatus.OK);
		}
		return responseEntity;
	}
	
	@DeleteMapping("/deleteProfile")
	public ResponseEntity<User> deleteUser(@AuthenticationPrincipal UserPrincipal currentUser) {

		userService.deleteUser(currentUser.getId());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/followUser")
	public ResponseEntity<Void> followUser(@AuthenticationPrincipal UserPrincipal currentUser,
										   @RequestParam("username") String username) {

		followerService.followUser(new Follower(currentUser.getId(), userService.getUserByUsername(username)));

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/followers")
	public ResponseEntity<List<User>> getFollowers(@AuthenticationPrincipal UserPrincipal currentUser) {

		List<User> followers = followerService.getFollowersByUserId(currentUser.getId());

		return new ResponseEntity<>(followers, HttpStatus.OK);
	}
}
