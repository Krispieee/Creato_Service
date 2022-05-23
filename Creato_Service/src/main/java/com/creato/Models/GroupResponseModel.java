package com.creato.Models;

import java.util.List;

public class GroupResponseModel {
	private Long id;

	private String instaUrl;

	private List<PictureResponseModel> pictures;

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

	public List<PictureResponseModel> getPictures() {
		return pictures;
	}

	public void setPictures(List<PictureResponseModel> pictures) {
		this.pictures = pictures;
	}

}
