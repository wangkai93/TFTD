/*
 *
 * FavoriteActivity.java created at Jan 31, 2012 10:51:46 AM
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
package com.ceosoftcenters.thoughtfortheday.activity;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.adapter.FavoriteListBaseAdapter;
import com.ceosoftcenters.thoughtfortheday.adapter.FavoriteListSimpleAdapter;
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsInfoOperater;
import com.ceosoftcenters.thoughtfortheday.util.Datamodel;
import com.ceosoftcenters.thoughtfortheday.util.ReadBitmapUtil;
import com.google.analytics.tracking.android.EasyTracker;

/**
 *
 * @file FavoriteActivity.java
 * @author Emily.Zhou
 * @date Jan 31, 2012
 */
public class FavoriteActivity extends BaseActivity{
	private ViewFlipper favoritesViewFlipper;


	// the following widgets belong to favorite list view
	private Button edit;
	private ListView favoriteList;

	private TextView favoriteTitle;

	private List<HashMap<String,String>> favoriteListData;
	private ThoughtsInfoOperater opertor;

	private GestureDetector gestureDetector;
	private OnTouchListener gestureListener;

	private View lastModifyReadStatusView;

	private String selectedId;
	private FavoriteListSimpleAdapter adapter;
	private FavoriteListBaseAdapter adapter1;
	// the following widgets belong to reading view
	private RelativeLayout readingRL ;
	private Button favorites;
	private TextView titleText;
	private WebView wv;

	private String selectedItemTitle,selectedItemBody,selectedItemBgIndex;

	private DisplayMetrics dm;
	private String content;

