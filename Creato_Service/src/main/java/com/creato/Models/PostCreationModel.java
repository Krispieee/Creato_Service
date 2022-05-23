package com.creato.Models;

import java.sql.Timestamp;

import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.creato.Entities.UserEntity;

public class PostCreationModel {
	private Long id;

	private String instaUrl;
	
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInstaUrl() {
		return instaUrl;
	}

	public void setInstaUrl(String instaUrl) {
		this.instaUrl = instaUrl;
	}
	
	
	
	

}
