package com.creato.Entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "action_types")
public class ActionTypes {
	@Id
	private Long id;

	@Column(name = "action_type")
	private int actionType;

	@Column(name = "action_desc")
	private String actionDesc;

	@OneToMany(mappedBy = "performedAction")
	private List<NotificationEntity> notification;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public List<NotificationEntity> getNotification() {
		return notification;
	}

	public void setNotification(List<NotificationEntity> notification) {
		this.notification = notification;
	}

	

}
