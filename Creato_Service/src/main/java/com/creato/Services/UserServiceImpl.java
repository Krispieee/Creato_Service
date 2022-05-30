package com.creato.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.creato.Entities.LookupEntity;
import com.creato.Entities.NotificationEntity;
import com.creato.Entities.PictureEntity;
import com.creato.Entities.PictureGroupEntity;
import com.creato.Entities.UserEntity;
import com.creato.Models.ChangeUserStatusModel;
import com.creato.Models.GroupCreationResponseModel;
import com.creato.Models.GroupResponseModel;
import com.creato.Models.JoinCodeUpdationModel;
import com.creato.Models.LoginModel;
import com.creato.Models.NotificationResponseModel;
import com.creato.Models.PictureCreationModel;
import com.creato.Models.PictureResponseModel;
import com.creato.Models.PostCreationModel;
import com.creato.Models.UpdatePasswordModel;
import com.creato.Models.UserCreationModel;
import com.creato.Models.UserModel;
import com.creato.Repository.LookupRepository;
import com.creato.Repository.NotificationRepository;
import com.creato.Repository.PictureGroupRepository;
import com.creato.Repository.PictureRepository;
import com.creato.Repository.UsersRepository;
import com.creato.Utils.JwtUtil;

@Service
public class UserServiceImpl implements UsersService {

	@Autowired
	UsersRepository userRepo;

	@Autowired
	LookupRepository lookupRepo;

	@Autowired
	NotificationRepository notiRepo;

	@Autowired
	PictureGroupRepository pictureGroupRepo;

