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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;
import com.ceosoftcenters.thoughtfortheday.util.ConstantUtil;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;


/**
 *
 * @file MyTabActivity.java
 * @author Samuel.Cai
 * @date Feb 22, 2012
 */
public class MyTabActivity extends TabActivity {
	// add for license check 20120703...
	private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvu607Dk0rj4klLBNWsXdQapMh+fqwNQLhJbrjkUOjp34IF3YDmRyD0eZrWpsWoediDysO1DsxAhweED1ZoB++oigUreocwJIuTzz3i4W22D4Y5QYKaZmLC/KbgjtszKLwyAKsEV4PpF6uUEqGR8zsxDbRjhA8bve0615iTU51gtRMYo3v0kyy9k1eRvx4qqdtn2xfonR4pjqXeVRnv+HSFuAaPmvjWOk7ukLD4iR7eBdajRsGNInaYA82AhLxULgqEnV7SrPkpGTwIpO8cO8+UO+eV0eUWcI6vwb2WNUubBFukQ8K2gVxMmwa7K5hllAK4wq8dS4heDhaHXYUYBUawIDAQAB";

	// Generate your own 20 random bytes, and put them here.
	private static final byte[] SALT = new byte[] {
		-46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64,
		89
	};
	private LicenseCheckerCallback mLicenseCheckerCallback;
	private LicenseChecker mChecker;
	// A handler on the UI thread.
	private Handler mHandler;


