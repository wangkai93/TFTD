/*
 *
 * ThoughtsOperate.java created at 2012-2-13 ����03:41:55
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
package com.ceosoftcenters.thoughtfortheday.sqlite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ceosoftcenters.thoughtfortheday.model.pojo.Event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



/**
 *
 * @file ThoughtsInfoOperater.java
 * @author Emily.Zhou
 * @date 2012-2-13
 */
public class ThoughtsForTheDayOperater {
	private DataBaseOperHelper db;
	private SQLiteDatabase sd;

	public ThoughtsForTheDayOperater(Context context){
		db=new DataBaseOperHelper(context);
	}

	public boolean insert(Event event){
		sd=db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", event.getTitle());
		values.put("context", event.getDescription());
		values.put("createDateStr", event.getCreateDateStr());
		if(sd.insert("thoughtsfortheday", null, values) != -1){
			sd.close();
			return true;
		}else{
			sd.close();
			return false;
		}
	}
	
	public void update(Event event,int id){
		sd = db.getWritableDatabase();
		sd.execSQL("update thoughtsfortheday set title=?,context=?,createDateStr=? where id = ?",  new Object[]{event.getTitle(),event.getDescription(),event.getCreateDateStr(), (id +1)});
		sd.close();
	}
	
	public Event query(Integer id){
		SQLiteDatabase sd = db.getReadableDatabase();
		Event event = null;
		Cursor cursor = sd.rawQuery("select id,context,title,createDateStr from thoughtsfortheday where id=?", new String[]{String.valueOf(id)});
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			event = new Event(String.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getString(2).trim(),cursor.getString(3));
		}
		cursor.close();
		sd.close();
		return event;
	}
	
	public ArrayList<Event> queryAll(){
		SQLiteDatabase sd = db.getReadableDatabase();
		ArrayList<Event> list = new ArrayList<Event>();
		Cursor cursor = sd.rawQuery("select id,context,title,createDateStr from thoughtsfortheday",null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Event event = new Event(String.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getString(2).trim(),cursor.getString(3));
			cursor.moveToNext();
			list.add(event);
		}
		cursor.close();
		sd.close();
		return list;
	}

	/**
	 *
	 * @param createDateStrPrefix 
	 * @return void
	 * @author Samuel.Cai
	 * @date Mar 9, 2012
	 */
	public Event queryByCreateDateStr(String createDateStrPrefix) {
		// TODO Auto-generated method stub
		SQLiteDatabase sd = db.getReadableDatabase();
		Event event = null;
		Cursor cursor = sd.rawQuery("select id,context,title,createDateStr from thoughtsfortheday where createDateStr like ? ", new String[]{createDateStrPrefix});
		int count = cursor.getCount();
		if(count > 0){
			cursor.moveToFirst();
			event = new Event(String.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getString(2).trim(),cursor.getString(3));
		}
		cursor.close();
		sd.close();
		return event;
	}
	
	/**
	 * 
	 *
	 * @param todayDateStrPrefix
	 * @return 
	 * @return boolean
	 * @author Samuel.Cai
	 * @date Mar 21, 2012
	 */
	public boolean checkHaveGetTodayNewMessage(String todayDateStrPrefix){
		SQLiteDatabase sd = db.getReadableDatabase();
		Cursor cursor = sd.rawQuery("select id,context,title,createDateStr from thoughtsfortheday where createDateStr like ? ", new String[]{todayDateStrPrefix});
		int count = cursor.getCount();
		if(count > 0){
			return true;
		}
		cursor.close();
		sd.close();
		return false;
	}
	

	/**
	 * 
	 * @return void
	 * @author Samuel.Cai
	 * @date Jun 5, 2012
	 */
	public void close(){
		if(db != null ){
			db.close();
		}
	}
}
