package com.creato.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.creato.Models.PictureCreationModel;
import com.creato.Models.PostCreationModel;
import com.creato.Services.UsersService;

@RestController
@RequestMapping("/picture")
public class PictureController {
	
	@Autowired
	UsersService userService;
	
	@PostMapping("/createGroup")
	public ResponseEntity<?> createGroup(@RequestBody PostCreationModel model){
		return userService.createGroup(model);
	}
	
	@PutMapping("/createGroup")
	public ResponseEntity<?> updateGroup(@RequestBody PostCreationModel model){
		return userService.updateGroup(model);
	}
	
	@PostMapping("/createGroup/delete")
	public ResponseEntity<?> deleteGroup(@RequestBody PostCreationModel model){
		return userService.deleteGroup(model.getId());
	}
	
	@PostMapping
	public ResponseEntity<?> createPicture(@RequestParam String createdBy, @RequestParam Long group, @RequestParam MultipartFile picture, @RequestParam int pictureType) throws IOException{
		PictureCreationModel model = new PictureCreationModel();
		model.setGroup(group);
		model.setCreatedBy(createdBy);
		model.setPicture(picture.getBytes());
		model.setPictureType(pictureType);
		
		return userService.createPicture(model);
	}
	
	@PutMapping
	public ResponseEntity<?> updatePicture(@RequestParam String createdBy, @RequestParam Long id, @RequestParam(required=false) MultipartFile picture, @RequestParam int pictureType) throws IOException{
		PictureCreationModel model = new PictureCreationModel();
		model.setId(id);
		model.setCreatedBy(createdBy);
		if(picture != null) model.setPicture(picture.getBytes());
		model.setPictureType(pictureType);
		
		return userService.updatePicture(model);
	} 
	
	@GetMapping(value="/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> getPicture(@PathVariable("id") Long id){
		return userService.getPictureById(id);
	}
	
	@GetMapping
	public ResponseEntity<?> getPictures(){
		return userService.getAllImages();
	}
	
	@PostMapping("/role")
	public ResponseEntity<?> getPicturesByUsername(@RequestBody PostCreationModel model){
		return userService.getAllImages(model.getUsername());
	}
	
}
