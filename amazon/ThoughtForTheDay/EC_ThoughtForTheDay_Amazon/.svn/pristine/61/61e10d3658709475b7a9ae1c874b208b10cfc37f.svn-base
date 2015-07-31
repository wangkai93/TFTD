/*
 *
 * ThoughtsActivity.java created at Jan 31, 2012 10:52:07 AM
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.adapter.ThoughtsAdapter;
import com.ceosoftcenters.thoughtfortheday.facebook.AsyncFacebookRunner;
import com.ceosoftcenters.thoughtfortheday.facebook.BaseDialogListener;
import com.ceosoftcenters.thoughtfortheday.facebook.DialogError;
import com.ceosoftcenters.thoughtfortheday.facebook.Facebook;
import com.ceosoftcenters.thoughtfortheday.facebook.Facebook.DialogListener;
import com.ceosoftcenters.thoughtfortheday.facebook.FacebookError;
import com.ceosoftcenters.thoughtfortheday.facebook.PostFacebookItem;
import com.ceosoftcenters.thoughtfortheday.facebook.SessionEvents;
import com.ceosoftcenters.thoughtfortheday.facebook.SessionStore;
import com.ceosoftcenters.thoughtfortheday.http.HttpClient;
import com.ceosoftcenters.thoughtfortheday.json.EventListModel;
import com.ceosoftcenters.thoughtfortheday.model.pojo.Event;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsForTheDayOperater;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsInfoOperater;
import com.ceosoftcenters.thoughtfortheday.twitter.TwitterApp;
import com.ceosoftcenters.thoughtfortheday.twitter.TwitterApp.TwDialogListener;
import com.ceosoftcenters.thoughtfortheday.util.ConstantUtil;
import com.ceosoftcenters.thoughtfortheday.util.ReadBitmapUtil;
import com.ceosoftcenters.thoughtfortheday.widget.ThoughtsGallery;
import com.google.analytics.tracking.android.EasyTracker;

/**
 *
 * @file ThoughtsActivity.java
 * @author Emily.Zhou
 * @date Jan 31, 2012
 */
public class ThoughtsActivity extends BaseActivity {
	//samuel.cai 20120601 add...
	private boolean isNewActivity = false;
		
	private ViewFlipper thoughtsViewFlipper;
	
	// the properties and widgets of splashView as follows:
	private RelativeLayout splashRL;
	private MyHandler myHandler;
	private static int GETDATA_FROM_BACKEND_SUCCESS = 1;
	private static int GETDATA_FROM_BACKEND_NO_INTERNET = 2;
	private static int GETDATA_FROM_BACKEND_ERROR_INTERNET = 3;
	private static int UNNECESSARY_RE_GETDATA_FROM_BACKEND = 4;
	
	private ThoughtsForTheDayOperater tftdOperater;
	
	// the widget of thoughtsView as follows
	private Button leftButton,rightButton;
	private TextView title;
	private WebView event;
	private ThoughtsGallery gallery;
	private  int year,month,day;
	
	private ArrayList<Event> list = null;
	private int index=0; 

	private ThoughtsInfoOperater opertor ;

	private static Calendar calendar = Calendar.getInstance();

	private ThoughtsAdapter thoughtsAdapter;	

	// samuel 20120308 add...
	// share function
	private String app_url_bitly = "";
	protected AsyncFacebookRunner mAsyncRunner;
	private Facebook mFacebook = null;
	private PostFacebookItem postFacebookItem = null;

	private TwitterApp mTwitter;
	private enum FROM {
		TWITTER_POST, TWITTER_LOGIN
	};

	private enum MESSAGE {
		SUCCESS, DUPLICATE, FAILED, CANCELLED
	};

