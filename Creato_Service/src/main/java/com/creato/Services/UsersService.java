package com.creato.Services;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.creato.Models.ChangeUserStatusModel;
import com.creato.Models.LoginModel;
import com.creato.Models.PictureCreationModel;
import com.creato.Models.PostCreationModel;
import com.creato.Models.UserCreationModel;
import com.creato.Models.UserModel;

public interface UsersService {
	public List<UserModel> getAllUsers();
	
	public ResponseEntity<?> createUser(UserCreationModel model);
	
	public Map<String, Boolean> validateUsername(Map<String, String> model);
	
	public ResponseEntity<?> loginUser(LoginModel model);
	
	public ResponseEntity<?> getNotificationCount();
	
	public ResponseEntity<?> getAllNotifications();
	
	public ResponseEntity<?> changeUserStatus(ChangeUserStatusModel username);
	
	public ResponseEntity<?> createGroup(PostCreationModel model);
	
	public ResponseEntity<?> updateGroup(PostCreationModel model);
	
	public ResponseEntity<?> createPicture(PictureCreationModel model);
	
	public ResponseEntity<?> updatePicture(PictureCreationModel model);
	
	public ResponseEntity<?> getPictureById(Long id);
	
	public ResponseEntity<?> getAllImages();
}
