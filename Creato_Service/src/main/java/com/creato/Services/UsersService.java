package com.creato.Services;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.creato.Models.LoginModel;
import com.creato.Models.UserCreationModel;
import com.creato.Models.UserModel;

public interface UsersService {
	public List<UserModel> getAllUsers();
	
	public ResponseEntity<?> createUser(UserCreationModel model);
	
	public Map<String, Boolean> validateUsername(Map<String, String> model);
	
	public ResponseEntity<?> loginUser(LoginModel model);
}