	private View.OnClickListener buttonListener = new OnClickListener() {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.shareButton){
				String[] choices = { "Share With Email", "Share With Facebook",
						"Share With Twitter", "Cancel" };
				AlertDialog.Builder dialog = null;
				if (Build.VERSION.SDK_INT >= 11) {
					dialog = new AlertDialog.Builder(ThoughtsActivity.this,AlertDialog.THEME_HOLO_LIGHT);
				}else{
					dialog = new AlertDialog.Builder(ThoughtsActivity.this);
				}
				dialog.setTitle("Share this App")
				.setItems(choices, onselectedListener).create();
				
				dialog.show();
			}else if(id == R.id.addButton){
				// get current data from adapter...
				if(opertor.insert(list.get(6-index))){
					v.setVisibility(View.INVISIBLE);
					Toast.makeText(ThoughtsActivity.this, R.string.add_success, Toast.LENGTH_SHORT).show();
					tftdApp.bolFavourite[6-index] = 1;
				}else{
					Toast.makeText(ThoughtsActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
				}
			}

		}
	};

	private void prepareForShareFunction(){
		// use for share function:
		// use for post to wall
		app_url_bitly = ConstantUtil.APP_URL_IN_MARKET_BITLY;

		// use for facebook
		mFacebook = new Facebook();
		postFacebookItem = new PostFacebookItem(ConstantUtil.POST_FACEBOOK_ITEM_TITLE,app_url_bitly,ConstantUtil.POST_FACEBOOK_ITEM_SUMMARY,ConstantUtil.POST_FACEBOOK_ITEM_ICON_SRC);
		mFacebook.setAttachment(postFacebookItem);

		// use for twitter
		mTwitter = new TwitterApp(this, ConstantUtil.CONSUMER_KEY, ConstantUtil.CONSUMER_SECRET);
	}

	private void getAllWidgetInstance(){
		getWidgetsOfSplashView();
		getWidgetsOfThoughtsView();
	}
	
	private void getWidgetsOfSplashView(){
		splashRL = (RelativeLayout)findViewById(R.id.splashRL);
		splashRL.setBackgroundDrawable(new BitmapDrawable(ReadBitmapUtil.readBitMap(this, R.drawable.splash_bg_tab)));
		tftdOperater = new ThoughtsForTheDayOperater(this);

		myHandler = new MyHandler();
		new Thread(new MyThread()).start();
	}

	private void getWidgetsOfThoughtsView(){
		leftButton = (Button)findViewById(R.id.leftButton);
		rightButton = (Button)findViewById(R.id.rightButton);
		gallery = (ThoughtsGallery)findViewById(R.id.gallery);
		
		title = (TextView)findViewById(R.id.title);
		title.setTypeface(Typeface.DEFAULT_BOLD,Typeface.BOLD_ITALIC);
		event = (WebView)findViewById(R.id.event);
		title.setText(getDate(getTodayDate()));
		leftButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index--;
				leftButton.setBackgroundResource(R.drawable.left_btn);
				rightButton.setBackgroundResource(R.drawable.right_btn);
				if(index <= 0){
					index = 0;
					leftButton.setClickable(false);
					rightButton.setClickable(true);
					leftButton.setBackgroundResource(R.drawable.pre_button_invisible);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				}else if (index < 6){
					leftButton.setClickable(true);
					rightButton.setClickable(true);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				}
				tftdApp.setSelectedPositionOfGallery(index);
				gallery.setSelection(index);
				title.setText(getDate(getBeforeDate(getTodayDate())));				
			}
		});
		rightButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index++;
				leftButton.setBackgroundResource(R.drawable.left_btn);
				rightButton.setBackgroundResource(R.drawable.right_btn);
				if(index >= 6){
					index = 6;
					leftButton.setClickable(true);
					rightButton.setClickable(false);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton.setBackgroundResource(R.drawable.next_button_invisible);
				}else if (index > 0){
					leftButton.setClickable(true);
					rightButton.setClickable(true);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				}
				tftdApp.setSelectedPositionOfGallery(index);
				gallery.setSelection(index);			
				title.setText(getDate(getAfterDate(getTodayDate())));
			}
		});
		
		
		opertor = new ThoughtsInfoOperater(this);
	}
	
	private void gotoThoughtsView(){
		thoughtsViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),   
				R.anim.push_left_in));  
		thoughtsViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),   
				R.anim.push_left_out));  
		thoughtsViewFlipper.showNext();
		setDataForThoughtsView();
	}
	
	private void setDataForThoughtsView(){
		list = tftdApp.getGalleryDataSet();
		
		int[] to = {R.id.info,R.id.shareButton,R.id.event,R.id.addButton};

		thoughtsAdapter = new ThoughtsAdapter(this, R.layout.gallery_list, tftdApp.getImageIDs(), list, to,buttonListener,tftdApp.isMdpiTablet());
		gallery.setAdapter(thoughtsAdapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				gallery.setSelection(arg2);
				index = arg2;			
				tftdApp.setSelectedPositionOfGallery(arg2);
				if(index == 0){
					//left button should be slided, right button should be clickable
					leftButton.setClickable(false);
					rightButton.setClickable(true);
					leftButton.setBackgroundResource(R.drawable.pre_button_invisible);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				}else if (index == 6){
					leftButton.setClickable(true);
					rightButton.setClickable(false);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton.setBackgroundResource(R.drawable.next_button_invisible);
				}else{
					leftButton.setClickable(true);
					rightButton.setClickable(true);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				}			
				title.setText(getYesterDayStr(6-arg2));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}});
		
		prepareForShareFunction();
		
		index = tftdApp.getSelectedPositionOfGallery();
		if(index == -1 || index == 6){
			index = 6;
			// right button unclickable left button should be clickable 
			rightButton.setClickable(false);
			leftButton.setClickable(true);
			leftButton.setBackgroundResource(R.drawable.left_btn);
			rightButton.setBackgroundResource(R.drawable.next_button_invisible);
		}else if(index == 0){
			//left button should be slided, right button should be clickable
			leftButton.setClickable(false);
			rightButton.setClickable(true);
			leftButton.setBackgroundResource(R.drawable.pre_button_invisible);
			rightButton.setBackgroundResource(R.drawable.right_btn);
		}else{
			leftButton.setClickable(true);
			rightButton.setClickable(true);
			leftButton.setBackgroundResource(R.drawable.left_btn);
			rightButton.setBackgroundResource(R.drawable.right_btn);
		}
		
		gallery.setSelection(index);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isNewActivity = true; // samuel.cai 20120601 add... 
		
		this.setContentView(R.layout.home);
		thoughtsViewFlipper = (ViewFlipper)findViewById(R.id.thoughtsViewFlipper);
		getAllWidgetInstance();
	}

	public  String getDate(Date date){
		String s=date+"";
		String dateEnd=s.substring(4, 7)+""+"."+s.substring(8, 10)+","+s.substring(s.length()-4,s.length());
		return dateEnd;
	}

	public  Date getTodayDate(){
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		return calendar.getTime();
	}

	public static Date getBeforeDate(Date date){      
		calendar.setTime(date);  
		calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - 1);  
		return calendar.getTime();  
	}  

	public static Date getAfterDate(Date date){      
		calendar.setTime(date);  
		calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) + 1); 
		return calendar.getTime(); 
	}

	private String getYesterDayStr(int step){
		calendar.setTime(new Date());  
		calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - step); 
		return getDate(calendar.getTime()) ;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		tftdApp.setSelectedPositionOfGallery(gallery.getSelectedItemPosition());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isNewActivity){
			tftdApp.setBooleanFavorite();
			if(thoughtsAdapter != null){
				thoughtsAdapter.notifyDataSetChanged();
			}
			if(tftdApp.isNeedReloadDataFromBackend()){
				int childIndex = thoughtsViewFlipper.getDisplayedChild();
				if(childIndex == 1){
					showSplashView();
				}
			}
			
			tftdApp.getThoughtsImagesBackground();
		}
		
		
		// samuel.cai 20120601 add... 
		isNewActivity = false;
	}
	
	private void showSplashView(){
		thoughtsViewFlipper.clearAnimation();
		thoughtsViewFlipper.showPrevious();
		new Thread(new MyThread()).start();
	}
	

	private android.content.DialogInterface.OnClickListener onselectedListener = new android.content.DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch(which){
			case 0:
			{
				if(!tftdApp.haveAvaliableNetWork()){
					Toast.makeText(ThoughtsActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
					break;
				}
				Intent mailIntent = new Intent(android.content.Intent.ACTION_SEND);
				mailIntent.setType("text/html"); // use text/plain will list a lot of other apps to react this intent, you we need change to text/html to avoid this situation...
				mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{});
				mailIntent.putExtra(android.content.Intent.EXTRA_CC, new String[]{});
				mailIntent.putExtra(android.content.Intent.EXTRA_BCC, new String[]{});

				String subject = ThoughtsActivity.this.getString(R.string.email_subject);

				mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);  

				String emailBody = getFromAssets("shareHtml.htm");
				if(emailBody == null){
					emailBody = "<div><br/><br/>You can download it from the <a href='"+ app_url_bitly +"'>Amazon Appstore </a> <br/><br/>Send from my Android-powered device. <br /></div> ";
				}else{
					emailBody = emailBody.replace("%@", list.get(6-index).getDescription()).replace("appstore", "Amazon Appstore");
				}

				mailIntent.putExtra(android.content.Intent.EXTRA_TEXT,  Html.fromHtml(emailBody));

				// when you use emulator to send email 
				//mailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(Intent.createChooser(mailIntent, "You need to configure email firstly..."));
				//dialog.dismiss();
			}
			break;
			case 1:
			{
				if(!tftdApp.haveAvaliableNetWork()){
					Toast.makeText(ThoughtsActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
					break;
				}
				if(mFacebook == null){
					break;
				}

				if(mFacebook.isSessionValid()){
					mFacebook.dialog(ThoughtsActivity.this, "stream.publish",new SampleDialogListener());  
				}else{
					mFacebook.authorize(ThoughtsActivity.this, ConstantUtil.APP_ID, ConstantUtil.PERMISSIONS,new LoginDialogListener()); 
				}

			}
			break;
			case 2:
			{
				if(!tftdApp.haveAvaliableNetWork()){
					Toast.makeText(ThoughtsActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
					break;
				}

				mTwitter.setListener(mTwLoginDialogListener);
				mTwitter.resetAccessToken();
				if (mTwitter.hasAccessToken() == true) {
					try {
						mTwitter.updateStatus(ConstantUtil.TWITTER_MESSAGE + app_url_bitly);
						postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
					} catch (Exception e) {
						if (e.getMessage().toString().contains("duplicate")) {
							postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
						}
						e.printStackTrace();
					}
					mTwitter.resetAccessToken();
				} else {
					mTwitter.authorize();
				}

			}
			break;
			case 3:
				break;
			}
		}};

		/**
		 * Show toast messages
		 * 
		 * @param twitterPost
		 * @param success
		 */
		private void postAsToast(FROM twitterPost, MESSAGE success) {
			switch (twitterPost) {
			case TWITTER_LOGIN:
				switch (success) {
				case SUCCESS:
					Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG)
					.show();
					break;
				case FAILED:
					Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
				default:
					break;
				}
				break;
			case TWITTER_POST:
				switch (success) {
				case SUCCESS:
					Toast.makeText(this, "Posted Successfully", Toast.LENGTH_LONG)
					.show();
					break;
				case FAILED:
					Toast.makeText(this, "Posting Failed", Toast.LENGTH_LONG)
					.show();
					break;
				case DUPLICATE:
					Toast.makeText(this,
							"Posting Failed because of duplicate message...",
							Toast.LENGTH_LONG).show();
				default:
					break;
				}
				break;
			}
		}

		/**
		 * Twitter Dialog Listner.
		 */
		private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

			@Override
			public void onError(String value) {
				postAsToast(FROM.TWITTER_LOGIN, MESSAGE.FAILED);
				Log.e("TWITTER", value);
				mTwitter.resetAccessToken();
			}

			@Override
			public void onComplete(String value) {
				try {
					mTwitter.updateStatus(ConstantUtil.TWITTER_MESSAGE + app_url_bitly);
					postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
				} catch (Exception e) {
					if (e.getMessage().toString().contains("duplicate")) {
						postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
					}
					e.printStackTrace();
				}
				mTwitter.resetAccessToken();
			}
		};

		/**
		 * facebook dialog listener
		 *
		 */
		public class SampleDialogListener extends BaseDialogListener {

			public void onComplete(Bundle values) {
				final String postId = values.getString("post_id");
				if (postId != null) {
					Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
					Toast.makeText(ThoughtsActivity.this, R.string.post_wall_success, Toast.LENGTH_SHORT).show();
				} else {
					Log.d("Facebook-Example", "No wall post made");
				}
			}
		}

		protected final class LoginDialogListener implements DialogListener {
			public void onComplete(Bundle values) {
				SessionEvents.onLoginSuccess();
				SessionStore.save(mFacebook, ThoughtsActivity.this);
				Editor sharedata = getSharedPreferences("data", 0).edit();
				sharedata.putBoolean("fb_is_login", true);
				sharedata.commit();
				mFacebook.dialog(ThoughtsActivity.this, "stream.publish",new SampleDialogListener());
			}

			public void onFacebookError(FacebookError error) {
				SessionEvents.onLoginError(error.getMessage());
			}

			public void onError(DialogError error) {
				SessionEvents.onLoginError(error.getMessage());
			}

			public void onCancel() {
				SessionEvents.onLoginError("Action Canceled");
			}
		}

		/**
		 * 
		 *
		 * @param fileName
		 * @return 
		 * @return String
		 * @author Samuel.Cai
		 * @date Mar 7, 2012
		 */
		public String getFromAssets(String fileName) {
			try {
				InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
				BufferedReader bufReader = new BufferedReader(inputReader);
				String line = "";
				String Result = "";
				while ((line = bufReader.readLine()) != null)
					Result += line;
				return Result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		
		
		// add by samuel....
		class MyHandler extends Handler {
			public MyHandler() {

			}

			public MyHandler(Looper l) {
				super(l);
			}

			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (msg.what == GETDATA_FROM_BACKEND_SUCCESS) {
					tftdApp.setSelectedPositionOfGallery(6);
				} else if (msg.what == GETDATA_FROM_BACKEND_NO_INTERNET) {
					Toast.makeText(ThoughtsActivity.this, R.string.no_internet,
							Toast.LENGTH_SHORT).show();
				} else if (msg.what == GETDATA_FROM_BACKEND_ERROR_INTERNET) {
					Toast.makeText(ThoughtsActivity.this, R.string.error_internet,
							Toast.LENGTH_SHORT).show();
				} else{
				//	Toast.makeText(ThoughtsActivity.this, "You have load today's message...",
				//			Toast.LENGTH_SHORT).show();
				}
				tftdApp.setNeedReloadDataFromBackend(false);
				gotoThoughtsView();
			}
		}

		class MyThread implements Runnable {
			public void run() {
				Message msg = new Message();
				
				// 20120322 add...
				if(tftdOperater.checkHaveGetTodayNewMessage(tftdApp.getYesterdaysStr(0) + "%%")){
					tftdApp.setThoughtsDataForGallery(false);
					myHandler.sendEmptyMessage(UNNECESSARY_RE_GETDATA_FROM_BACKEND);
				}else{
					// need handle some situations
					// 02-27 16:36:53.473: W/System.err(1420): java.net.SocketException:
					// Connection reset by peer
					// or other exception
					// before communication with the backend ,you must check the
					// internet is or not avaliable ? If not , toast 'There is no
					// Interent'
					// then, you should get data from local sqlite database, then go
					// into Thoughts View.
					if (tftdApp.haveAvaliableNetWork()) {
						String str = HttpClient.getTheTextFromHttp();
						if (!"".equals(str)) {
							try {
								ArrayList<Event> list = EventListModel.getInstance().getJson(new JSONObject(str));
								int size = list.size();
								Event event = null;
								for (int i = 0; i < size; i++) {
									event = list.get(i);
									tftdOperater.update(event, i);
								}
								tftdApp.setThoughtsDataForGallery(true);

								myHandler.sendEmptyMessage(GETDATA_FROM_BACKEND_SUCCESS);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								tftdApp.setThoughtsDataForGallery(false);
								myHandler.sendEmptyMessage(GETDATA_FROM_BACKEND_ERROR_INTERNET);
							}

						} else {
							tftdApp.setThoughtsDataForGallery(false);
							myHandler.sendEmptyMessage(GETDATA_FROM_BACKEND_ERROR_INTERNET);
						}
					} else {
						tftdApp.setThoughtsDataForGallery(false);
						myHandler.sendEmptyMessage(GETDATA_FROM_BACKEND_NO_INTERNET);
					}
				}
				
				
			}
		}
		
	// samuel.cai 20120605 add...
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("ThoughtsActivity onDestroy().... ");
		if (opertor != null) {
			opertor.close();
		}

		if (tftdOperater != null) {
			tftdOperater.close();
		}
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