	private int bodySize,titleSize;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.favorites);
		favoritesViewFlipper = (ViewFlipper)findViewById(R.id.favoritesViewFlipper);

		getAllWidgetInstance();

		//		if(tftdApp.isMdpiTablet()){
		//			bodySize = 30;
		//			titleSize = 40;
		//		}else{
		//			bodySize = 20;
		//			titleSize = 30;
		//		}

		String tabletCheck = this.getResources().getString(R.string.isTablet);
		if("2".equals(tabletCheck)){
			bodySize = 30;
			titleSize = 40;
		}else if ("1".equals(tabletCheck)){
			bodySize = 27;
			titleSize = 37;
		}else{
			bodySize = 20;
			titleSize = 30;
		}
	}

	public void setDataFavoriteList(){

		String temp = edit.getText().toString();
		if("Done".equals(temp)){
			edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.done_btn));
			int[] to = {R.id.bt,R.id.title, R.id.body,R.id.favoriteId,R.id.del};
			adapter1 = new FavoriteListBaseAdapter(FavoriteActivity.this, R.layout.favorite_item_del, favoriteListData, to);
			favoriteList.setAdapter(adapter1);
			favoriteList.invalidate();
		}else{
			edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_btn));
			adapter = new FavoriteListSimpleAdapter(
					FavoriteActivity.this,
					favoriteListData,
					R.layout.favorite_item,
					new String[]{"title","context","id"},
					new int[]{R.id.title, R.id.body,R.id.favoriteId}
					);
			favoriteList.setAdapter(adapter);
			favoriteList.invalidate();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reSetDataForFavoriteList();
		setDataFavoriteList();
	}

	private void getAllWidgetInstance(){
		getWidgetInstanceForFavoriteListView();
		getWidgetInstanceForReadingView();
	}

	private void getWidgetInstanceForFavoriteListView(){
		favoriteTitle = (TextView)findViewById(R.id.favoriteTitle);
		favoriteTitle.setTypeface(tftdApp.boldFont);
		edit = (Button)findViewById(R.id.rightButton);
		edit.setTextColor(Color.WHITE);
		edit.setText("Edit");
		edit.setTypeface(TFTDApplication.normalFont);
		edit.setTag("EditTag");

		favoriteList = (ListView)findViewById(android.R.id.list);	

		edit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String temp = arg0.getTag().toString();
				if("EditTag".equals(temp)){
					if(lastModifyReadStatusView!= null){
						resetLastView(lastModifyReadStatusView);
					}else{
						edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.done_btn));
						edit.setText("Done");
						edit.setTag("DoneTag");
						int[] to = {R.id.bt,R.id.title, R.id.body,R.id.favoriteId,R.id.del};
						adapter1 = new FavoriteListBaseAdapter(FavoriteActivity.this, R.layout.favorite_item_del, favoriteListData, to);
						favoriteList.setAdapter(adapter1);
						favoriteList.invalidate();
					}
				}else{
					edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_btn));
					edit.setText("Edit");
					edit.setTag("EditTag");
					adapter = new FavoriteListSimpleAdapter(
							FavoriteActivity.this,
							favoriteListData,
							R.layout.favorite_item,
							new String[]{"title","context","id"},
							new int[]{R.id.title, R.id.body,R.id.favoriteId}
							);
					favoriteList.setAdapter(adapter);
					favoriteList.invalidate();
				}
			}
		});

		reSetDataForFavoriteList();	

		favoriteList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Datamodel.vector = null;
				selectedItemTitle = favoriteListData.get(arg2).get("title").toString();
				selectedItemBody = favoriteListData.get(arg2).get("context");
				selectedItemBgIndex = "" + arg2 % 7;
				gotoReadingView();
			}		
		});		

		favoriteList.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				
				new AlertDialog.Builder(FavoriteActivity.this).setMessage(getString(R.string.want_to_delete,favoriteListData.get(position).get("title").toString()))
				.setTitle("Warning")
				.setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
		                opertor.delete(Integer.parseInt(favoriteListData.get(position).get("id")));	
						favoriteListData.remove(position);
						adapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				})
				.setNegativeButton(R.string.str_no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();


				return true;
			}

		});

		gestureDetector = new GestureDetector(new MyGestureDetector()); 
		gestureListener = new View.OnTouchListener() { 
			public boolean onTouch(View v, MotionEvent event) { 
				return gestureDetector.onTouchEvent(event);
			} 
		}; 
		favoriteList.setOnTouchListener(gestureListener);
	}

	private void getWidgetInstanceForReadingView(){
		readingRL = (RelativeLayout)findViewById(R.id.readings);
		dm =  this.getResources().getDisplayMetrics();

		favorites = (Button)findViewById(R.id.favoritesBtn);
		favorites.setText(R.string.favorites);
		favorites.setTextColor(Color.WHITE);
		favorites.setTypeface(TFTDApplication.normalFont);

		titleText = (TextView)findViewById(R.id.title);
		wv = (WebView)findViewById(R.id.event);
		favorites.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				gotoFavoriteListView();
				setWebViewContent(wv, "");
			}
		});
	}

	private void gotoFavoriteListView(){
		favoritesViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),   
				R.anim.push_right_in));  
		favoritesViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),   
				R.anim.push_right_out));  
		favoritesViewFlipper.showPrevious();
	}

	private void gotoReadingView(){
		favoritesViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),   
				R.anim.push_left_in));  
		favoritesViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),   
				R.anim.push_left_out));  
		favoritesViewFlipper.showNext();
		setDataForReadingView();
	}

	private void setDataForReadingView(){
		titleText.setText(selectedItemTitle);
		titleText.setTypeface(TFTDApplication.boldFont);

		content = "<html><head><style type='text/css'>p{color:white;font-size:"+bodySize+"px;font-family:Arial;text-align:center;text-shadow:1px 1px 2px #000000;}</style></head><body><p style='font-size:"+titleSize+"px;font-family:Arial;text-align:center;'>"+selectedItemTitle+"</p>"+selectedItemBody+"</body></html>"; 
		setWebViewContent(wv, content);
		readingRL.setBackgroundDrawable(new BitmapDrawable(ReadBitmapUtil.readBitMap(FavoriteActivity.this,tftdApp.imageIDs[Integer.parseInt(selectedItemBgIndex)])));
	}

	public void reSetDataForFavoriteList(){	
		opertor = new ThoughtsInfoOperater(this);
		favoriteListData = opertor.queryAllFavorites();
		adapter = new FavoriteListSimpleAdapter(
				this,
				favoriteListData,
				R.layout.favorite_item,
				new String[]{"title","context","id"},
				new int[]{R.id.title, R.id.body,R.id.favoriteId}
				);
		favoriteList.setAdapter(adapter);
	}

	/**
	 *
	 * @param pos 
	 * @return void
	 * @author Emily.Zhou
	 * @date 2012-2-15
	 */
	private void myOnItemClick(int pos) {
		// TODO Auto-generated method stub
		int firstVisibalePosition = favoriteList.getFirstVisiblePosition();
		final View v = favoriteList.getChildAt(pos - firstVisibalePosition);
	}

	class MyGestureDetector extends SimpleOnGestureListener{ 
		// Detect a single-click and call my own handler.
		@Override 
		public boolean onSingleTapUp(MotionEvent e) {
			resetLastView(lastModifyReadStatusView);
			int pos = favoriteList.pointToPosition((int)e.getX(), (int)e.getY()); 
			if(pos >= 0){
				myOnItemClick(pos);
			}
			return false;
		}

		/** 
		 * listen sliding
		 */  
		@Override 
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if ("Edit".equals(edit.getText().toString())){
				resetLastView(lastModifyReadStatusView);
				if(e1 != null && e2 != null){
					if(((Math.abs(e2.getX() - e1.getX()) > 30) && (Math.abs(e2.getY() - e1.getY()) < 20))){
						int pos = favoriteList.pointToPosition((int)e1.getX(), (int)e1.getY());
						if(pos >= 0){ 
							changeItemStyle(pos);
						}
					}
				}
			}
			return false; 
		} 

		private void changeItemStyle(final int pos) {
			// TODO Auto-generated method stub
			int firstVisibalePosition = favoriteList.getFirstVisiblePosition();
			final View v = favoriteList.getChildAt(pos - firstVisibalePosition);
			lastModifyReadStatusView = v;

			if(v != null){
				final Button del = (Button)v.findViewById(R.id.del);
				del.setTypeface(TFTDApplication.normalFont);

				Button rightBt = (Button)v.findViewById(R.id.rightBt);
				del.setVisibility(Button.VISIBLE);
				rightBt.setVisibility(Button.GONE);

				TextView favoriteId = (TextView)v.findViewById(R.id.favoriteId);
				selectedId = favoriteId.getText().toString();
				del.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						opertor = new ThoughtsInfoOperater(FavoriteActivity.this);
						opertor.delete(Integer.parseInt(selectedId));                   
						favoriteListData.remove(pos);                   
						//refresh the interface
						adapter.notifyDataSetChanged();
						getWidgetInstanceForFavoriteListView();
						lastModifyReadStatusView = null;
					}
				});
			}
		} 
	}

	/**
	 * 
	 *
	 * @param v 
	 * @return void
	 * @author Emily.Zhou
	 * @date Feb 15, 2012
	 */
	private void resetLastView(View v){
		if(v != null){
			Button del = (Button)v.findViewById(R.id.del);
			Button rightBt = (Button)v.findViewById(R.id.rightBt);
			del.setVisibility(Button.GONE);
			rightBt.setVisibility(Button.VISIBLE);
		}
		lastModifyReadStatusView = null;
	}

	//samuel.cai 20120605 add...
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("FavoriteActivity onDestroy().... ");
		if (opertor != null) {
			opertor.close();
		}
	}

	// add in v1.1.5 by Samuel
	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this);
	}
}
