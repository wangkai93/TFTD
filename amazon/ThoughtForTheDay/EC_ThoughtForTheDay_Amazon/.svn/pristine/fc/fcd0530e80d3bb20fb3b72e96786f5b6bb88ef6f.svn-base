/*
 *
 * MyTabActivity.java created at Feb 22, 2012 3:33:01 PM
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

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;
import com.google.analytics.tracking.android.EasyTracker;

/**
*
* @file MyTabActivity.java
* @author Samuel.Cai
* @date Feb 22, 2012
*/
public class MyTabActivity extends TabActivity {
	private  TFTDApplication tftdApp;
	
	private boolean isMdpiTablet;
	//private TabWidget tabs;
	private TabWidget tw;
	private ImageView tab_icon;
	private TextView tab_title;
	private TabHost tabHost;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_bottom);
	    
	    tabHost = getTabHost();  // The activity TabHost
	    
	    checkDeviceIsMdpiTablet();
	    
	    if(isMdpiTablet){
	    	setSpecsForMdpiTablet();
	    }else{
	    	setSpecsForTab();
	    }

	    tabHost.setCurrentTab(0);
	    
	    tftdApp = (TFTDApplication)this.getApplication();
	}
	
	private void setSpecsForMdpiTablet(){
		 TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		    Intent intent;  // Reusable Intent for each tab

		    LinearLayout ll = (LinearLayout)tabHost.getChildAt(0);  
		    tw = (TabWidget)ll.getChildAt(1);  
		    
		    intent = new Intent().setClass(this, ThoughtsActivity.class);
		    spec = tabHost.newTabSpec("Thoughts").setIndicator(createIndicatorView("Thoughts",R.drawable.thoughts_tab)).setContent(intent);
		    tabHost.addTab(spec);

		    // Do the same for the other tabs
		    intent = new Intent().setClass(this, FavoriteActivity.class);
		    spec = tabHost.newTabSpec("Favorite").setIndicator(createIndicatorView("Favorite",R.drawable.favorite_tab)).setContent(intent);
		    tabHost.addTab(spec);

		    intent = new Intent().setClass(this, TipsActivity.class);
		    spec = tabHost.newTabSpec("Tips").setIndicator(createIndicatorView("Tips",R.drawable.howtouse_tab)).setContent(intent);
		    tabHost.addTab(spec);
		    
		    intent = new Intent().setClass(this, EdgarCayceActivity.class);
		    spec = tabHost.newTabSpec("Edgar Cayce").setIndicator(createIndicatorView("Edgar Cayce",R.drawable.edgar_cayce_tab)).setContent(intent);
		    tabHost.addTab(spec);
		    
		    intent = new Intent().setClass(this, InfoActivity.class);
		    spec = tabHost.newTabSpec("Info").setIndicator(createIndicatorView("Info",R.drawable.info_tab)).setContent(intent);
		    tabHost.addTab(spec);
	}
	
	private void setSpecsForTab(){
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    LinearLayout ll = (LinearLayout)tabHost.getChildAt(0);  
	    tw = (TabWidget)ll.getChildAt(1);  
	    
	    intent = new Intent().setClass(this, ThoughtsActivity.class);
	    spec = tabHost.newTabSpec("Thoughts").setIndicator(createIndicatorView("Thoughts",R.drawable.thoughts)).setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, FavoriteActivity.class);
	    spec = tabHost.newTabSpec("Favorite").setIndicator(createIndicatorView("Favorite",R.drawable.favorite)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, TipsActivity.class);
	    spec = tabHost.newTabSpec("Tips").setIndicator(createIndicatorView("Tips",R.drawable.howtouse)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, EdgarCayceActivity.class);
	    spec = tabHost.newTabSpec("Edgar Cayce").setIndicator(createIndicatorView("Edgar Cayce",R.drawable.edgar_cayce)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, InfoActivity.class);
	    spec = tabHost.newTabSpec("Info").setIndicator(createIndicatorView("Info",R.drawable.info)).setContent(intent);
	    tabHost.addTab(spec);
	}
	
	private View createIndicatorView(String label, int icon){
		View tabIndicator =  LayoutInflater.from(this).inflate(R.layout.tab_indicator,tw,false);
		tab_title = (TextView)tabIndicator.findViewById(R.id.tab_title);
	    tab_icon = (ImageView)tabIndicator.findViewById(R.id.tab_icon);
	    tab_title.setText(label);
	    tab_icon.setImageResource(icon);
	    
	    return tabIndicator;
	}
	
	/**
	 * 
	 * 
	 * @return void
	 * @author Samuel.Cai
	 * @date Mar 19, 2012
	 */
	private void checkDeviceIsMdpiTablet(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		if(metrics.heightPixels> 640 && metrics.widthPixels > 480 && (metrics.density == 1.0f) ){
			isMdpiTablet = true;
		}else{
			isMdpiTablet = false;
		}
		
		TFTDApplication tftdApplication = (TFTDApplication)getApplication();
		tftdApplication.setMdpiTablet(isMdpiTablet);
	}
	
	// samuel.cai 20120601 add...
	
		/* (non-Javadoc)
		 * @see android.app.ActivityGroup#onPause()
		 */
		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			
			if(tftdApp.checkSoundON()){
				tftdApp.pauseOrStopBGMusic();
			}
		}

		/* (non-Javadoc)
		 * @see android.app.ActivityGroup#onResume()
		 */
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			if(tftdApp.checkSoundON()){
				tftdApp.startBgMusic();
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
