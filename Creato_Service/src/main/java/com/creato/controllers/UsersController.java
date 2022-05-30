package com.creato.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.creato.Models.ChangeUserStatusModel;
import com.creato.Models.JoinCodeUpdationModel;
import com.creato.Models.LoginModel;
import com.creato.Models.UpdatePasswordModel;
import com.creato.Models.UserCreationModel;
import com.creato.Models.UserModel;
import com.creato.Services.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	UsersService userService;
	
	@GetMapping
	public ResponseEntity<?> getUsers() {
		return new ResponseEntity<List<UserModel>>(userService.getAllUsers(), HttpStatus.OK); 
	}
	
	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody UserCreationModel model){
		return userService.createUser(model);
	}
	
	@PostMapping("/usernameValidation")
	public ResponseEntity<?> validateUsername(@RequestBody Map<String, String> model){
		return new ResponseEntity<Map<String, Boolean>>(userService.validateUsername(model), HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginModel model){
		return userService.loginUser(model);
	}
	
	@PostMapping("/acceptOrReject")
	public ResponseEntity<?> changeUserStatus(@RequestBody ChangeUserStatusModel model){
		return userService.changeUserStatus(model);
	}
	
	@PostMapping("/getJoinCode")
	public ResponseEntity<?> getJoinCode(@RequestBody UserCreationModel model){
		return userService.getJoinCode(model);
	}
	
	@PostMapping("/updateJoinCode")
	public ResponseEntity<?> updateJoinCode(@RequestBody JoinCodeUpdationModel model){
		return userService.updateJoinCode(model);
	}
	
	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordModel model){
		return userService.updatePassword(model);
	}
}
