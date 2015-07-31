/*
 *
 * EdgarCayceActivity.java created at Jan 31, 2012 10:49:59 AM
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

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.util.ConstantUtil;
import com.ceosoftcenters.thoughtfortheday.util.ReadBitmapUtil;
import com.google.analytics.tracking.android.EasyTracker;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 *
 * @file EdgarCayceActivity.java
 * @author Emily.Zhou
 * @date Jan 31, 2012
 */

public class EdgarCayceActivity extends BaseActivity {
	private RelativeLayout edgarCayceRL;
	private Button edgarButton,aboutAREButton;
    private WebView edgar_cayce_webView,about_are_webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.edgar_cayce);
		getRemainingComponentInstance();
		
		setWebViewContentFromAssets(edgar_cayce_webView,ConstantUtil.EDGAR_CAYCE_HTML_URL);
		setWebViewContentFromAssets(about_are_webView,ConstantUtil.ABOUT_ARE_HTML_URL);
		about_are_webView.setVisibility(View.INVISIBLE);
  
		edgarButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				edgarButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_button_blue));
				aboutAREButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_button_white));
				edgarButton.setTextColor(Color.WHITE);
				aboutAREButton.setTextColor(Color.GRAY);
				edgar_cayce_webView.setVisibility(View.VISIBLE);
				about_are_webView.setVisibility(View.INVISIBLE);
			}
		});
		aboutAREButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				edgarButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_button_white));
				aboutAREButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_button_blue));
				edgarButton.setTextColor(Color.GRAY);
				aboutAREButton.setTextColor(Color.WHITE);
				about_are_webView.setVisibility(View.VISIBLE);
				edgar_cayce_webView.setVisibility(View.INVISIBLE);
			}
		});
		
	}

	public void getRemainingComponentInstance(){
		edgarCayceRL = (RelativeLayout)findViewById(R.id.edgar_cayce);
		edgarCayceRL.setBackgroundDrawable(new BitmapDrawable(ReadBitmapUtil.readBitMap(this, R.drawable.edgar_cayce_bg)));
		edgarButton = (Button)findViewById(R.id.edgarButton);
		aboutAREButton = (Button)findViewById(R.id.aboutAREButton);
		edgarButton.setTypeface(tftdApp.boldFont);
		aboutAREButton.setTypeface(tftdApp.boldFont);
		edgar_cayce_webView = (WebView)findViewById(R.id.edgar_cayce_web);
		about_are_webView = (WebView)findViewById(R.id.about_are_web);
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
