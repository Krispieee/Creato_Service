package com.creato.Entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performed_by")
	private UserEntity performedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performed_action")
	private ActionTypes performedAction;

	@Column(name = "performed_at")
	private Timestamp performedAt;

	@Column(name = "is_read")
	private int isRead;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(UserEntity performedBy) {
		this.performedBy = performedBy;
	}

	public ActionTypes getPerformedAction() {
		return performedAction;
	}

	public void setPerformedAction(ActionTypes performedAction) {
		this.performedAction = performedAction;
	}

	public Timestamp getPerformedAt() {
		return performedAt;
	}

	public void setPerformedAt(Timestamp performedAt) {
		this.performedAt = performedAt;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

}
