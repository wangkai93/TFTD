/*
 *
 * EventModel.java created at 2012-2-8 ÏÂÎç05:17:10
 *
 *
 *  Copyright (C) 2009-2012 Suzhou CEO Softcenters Co. Ltd.
 *  All rights reserved.
 *
 *   This software is the intellectual property of Suzhou CEO Softcenters Co. Ltd.. 
 *   It is protected by state law, copyright law and/or international treaties. 
 *   Neither receipt nor possession of this software (in any form) confers any right 
 *   to reproduce,use or disclose it, in whole or in part, without written authorization 
 *   from Suzhou CEO Softcenters Co. Ltd..
 *
 */
package com.ceosoftcenters.thoughtfortheday.model.pojo;

public class Event {
	private String createDateStr;
	private String description;
	private String id;
	private String isDeleted;
	private String title;
	
	public Event() {
		super();
	}
	
	public Event(String createDateStr){
		super();
		this.createDateStr=createDateStr;
	}
	
	public Event(String title,String description){
		super();
		this.title=title;
		this.description=description;
	}
	
	public Event(String id,String description,String title,String createDateStr){
		super();
		this.description=description;
		this.id=id;
		this.title=title;
		this.createDateStr = createDateStr;
	}

	public Event(String createDateStr,String description,String id,String isDeleted,String title){
		super();
		this.createDateStr=createDateStr;
		this.description=description;
		this.id=id;
		this.isDeleted=isDeleted;
		this.title=title;
	}
	
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCreateDateStr() {
		return createDateStr;
	}
	
	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "Event [createDateStr=" + createDateStr + ", description="
				+ description + ", id=" + id + ", isDeleted=" + isDeleted
				+ ", title=" + title + "]";
	}
	
}
