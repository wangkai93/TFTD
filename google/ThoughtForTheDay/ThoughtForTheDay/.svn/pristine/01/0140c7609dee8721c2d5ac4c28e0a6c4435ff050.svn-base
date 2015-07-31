/*
 *
 * ThoughtsOperate.java created at 2012-2-13 ÏÂÎç03:41:55
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ceosoftcenters.thoughtfortheday.model.pojo.Event;


/**
 *
 * @file ThoughtsInfoOperater.java  ----> favorites function -----> thoughtsInfo table -----> URCD 
 * @author Emily.Zhou
 * @date 2012-2-13
 */
public class ThoughtsInfoOperater {
	private DataBaseOperHelper db;
	private SQLiteDatabase sd;

	public ThoughtsInfoOperater(Context context){
		db=new DataBaseOperHelper(context);
	}

	//samuel.cai 20120704 add...
	public ArrayList<Event> getAllFavorites(){
		ArrayList<Event> favorites = new ArrayList<Event>();
		Event event = null;
		sd=db.getWritableDatabase();
		Cursor cursor = sd.rawQuery("select title,context from thoughtsInfo", null);
		cursor.moveToFirst(); 
		while (!cursor.isAfterLast()){
			event = new Event();
			event.setTitle(cursor.getString(0));
			event.setDescription(cursor.getString(1));
			event.setCreateDateStr("");
			
			favorites.add(event);
			cursor.moveToNext();
		}
		cursor.close();
		sd.close();
		return favorites;
	}
	
	//samuel.cai 20120704 add...
	public void insertFavorite(ArrayList<Event> oldFavorites){
		sd=db.getWritableDatabase();
		
		ContentValues values = null;
		for(Event favorite: oldFavorites){
			System.out.println("<<<<<<< Insert old favorites = " + favorite);
			values = new ContentValues();
			values.put("title", favorite.getTitle());
			values.put("context", favorite.getDescription());
			values.put("createDateStr", favorite.getCreateDateStr());
			sd.insert("thoughtsInfo", null, values);
		}
		
		sd.close();
	}
	
	public boolean insert(Event event){
		sd=db.getWritableDatabase();
		//Cursor cursor = sd.rawQuery("select * from thoughtsInfo where title=?", new String[]{event.getTitle()});
		Cursor cursor = sd.rawQuery("select * from thoughtsInfo where createDateStr=?", new String[]{event.getCreateDateStr()});
		if(cursor.getCount() == 0){
			
			ContentValues values = new ContentValues();
			values.put("title", event.getTitle());
			values.put("context", event.getDescription());
			values.put("createDateStr", event.getCreateDateStr());
			if(sd.insert("thoughtsInfo", null, values) != -1){
				cursor.close();
				sd.close();
				return true;
			}else{
				cursor.close();
				sd.close();
				return false;
			}
		}
		return false;
	}


	public void delete(int id){
		if(sd==null)
			sd=db.getWritableDatabase();
		sd.execSQL("delete from thoughtsInfo where id=?", new Object[]{id});
		sd.close();
	}

	public List<HashMap<String,String>> queryAllFavorites(){
		sd=db.getWritableDatabase();
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		Cursor cursor=sd.rawQuery("select id,title,context from thoughtsInfo", null);
		cursor.moveToFirst(); 
		while (!cursor.isAfterLast()){
			HashMap<String, String>  map = new HashMap<String,String>();
			map.put("id", String.valueOf(cursor.getInt(0)));
			map.put("title", cursor.getString(1));
			map.put("context", cursor.getString(2));
			list.add(map);
			cursor.moveToNext();
		}
		cursor.close();
		sd.close();
		return list;
	}

	/**
	 *
	 * @param createDateStr 
	 * @return boolean
	 * @author Samuel.Cai
	 * @date Mar 9, 2012
	 */
	public boolean alreadyExists(String createDateStr) {
		// TODO Auto-generated method stub
		Cursor cursor = null;
		Boolean flag = true;
		if(createDateStr != null){
			sd=db.getReadableDatabase();
			cursor = sd.rawQuery("select * from thoughtsInfo where createDateStr=?", new String[]{createDateStr.trim()});
			if(cursor.getCount() == 0){
				flag = false;
			}else{
				flag = true;
			}
		}
		cursor.close();
		sd.close();
		return flag;
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
