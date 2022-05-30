package com.creato.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creato.Models.NotificationResponseModel;
import com.creato.Services.UsersService;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
	
	@Autowired
	UsersService userService;
	
	@GetMapping
	public ResponseEntity<?> getAllNotifications(){
		return userService.getAllNotifications();
	}
	
	@GetMapping("/count")
	public ResponseEntity<?> getNotificationCount(){
		return userService.getNotificationCount();
	}
	
	@PostMapping("readDelete")
	public ResponseEntity<?> markAsRead(@RequestBody NotificationResponseModel model){
		return userService.markNotificationAsRead(model);
	}
}
