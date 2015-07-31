/*
 *
 * EventModel.java created at 2012-2-8 ����05:17:10
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

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;
import com.google.analytics.tracking.android.EasyTracker;

/**
 *
 * @file BaseActivity.java
 * @author Emily.Zhou
 * @date Feb 1, 2012
 */
public class BaseActivity extends Activity{
	private int webviewScale = 100;
	protected TFTDApplication tftdApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		tftdApp = (TFTDApplication)this.getApplication();
		
		Display display = getWindowManager().getDefaultDisplay();
		
//		if(display.getWidth() > 800){
//			webviewScale = 100;
//		}else{
//			webviewScale = (int)((display.getWidth()/800.0f) * 100);
//		}
		String tabletCheck = this.getResources().getString(R.string.isTablet);
		if("2".equals(tabletCheck)){
			webviewScale = 90;
		}else if("1".equals(tabletCheck)){
			webviewScale = 65;
		}else{
			webviewScale = (int)((display.getWidth()/800.0f) * 100);
			if(webviewScale > 90){
				webviewScale = 90;
			}
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	    switch (keyCode){
	    case KeyEvent.KEYCODE_VOLUME_DOWN: 
	    	tftdApp.reSaveMusicVolume();
	         break;
	    case KeyEvent.KEYCODE_VOLUME_UP:
	    	tftdApp.reSaveMusicVolume();
	         break;
//	    case KeyEvent.KEYCODE_BACK:
//	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 *
	 * @param wv
	 * @param fileUrl 
	 * @return void
	 * @author Emily.Zhou
	 * @date 2012-2-9
	 */
	protected void setWebViewContentFromAssets(final WebView wv,String fileUrl){
		wv.setInitialScale(webviewScale);
		//wv.setBackgroundColor(Color.TRANSPARENT);//can't work on many devices test by Samuel in 20140116
		forceWebViewBackgroundTransparent(wv);
		wv.loadUrl(fileUrl);
	}

	/**
	 * 
	 *
	 * @param wv
	 * @param fileData 
	 * @return void
	 * @author Emily.Zhou
	 * @date 2012-2-10
	 */
	protected void setWebViewContent(WebView wv,String data){
		wv.setBackgroundColor(Color.TRANSPARENT);//can't work on HTC g20, but work on Nexus7 and other devices. 
		wv.loadData(data, "text/html", "UTF-8");
	}
	
	/**
	 * fix bug: the background color is black on Nexus 7 with 4.4.2. I think this bug exists on many devices. Because this app can't test on 4.x os
	 * solution come from: http://stackoverflow.com/questions/5003156/android-webview-style-background-colortransparent-ignored-on-android-2-2
	 *  
	 * if you use webview.loadUrl(...) to load html file, you need it.
	 * if you use .loadData(...), it is optional
	 * 
	 * @param wv 
	 * @return void
	 * @author Samuel.Cai
	 * @date Jan 16, 2014
	 */
	private void forceWebViewBackgroundTransparent(final WebView wv) {
		wv.setBackgroundColor(0x00000000);
		if (Build.VERSION.SDK_INT >= 11) {
			wv.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		}

		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				wv.setBackgroundColor(0x00000000);
				if (Build.VERSION.SDK_INT >= 11) {
					wv.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
				}
			}
		});
	}
	
	//add in v1.1.5 by Samuel
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
