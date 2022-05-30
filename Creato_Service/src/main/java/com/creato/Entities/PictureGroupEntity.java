package com.creato.Entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "picture_groups")
public class PictureGroupEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(name = "insta_url")
	public String instaUrl;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", orphanRemoval = true)
	private List<PictureEntity> pictures;

	@Column(name = "created_at")
	@CreationTimestamp
	public Timestamp createdAt;

	@ManyToOne
	@JoinColumn(name = "created_by")
	public UserEntity createdBy;

	@ManyToOne
	@JoinColumn(name = "updated_by")
	public UserEntity updatedby;

	@Column(name = "updated_at")
	@UpdateTimestamp
	public Timestamp updatedAt;

	@Column(name = "status")
	public int status;

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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public UserEntity getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(UserEntity updatedby) {
		this.updatedby = updatedby;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<PictureEntity> getPictures() {
		return pictures;
	}

	public void setPictures(List<PictureEntity> pictures) {
		this.pictures = pictures;
	}

}