	@Autowired
	PictureRepository pictureRepo;

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
		UserEntity user = userRepo.findByUsername(model.get("value"));
		Map<String, Boolean> resp = new HashMap<String, Boolean>();
		if (user == null) {
			resp.put("valid", true);
		} else {
			resp.put("valid", false);
		}
		return resp;
	}

	@Override
	public ResponseEntity<?> loginUser(LoginModel model) {
		// TODO Auto-generated method stub
		Map<String, String> resp;
		UserEntity userEntity = userRepo.findByCreds(model.getUsername(), model.getPassword());
		if (userEntity != null) {
			if (userEntity.getStatus() == 0) {
				resp = new HashMap<String, String>();
				resp.put("message", "Your Request still in progress");
				return new ResponseEntity<Map<String, String>>(resp, HttpStatus.UNAUTHORIZED);
			} else if (userEntity.getStatus() == 2) {
				resp = new HashMap<String, String>();
				resp.put("message", "Rejected by Admin and we are closing your request");
				userRepo.delete(userEntity);
				return new ResponseEntity<Map<String, String>>(resp, HttpStatus.OK);
			} else {
				resp = new HashMap<String, String>();
				resp.put("access-token", jwt.generateJwt(userEntity));
				return new ResponseEntity<Map<String, String>>(resp, HttpStatus.OK);
			}
		} else {
			resp = new HashMap<String, String>();
			resp.put("message", "Invalid Credentials");
			return new ResponseEntity<Map<String, String>>(resp, HttpStatus.UNAUTHORIZED);
		}
	}

	@Override
	public ResponseEntity<?> getNotificationCount() {
		// TODO Auto-generated method stub
		Map<String, Integer> resp = new HashMap<String, Integer>();
		try {
			List<NotificationEntity> notifications = notiRepo.getUnReadNotification();
			resp.put("count", notifications.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}

	@Override
	public ResponseEntity<?> getAllNotifications() {
		// TODO Auto-generated method stub
		List<NotificationResponseModel> respNotifications = new ArrayList<NotificationResponseModel>();
		List<Order> orderBys = new ArrayList<Sort.Order>();
		orderBys.add(new Order(Sort.Direction.DESC, "isRead"));
		orderBys.add(new Order(Sort.Direction.ASC, "performedAt"));
		try {
			respNotifications = notiRepo.findAll().stream().map((NotificationEntity entity) -> {
				NotificationResponseModel notificationModel = new NotificationResponseModel();
				BeanUtils.copyProperties(entity, notificationModel);
				notificationModel.setPerformedAction(entity.getPerformedAction().getActionDesc());
				notificationModel.setPerformedBy(entity.getPerformedBy().getUsername());
				notificationModel.setActionType(entity.getPerformedAction().getActionType());
				return notificationModel;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.OK).body(respNotifications);
	}

	@Override
	public ResponseEntity<?> changeUserStatus(ChangeUserStatusModel model) {
		// TODO Auto-generated method stub
		Map<String, String> resp = new HashMap<String, String>();
		try {
			UserEntity userEntity = userRepo.findByUsername(model.getUsername(), 0);
			if (userEntity != null) {
				userEntity.setStatus(model.getStatus());
				userRepo.save(userEntity);
				notiRepo.deleteById(model.getNotificationId());
				resp.put("message", "Action completed");
			} else {
				resp.put("message", "Not user found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp);
	}

	@Override
	public ResponseEntity<?> createGroup(PostCreationModel model) {
		// TODO Auto-generated method stub
		PictureGroupEntity entity = new PictureGroupEntity();
		GroupCreationResponseModel respModel = new GroupCreationResponseModel();
		try {
			UserEntity userEntity = userRepo.findByUsername(model.getUsername(), 1);
			if(model.getId() != null && model.getId() != 0 ) entity.setId(model.getId());
			entity.setInstaUrl(model.getInstaUrl());
			entity.setCreatedBy(userEntity);
			entity.setUpdatedby(userEntity);
			entity.setStatus(1);
			pictureGroupRepo.save(entity);
			respModel.setId(entity.getId());
			respModel.setInstaUrl(entity.getInstaUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(respModel);
	}

	@Override
	public ResponseEntity<?> createPicture(PictureCreationModel model) {
		// TODO Auto-generated method stub
		PictureEntity entity = new PictureEntity();
		PictureResponseModel respModel = new PictureResponseModel();
		try {

			UserEntity userEntity = userRepo.findByUsername(model.getCreatedBy(), 1);
			PictureGroupEntity groupEntity = pictureGroupRepo.findById(model.getGroup()).get();

			entity.setCreatedBy(userEntity);
			entity.setUpdatedBy(userEntity);
			entity.setGroup(groupEntity);
			entity.setPicture(model.getPicture());
			entity.setPictureType(model.getPictureType());
			pictureRepo.save(entity);
			entity.setImgUrl("picture/"+String.valueOf(entity.getId()));
			pictureRepo.save(entity);
			BeanUtils.copyProperties(entity, respModel);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(respModel);
	}

	@Override
	public ResponseEntity<?> getPictureById(Long id) {
		// TODO Auto-generated method stub
		PictureEntity entity = pictureRepo.findById(id).get();
		Map<String, String> resp = new HashMap<String, String>();
		if(entity != null) {
			return ResponseEntity.status(HttpStatus.OK).body(entity.getPicture());
		}
		resp.put("message", "Invalid request");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}

	@Override
	public ResponseEntity<?> getAllImages() {
		// TODO Auto-generated method stub
		List<GroupResponseModel> respGroup = new ArrayList<GroupResponseModel>();
		List<PictureGroupEntity> toBeDeleted = new ArrayList<PictureGroupEntity>();
		
		try {
			PageRequest page = PageRequest.of(0, 6, Sort.by(Direction.DESC, "createdBy.isAdmin"));

			respGroup = pictureGroupRepo.getAllImages(page).stream().map((PictureGroupEntity groupEntity) -> {
				List<PictureEntity> pictures = groupEntity.getPictures();
				GroupResponseModel groupModel = new GroupResponseModel();
				PictureResponseModel picModel; List<PictureResponseModel> picModels;
				if(pictures.size() != 0) {
					groupModel = new GroupResponseModel();
					picModels = new ArrayList<PictureResponseModel>();
					for(PictureEntity picEntity: pictures) {
						picModel = new PictureResponseModel();
						BeanUtils.copyProperties(picEntity, picModel);
						picModels.add(picModel);
					}
					BeanUtils.copyProperties(groupEntity, groupModel);
					groupModel.setPictures(picModels);
					return groupModel;
					
					
					
				}else {
					toBeDeleted.add(groupEntity);
				}
				return null;
			}).collect(Collectors.toList());
			toBeDeleted.stream().forEach(pictureGroupRepo::delete);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(respGroup);
	}
	
	@Override
	public ResponseEntity<?> getAllImages(String username) {
		// TODO Auto-generated method stub
		List<GroupResponseModel> respGroup = new ArrayList<GroupResponseModel>();
		List<PictureGroupEntity> toBeDeleted = new ArrayList<PictureGroupEntity>();
		List<PictureGroupEntity> groups = new ArrayList<PictureGroupEntity>();
		try {
			UserEntity user = userRepo.findByUsername(username, 1);
			if(user.getIsAdmin() == 1) {
				groups = pictureGroupRepo.findAll();
			}else {
				groups = pictureGroupRepo.getPictureByUsername(user);
			}
			respGroup = groups.stream().map((PictureGroupEntity groupEntity) -> {
				List<PictureEntity> pictures = groupEntity.getPictures();
				GroupResponseModel groupModel = new GroupResponseModel();
				PictureResponseModel picModel; List<PictureResponseModel> picModels;
				if(pictures.size() != 0) {
					groupModel = new GroupResponseModel();
					picModels = new ArrayList<PictureResponseModel>();
					for(PictureEntity picEntity: pictures) {
						picModel = new PictureResponseModel();
						BeanUtils.copyProperties(picEntity, picModel);
						picModels.add(picModel);
					}
					BeanUtils.copyProperties(groupEntity, groupModel);
					groupModel.setPictures(picModels);
					return groupModel;
					
					
					
				}else {
					toBeDeleted.add(groupEntity);
				}
				return null;
			}).collect(Collectors.toList());
			toBeDeleted.stream().forEach(pictureGroupRepo::delete);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(respGroup);
	}

	@Override
	public ResponseEntity<?> updatePicture(PictureCreationModel model) {
		// TODO Auto-generated method stub
		PictureEntity entity;
		PictureResponseModel respModel = new PictureResponseModel();
		try {			
			entity = pictureRepo.getById(model.getId());
			entity.setUpdatedBy(userRepo.findByUsername(model.getCreatedBy(), 1));
			entity.setPictureType(model.getPictureType());
			if(model.getPicture() != null && model.getPicture().length > 0) entity.setPicture(model.getPicture());
			pictureRepo.save(entity);
			BeanUtils.copyProperties(entity, respModel);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(respModel);
	}

	@Override
	public ResponseEntity<?> updateGroup(PostCreationModel model) {
		// TODO Auto-generated method stub
		PictureGroupEntity entity;
		GroupResponseModel respModel = new GroupResponseModel();
		try {			
			entity = pictureGroupRepo.getById(model.getId());
			entity.setInstaUrl(model.getInstaUrl());
			entity.setUpdatedby(userRepo.findByUsername(model.getUsername(), 1));
			pictureGroupRepo.save(entity);
			BeanUtils.copyProperties(entity, respModel);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(respModel);
	}

	@Override
	public ResponseEntity<?> deleteGroup(Long id) {
		// TODO Auto-generated method stub
		PictureGroupEntity group = new PictureGroupEntity();
		Map<String, String> resp = new HashMap<String, String>();
		try {
			group = pictureGroupRepo.findById(id).get();
			pictureGroupRepo.delete(group);
			resp.put("message", "success");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}

	@Override
	public ResponseEntity<?> markNotificationAsRead(NotificationResponseModel model) {
		// TODO Auto-generated method stub
		Map<String, String> respModel = new HashMap<String, String>();
		try {
			NotificationEntity entity = notiRepo.findById(model.getId()).get();
			if(model.getPerformedAction().equals("read")) {
				entity.setIsRead(1);
				notiRepo.save(entity);
				respModel.put("message", "Marked as read");
			}else {
				notiRepo.delete(entity);
				respModel.put("message", "Deleted");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(respModel);
	}

	@Override
	public ResponseEntity<?> getJoinCode(UserCreationModel model) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepo.findByCreds(model.getUsername(), model.getPassword());
		Map<String, String> respModel = new HashMap<String, String>();
		if(userEntity != null) {			
			LookupEntity lookup = lookupRepo.findLookupData("JOIN_CODE");
			respModel.put("joinCode", lookup.getLookupValue());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(respModel);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@Override
	public ResponseEntity<?> updateJoinCode(JoinCodeUpdationModel model) {
		// TODO Auto-generated method stub
		Map<String, String> respModel = new HashMap<String, String>();
		try {
			LookupEntity lookup = lookupRepo.findLookupData("JOIN_CODE");
			lookup.setLookupValue(model.getJoinCode());
			lookupRepo.save(lookup);
			respModel.put("joinCode", lookup.getLookupValue());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(respModel);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@Override
	public ResponseEntity<?> updatePassword(UpdatePasswordModel model) {
		// TODO Auto-generated method stub
		Map<String, String> respModel = new HashMap<String, String>();
		try{
			UserEntity userEntity = userRepo.findByCreds(model.getUsername(), model.getOldPassword());
			if(userEntity != null) {
				userEntity.setPassword(model.getNewPassword());
				userRepo.save(userEntity);
				respModel.put("message", "updated");
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(respModel);
				
			}else {
				respModel.put("message", "Old Password incorrect");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respModel);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respModel);
	}

	

}
