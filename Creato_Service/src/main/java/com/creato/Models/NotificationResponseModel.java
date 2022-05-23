package com.creato.Models;

import java.sql.Timestamp;

public class NotificationResponseModel {
	private Long id;

	private String performedBy;

	private String performedAction;

	private Timestamp performedAt;
	
	private int actionType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

	public String getPerformedAction() {
		return performedAction;
	}

	public void setPerformedAction(String performedAction) {
		this.performedAction = performedAction;
	}

	public Timestamp getPerformedAt() {
		return performedAt;
	}

	public void setPerformedAt(Timestamp performedAt) {
		this.performedAt = performedAt;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

}
