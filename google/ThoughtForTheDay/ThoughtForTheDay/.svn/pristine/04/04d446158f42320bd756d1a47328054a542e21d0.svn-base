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
package com.ceosoftcenters.thoughtfortheday.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ceosoftcenters.thoughtfortheday.model.pojo.Event;

/**
 *
 * @file EventListModel.java
 * @author Emily.Zhou
 * @date Feb 8, 2012
 */
public class EventListModel {
	private  ArrayList<Event> array = null;
	private  String info_type;
	private  String message;
	private  String nowDate;
	private  String success;
	
	private static EventListModel instance;
	private EventListModel(){
		
	}
	
	public static EventListModel getInstance(){
		if(instance == null){
			instance = new EventListModel();
		}
		return instance;
	}
	public  ArrayList<Event> getJson(JSONObject json){
		try {
			JSONArray eventlist = json.getJSONArray("eventList");
			info_type = json.getString("info_type");
			message = json.getString("message");
			nowDate = json.getString("nowDate");
			success = json.getString("success");
			
			array = new ArrayList<Event> (7);
			if(eventlist != null){
				int length = eventlist.length();
				Event em = null;
				try {
					for(int j = 0 ; j < length; j ++)
					{
						em = new Event();
						JSONObject temp = eventlist.getJSONObject(j);						
						em.setCreateDateStr(temp.getString("createDateStr"));
						em.setDescription(temp.getString("description"));
						em.setId(temp.getString("id"));
						em.setIsDeleted(temp.getString("isDeleted"));
						em.setTitle(temp.getString("title"));
						array.add(em);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}
} 
