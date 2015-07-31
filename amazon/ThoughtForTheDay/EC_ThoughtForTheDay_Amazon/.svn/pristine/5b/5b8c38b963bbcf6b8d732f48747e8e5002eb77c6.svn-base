/*
 *
 * TipsActivity.java created at Jan 31, 2012 10:49:28 AM
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
 * @file TipsActivity.java
 * @author Emily.Zhou
 * @date Jan 31, 2012
 */
public class TipsActivity extends BaseActivity {
	private RelativeLayout tipsRL;
	private Button studyReadingsButton,ecMaterialsButton;
	private WebView study_readings_webView,ec_materials_webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.tips);
		getRemainingComponentInstance();

		setWebViewContentFromAssets(study_readings_webView,ConstantUtil.STUDY_READINGS_HTML_URL);
		setWebViewContentFromAssets(ec_materials_webView,ConstantUtil.EC_MATERIALS_HTML_URL);
		
		ec_materials_webView.setVisibility(View.INVISIBLE);

		studyReadingsButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				studyReadingsButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_button_blue));
				ecMaterialsButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_button_white));
				studyReadingsButton.setTextColor(Color.WHITE);
				ecMaterialsButton.setTextColor(Color.GRAY);
				study_readings_webView.setVisibility(View.VISIBLE);
				ec_materials_webView.setVisibility(View.INVISIBLE);
			}
		});
		ecMaterialsButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				studyReadingsButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_button_white));
				ecMaterialsButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_button_blue));
				studyReadingsButton.setTextColor(Color.GRAY);
				ecMaterialsButton.setTextColor(Color.WHITE);
				ec_materials_webView.setVisibility(View.VISIBLE);
				study_readings_webView.setVisibility(View.INVISIBLE);
			}		
		});

	}

	public void getRemainingComponentInstance(){
		tipsRL = (RelativeLayout)findViewById(R.id.tips);
		tipsRL.setBackgroundDrawable(new BitmapDrawable(ReadBitmapUtil.readBitMap(this, R.drawable.howtouse_bg)));
		studyReadingsButton = (Button)findViewById(R.id.studyReadingsButton);
		ecMaterialsButton = (Button)findViewById(R.id.ecMaterialsButton);
		studyReadingsButton.setTypeface(tftdApp.boldFont);
		ecMaterialsButton.setTypeface(tftdApp.boldFont);
		study_readings_webView = (WebView)findViewById(R.id.study_readings_web);
		ec_materials_webView = (WebView)findViewById(R.id.ec_materials_web);
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
