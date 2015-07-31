/*
 *
 * ThoughtsAdapter.java created at 2012-2-19 ����01:06:12
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

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;
import com.ceosoftcenters.thoughtfortheday.model.pojo.Event;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsInfoOperater;
import com.ceosoftcenters.thoughtfortheday.util.ReadBitmapUtil;

/**
 * 
 * @file ThoughtsAdapter.java
 * @author Emily.Zhou
 * @date 2012-2-19
 */
public class ThoughtsAdapter extends android.widget.BaseAdapter {
	private ThoughtsInfoOperater opertor;
	private LayoutInflater mInflater;
	private Context context;
	private int mViewResourceId;
	private int[] imageIDs;
	private ArrayList<Event> list;
	private int[] mTo;
	private HashMap<String, Integer> map = new HashMap<String, Integer>();

	private DisplayMetrics dm;
	private int height;
	private int width;

	private String body;

	private Bitmap currentBg = null;

	private int bodySize, titleSize;

	// samuel add...
	private View.OnClickListener listener;

	public ThoughtsAdapter(Context context, int viewRourcesID, int[] imageIDs,
			ArrayList<Event> obj, int[] to, View.OnClickListener listener,
			boolean isMdpiTablet) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mViewResourceId = viewRourcesID;
		this.mTo = to;
		this.imageIDs = imageIDs;
		this.list = obj;

		opertor = new ThoughtsInfoOperater(context);

		this.dm = context.getResources().getDisplayMetrics();

		this.listener = listener;

		// if(isMdpiTablet){
		// bodySize = 30;
		// titleSize = 40;
		// }else{
		// bodySize = 20;
		// titleSize = 30;
		// }

		String tabletCheck = context.getResources()
				.getString(R.string.isTablet);
		if ("2".equals(tabletCheck)) {
			bodySize = 30;
			titleSize = 40;
		} else if ("1".equals(tabletCheck)) {
			bodySize = 27;
			titleSize = 37;
		} else {
			bodySize = 20;
			titleSize = 30;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null)
			return 0;
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (list == null)
			return null;
		return list.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(mViewResourceId, null);
			holder = new ViewHolder();
			holder.relativeLayout = (RelativeLayout) convertView
					.findViewById(mTo[0]);
			holder.leftBt = (Button) convertView.findViewById(mTo[1]);
			holder.webView = (WebView) convertView.findViewById(mTo[2]);
			holder.rightBt = (Button) convertView.findViewById(mTo[3]);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int temp = 6 - position;
		if (temp <= 0) {
			temp = 0;
		} else {
			temp = 6 - position;
		}

		holder.leftBt.setOnClickListener(listener);
		holder.rightBt.setOnClickListener(listener);
		holder.relativeLayout.setBackgroundDrawable(new BitmapDrawable(
				ReadBitmapUtil.readBitMap(context, imageIDs[temp])));

		body = "<html><head><style type='text/css'>body{background-color:transparent;} p{background-color:transparent;color:white;font-size:"
				+ bodySize
				+ "px;font-family:Arial;text-align:center;text-shadow:1px 1px 2px #000000;}</style></head><body><p style='font-size:"
				+ titleSize
//				+ "px;font-family:Times New Roman,Times,serif;text-align:center;'>"
				// Update by Ivan 2015-06-25
				+ "px;font-family:Arial;text-align:center;'>"
				+ list.get(temp).getTitle()
				+ "</p>"
				+ list.get(temp).getDescription() + "</body></html>";

		setWebViewContent(holder.webView, body);

		if (TFTDApplication.bolFavourite[temp] == 1) {
			holder.rightBt.setVisibility(Button.GONE);
		} else {
			holder.rightBt
					.setBackgroundResource(R.drawable.addfav_button_normal);
		}
		return convertView;
	}

	private class ViewHolder {
		RelativeLayout relativeLayout;
		Button leftBt;
		WebView webView;
		Button rightBt;
	}

	private void setWebViewContent(WebView wv, String data) {
		wv.setBackgroundColor(Color.TRANSPARENT);
		wv.loadDataWithBaseURL(null,data, "text/html", "UTF-8",null);
	}
}
