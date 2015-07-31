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

public class EventList {
	private Event eventList;
	private String info_Type;
	private String message;
	private String nowDate;
	private String success;

	public EventList() {
		super();
	}
	
	public EventList(Event eventList,String info_Type,String message,String nowDate,String success){
		super();
		this.eventList=eventList;
		this.info_Type=info_Type;
		this.message=message;
		this.nowDate=nowDate;
		this.success=success;
	}

	public void setEventList(Event eventList) {
		this.eventList = eventList;
	}

	public void setInfo_Type(String info_Type) {
		this.info_Type = info_Type;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public Event getEventList() {
		return eventList;
	}

	public String getInfo_Type() {
		return info_Type;
	}

	public String getMessage() {
		return message;
	}

	public String getNowDate() {
		return nowDate;
	}

	public String getSuccess() {
		return success;
	}

	@Override
	public String toString() {
		return "EventList [eventList=" + eventList + ", info_Type=" + info_Type
				+ ", message=" + message + ", nowDate=" + nowDate
				+ ", success=" + success + "]";
	}

}
