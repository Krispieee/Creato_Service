package com.creato.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.creato.Entities.LookupEntity;
import com.creato.Entities.UserEntity;
import com.creato.Models.LoginModel;
import com.creato.Models.UserCreationModel;
import com.creato.Models.UserModel;
import com.creato.Repository.LookupRepository;
import com.creato.Repository.UsersRepository;
import com.creato.Utils.JwtUtil;

@Service
public class UserServiceImpl implements UsersService {

	@Autowired
	UsersRepository userRepo;

	@Autowired
	LookupRepository lookupRepo;
	
	@Autowired
	JwtUtil jwt;
	

	@Override
	public List<UserModel> getAllUsers() {
		List<UserModel> resp = userRepo.getAllUsers().stream().map((UserEntity userEntity) -> {
			UserModel userModel = new UserModel();
			BeanUtils.copyProperties(userEntity, userModel);
			return userModel;
		}).collect(Collectors.toList());
		return resp;
	}

	@Override
	public ResponseEntity<?> createUser(UserCreationModel model) {
		// TODO Auto-generated method stub
		Map<String, String> respMsg;
		LookupEntity lookup = lookupRepo.findLookupData("JOIN_CODE");
		if (lookup.getLookupValue().equalsIgnoreCase(model.getJoinCode())) {
			UserEntity userEntity = userRepo.findByUsername(model.getUsername(), 0);
			if (userEntity == null) {

				userEntity = new UserEntity();
				BeanUtils.copyProperties(model, userEntity);
				userEntity.setIsAdmin(0);
				userEntity.setStatus(0);
				userRepo.save(userEntity);
				return new ResponseEntity<UserEntity>(userEntity, HttpStatus.CREATED);

			} else {
				respMsg = new HashMap<String, String>();
				respMsg.put("message", "Username already exist");
				return new ResponseEntity<Map<String, String>>(respMsg, HttpStatus.BAD_REQUEST);
			}
		} else {
			respMsg = new HashMap<String, String>();
			respMsg.put("message", "Server busy right now");
			return new ResponseEntity<Map<String, String>>(respMsg, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public Map<String, Boolean> validateUsername(Map<String, String> model) {
		// TODO Auto-generated method stub
		UserEntity user = userRepo.findByUsername(model.get("value"), 0);
		Map<String, Boolean> resp = new HashMap<String, Boolean>();
		if(user == null) {
			resp.put("valid", true);
		}else {
			resp.put("valid", false);
		}
		return resp;
	}

	@Override
	public ResponseEntity<?> loginUser(LoginModel model) {
		// TODO Auto-generated method stub
		Map<String, String> resp;
		UserEntity userEntity = userRepo.findByCreds(model.getUsername(), model.getPassword());
		if(userEntity != null) {
			if(userEntity.getStatus() == 0) {
				resp = new HashMap<String, String>();
				resp.put("message", "Your Request still in progress");
				return new ResponseEntity<Map<String, String>>(resp, HttpStatus.UNAUTHORIZED);
			}else {
				resp = new HashMap<String, String>();
				resp.put("access-token", jwt.generateJwt(userEntity));
				return new ResponseEntity<Map<String, String>>(resp, HttpStatus.OK);
			}
		}else {
			resp = new HashMap<String, String>();
			resp.put("message", "Invalid Credentials");
			return new ResponseEntity<Map<String, String>>(resp, HttpStatus.UNAUTHORIZED);
		}
	}

}
