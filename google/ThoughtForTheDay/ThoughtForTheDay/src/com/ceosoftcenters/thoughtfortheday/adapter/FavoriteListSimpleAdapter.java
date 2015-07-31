/*
 *
 * FavoriteSimpleAdapter.java created at 2012-2-15 ÏÂÎç03:10:08
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
package com.ceosoftcenters.thoughtfortheday.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;

/**
 *
 * @file FavoriteListSimpleAdapter.java
 * @author Emily.Zhou
 * @date 2012-2-15
 */
public class FavoriteListSimpleAdapter extends SimpleAdapter{
	private Button delete,rightBt;
	private List<HashMap<String,String>> favoriteListData;
	private LayoutInflater mInflater;
    private int mViewResourceId;
	
	private TextView title,context;

	
	public void setDate(List<HashMap<String,String>> favoriteListData){
		this.favoriteListData = favoriteListData;
	}
	
	/**
	 *
	 * @param context
	 * @param data
	 * @param resource
	 * @param from
	 * @param to
	 */
	public FavoriteListSimpleAdapter(Context context,  List<HashMap<String,String>> data, int viewRourcesID, String[] from,
			int[] to) {
		super(context, data, viewRourcesID, from, to);
    	this.mInflater = LayoutInflater.from(context);
    	this.mViewResourceId = viewRourcesID;
		this.favoriteListData = data;  
	}
	
	 /**
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = super.getView(position, convertView, parent);		
		delete = (Button)v.findViewById(R.id.del);
		rightBt = (Button)v.findViewById(R.id.rightBt);
		delete.setVisibility(View.GONE);
		rightBt.setVisibility(View.VISIBLE);	
		
		title = (TextView)v.findViewById(R.id.title);
		title.setTypeface(TFTDApplication.boldFont);

		context = (TextView)v.findViewById(R.id.body);
		String text = ""+this.favoriteListData.get(position).get("context");
//		String p = text.replaceAll("<p>", "").replaceAll("&nbsp;</p>", "").replaceAll("&", "").replaceAll("</p>", "");
//		context.setText(p);
		context.setText(Html.fromHtml(text));
		context.setTextColor(Color.parseColor("#465380"));
		context.setTypeface(TFTDApplication.normalFont);
		
		return v;
	}

	
	

}
