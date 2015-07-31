/*
 *
 * ThoughtSQLiteHelper.java created at 2012-2-13 ÏÂÎç03:32:16
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

import com.ceosoftcenters.thoughtfortheday.util.ConstantUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 *
 * @file DataBaseOperHelper.java
 * @author Emily.Zhou
 * @date 2012-2-13
 */
public class DataBaseOperHelper extends SQLiteOpenHelper{
    
	public DataBaseOperHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DataBaseOperHelper(Context context){
		this(context,ConstantUtil.DATABASE_NAME,null,ConstantUtil.DATABASE_VERSON);
	}
	// Called when the database is created for the first time.
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println(" database onUpdate || oldVersion = " + oldVersion + "  || new Version = " + newVersion);
	}

}