	private  TFTDApplication tftdApp;
	private boolean isMdpiTablet;
	//private TabWidget tabs;
	private TabWidget tw;
	private ImageView tab_icon;
	private TextView tab_title;
	private TabHost tabHost;
	private Uri uriForTwitter; 

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_bottom);

		tabHost = getTabHost();  // The activity TabHost

		checkDeviceIsMdpiTablet();
		uriForTwitter = getIntent().getData();

		System.out.println("5555555555555 isMdpiTablet = " + isMdpiTablet);
		if(isMdpiTablet){
			setSpecsForMdpiTablet();
		}else{
			setSpecsForTab();
		}
		tabHost.setCurrentTab(0);

		tftdApp = (TFTDApplication)this.getApplication();

		// checkLicense();
	}


	private void setSpecsForMdpiTablet(){
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		LinearLayout ll = (LinearLayout)tabHost.getChildAt(0);  
		tw = (TabWidget)ll.getChildAt(1);  
		intent = new Intent().setClass(this, ThoughtsActivity.class).putExtra("uriForTwitter",uriForTwitter == null ? null : uriForTwitter.toString());
		spec = tabHost.newTabSpec("Thoughts").setIndicator(createIndicatorView(R.string.thoughts,R.drawable.thoughts_tab)).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, FavoriteActivity.class);
		spec = tabHost.newTabSpec("Favorite").setIndicator(createIndicatorView(R.string.favorite,R.drawable.favorite_tab)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TipsActivity.class);
		spec = tabHost.newTabSpec("Tips").setIndicator(createIndicatorView(R.string.tips,R.drawable.howtouse_tab)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, EdgarCayceActivity.class);
		spec = tabHost.newTabSpec("Edgar Cayce").setIndicator(createIndicatorView(R.string.edgar_cayce,R.drawable.edgar_cayce_tab)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, InfoActivity.class);
		spec = tabHost.newTabSpec("Info").setIndicator(createIndicatorView(R.string.info,R.drawable.info_tab)).setContent(intent);
		tabHost.addTab(spec);
	}

	private void setSpecsForTab(){
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		LinearLayout ll = (LinearLayout)tabHost.getChildAt(0);  
		tw = (TabWidget)ll.getChildAt(1);  
		intent = new Intent().setClass(this, ThoughtsActivity.class).putExtra("uriForTwitter", uriForTwitter == null ? null : uriForTwitter.toString());
		spec = tabHost.newTabSpec("Thoughts").setIndicator(createIndicatorView(R.string.thoughts,R.drawable.thoughts)).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, FavoriteActivity.class);
		spec = tabHost.newTabSpec("Favorite").setIndicator(createIndicatorView(R.string.favorite,R.drawable.favorite)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TipsActivity.class);
		spec = tabHost.newTabSpec("Tips").setIndicator(createIndicatorView(R.string.tips,R.drawable.howtouse)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, EdgarCayceActivity.class);
		spec = tabHost.newTabSpec("Edgar Cayce").setIndicator(createIndicatorView(R.string.edgar_cayce,R.drawable.edgar_cayce)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, InfoActivity.class);
		spec = tabHost.newTabSpec("Info").setIndicator(createIndicatorView(R.string.info,R.drawable.info)).setContent(intent);
		tabHost.addTab(spec);
	}

	private View createIndicatorView(int label, int icon){
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

	/**
	 * @return void
	 * @author Samuel.Cai
	 * @date Jul 3, 2012
	 */
	private void checkLicense(){
		mHandler = new Handler();

		// Try to use more data here. ANDROID_ID is a single point of attack.
		String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		// Library calls this when it's done.
		mLicenseCheckerCallback = new MyLicenseCheckerCallback();
		// Construct the LicenseChecker with a policy.
		mChecker = new LicenseChecker(
				this, new ServerManagedPolicy(this,
						new AESObfuscator(SALT, getPackageName(), deviceId)),
						BASE64_PUBLIC_KEY);
		mChecker.checkAccess(mLicenseCheckerCallback);
	}

	protected Dialog onCreateDialog(int id) {
		final boolean bRetry = (id == 1);
		return new AlertDialog.Builder(this)
		.setTitle(R.string.unlicensed_dialog_title)
		.setMessage(bRetry ? R.string.unlicensed_dialog_retry_body : R.string.unlicensed_dialog_body)
		.setPositiveButton(bRetry ? R.string.retry_button : R.string.buy_button, new DialogInterface.OnClickListener() {
			boolean mRetry = bRetry;
			public void onClick(DialogInterface dialog, int which) {
				if ( mRetry ) {
					mChecker.checkAccess(mLicenseCheckerCallback);
				} else {
					Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
							"http://market.android.com/details?id=" + getPackageName()));
					startActivity(marketIntent);                        
				}
			}
		})
		.setNegativeButton(R.string.quit_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setCancelable(false).setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(DialogInterface dialog,
					int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_SEARCH){
					System.out.println("you on key Search.... ");
					dialog.dismiss();
					MyTabActivity.this.finish();
				}
				return false;
			}}).create();
	}

	private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
		public void allow(int policyReason) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			// Should allow user access.
			Toast.makeText(MyTabActivity.this, R.string.allow, Toast.LENGTH_SHORT).show();
		}

		public void dontAllow(int policyReason) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			//Toast.makeText(MyTabActivity.this, R.string.dont_allow, Toast.LENGTH_SHORT).show();

			// Should not allow access. In most cases, the app should assume
			// the user has access unless it encounters this. If it does,
			// the app should inform the user of their unlicensed ways
			// and then either shut down the app or limit the user to a
			// restricted set of features.
			// In this example, we show a dialog that takes the user to Market.
			// If the reason for the lack of license is that the service is
			// unavailable or there is another problem, we display a
			// retry button on the dialog and a different message.
			displayDialog(policyReason == Policy.RETRY);
		}

		public void applicationError(int errorCode) {
			System.out.println("<<<<<<<<< errorCode = " + errorCode );
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			// This is a polite way of saying the developer made a mistake
			// while setting up or calling the license checker library.
			// Please examine the error code and fix the error.
			String result = String.format(getString(R.string.application_error), errorCode);
			Toast.makeText(MyTabActivity.this, result, Toast.LENGTH_SHORT).show();
		}
	}


	private void displayDialog(final boolean showRetry) {
		mHandler.post(new Runnable() {
			public void run() {
				//setProgressBarIndeterminateVisibility(false);
				System.out.println("<<<<<<< boolean ShowRetry = " + showRetry );
				showDialog(showRetry ? 1 : 0);
			}
		});
	}    

	//	    @Override
	//	    protected void onDestroy() {
	//	        super.onDestroy();
	//	        mChecker.onDestroy();
	//	    }

	// add in v1.1.5 by Samuel
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this);
	}

}
