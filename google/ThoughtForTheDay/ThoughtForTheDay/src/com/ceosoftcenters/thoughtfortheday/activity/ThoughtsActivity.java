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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.util.Xml.Encoding;
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
import com.ceosoftcenters.thoughtfortheday.http.HttpClient;
import com.ceosoftcenters.thoughtfortheday.json.EventListModel;
import com.ceosoftcenters.thoughtfortheday.model.pojo.Event;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsForTheDayOperater;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsInfoOperater;
import com.ceosoftcenters.thoughtfortheday.twitter.TwitterUtil;
import com.ceosoftcenters.thoughtfortheday.util.ConstantUtil;
import com.ceosoftcenters.thoughtfortheday.util.ReadBitmapUtil;
import com.ceosoftcenters.thoughtfortheday.widget.ThoughtsGallery;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.analytics.tracking.android.EasyTracker;
import com.hintdesk.core.util.StringUtil;
/**
 *
 * @file ThoughtsActivity.java
 * @author Emily.Zhou
 * @date Jan 31, 2012
 */
public class ThoughtsActivity extends BaseActivity {

	//add by tommy. for twitter.
	private ProgressDialog progressDialog;
	private Uri uriForTwitter; 

	//add by tommy. for facebook.
	private PendingAction pendingAction = PendingAction.NONE;
	private boolean canPresentShareDialog;
	private CallbackManager callbackManager;
	private ProfileTracker profileTracker;
	private ShareDialog shareDialog;
	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
		@Override
		public void onCancel() {
			Log.d("HelloFacebook", "Canceled");
			Toast.makeText(ThoughtsActivity.this, "Share canceled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(FacebookException error) {
			Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
			Toast.makeText(ThoughtsActivity.this, "Share fail", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(Sharer.Result result) {
			Log.d("HelloFacebook", "Success!");
			Toast.makeText(ThoughtsActivity.this, "Share Success", Toast.LENGTH_SHORT).show();
		}

	};

	private enum PendingAction {
		NONE, POST_STATUS_UPDATE
	}

	// samuel.cai 20120601 add...
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
	private Button leftButton, rightButton;
	private TextView title;
	private WebView event;
	private ThoughtsGallery gallery;
	private int year, month, day;

	private ArrayList<Event> list = null;
	private int index = 0;

	private ThoughtsInfoOperater opertor;

	private static Calendar calendar = Calendar.getInstance();

	private ThoughtsAdapter thoughtsAdapter;

	// samuel 20120308 add...
	// share function
	private String app_url_bitly = ConstantUtil.APP_URL_IN_MARKET_BITLY;
	// protected AsyncFacebookRunner mAsyncRunner;
	// private Facebook mFacebook = null;
	// private PostFacebookItem postFacebookItem = null;

	//	private TwitterApp mTwitter;

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
			if (id == R.id.shareButton) {
				String[] choices = { getString(R.string.share_with_email),  getString(R.string.share_with_facebook),
						getString(R.string.share_with_twitter), getString(R.string.cancel) };
				AlertDialog.Builder dialog = null;
				if (Build.VERSION.SDK_INT >= 11) {
					dialog = new AlertDialog.Builder(ThoughtsActivity.this,
							AlertDialog.THEME_HOLO_DARK);
				} else {
					dialog = new AlertDialog.Builder(ThoughtsActivity.this);
				}
				dialog.setTitle(getString(R.string.share_this_app))
				.setItems(choices, onselectedListener).create();

				dialog.show();
			} else if (id == R.id.addButton) {
				// get current data from adapter...
				if (opertor.insert(list.get(6 - index))) {
					v.setVisibility(View.INVISIBLE);
					Toast.makeText(ThoughtsActivity.this, R.string.add_success,
							Toast.LENGTH_SHORT).show();
					tftdApp.bolFavourite[6 - index] = 1;
				} else {
					Toast.makeText(ThoughtsActivity.this, R.string.add_failed,
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	};

	private void prepareForShareFunction() {
		// use for share function:
		// use for post to wall
		app_url_bitly = ConstantUtil.APP_URL_IN_MARKET_BITLY;

		// use for twitter
		//		mTwitter = new TwitterApp(this, ConstantUtil.CONSUMER_KEY,
		//				ConstantUtil.CONSUMER_SECRET);
	}

	private void getAllWidgetInstance() {
		getWidgetsOfSplashView();
		getWidgetsOfThoughtsView();
	}

	private void getWidgetsOfSplashView() {
		splashRL = (RelativeLayout) findViewById(R.id.splashRL);
		splashRL.setBackgroundDrawable(new BitmapDrawable(ReadBitmapUtil
				.readBitMap(this, R.drawable.splash_bg_tab)));
		tftdOperater = new ThoughtsForTheDayOperater(this);

		myHandler = new MyHandler();
		new Thread(new MyThread()).start();
	}

	private void getWidgetsOfThoughtsView() {
		leftButton = (Button) findViewById(R.id.leftButton);
		rightButton = (Button) findViewById(R.id.rightButton);
		gallery = (ThoughtsGallery) findViewById(R.id.gallery);

		title = (TextView) findViewById(R.id.title);
		title.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC);
		event = (WebView) findViewById(R.id.event);
		title.setText(getDate(getTodayDate()));
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index--;
				leftButton.setBackgroundResource(R.drawable.left_btn);
				rightButton.setBackgroundResource(R.drawable.right_btn);
				if (index <= 0) {
					index = 0;
					leftButton.setClickable(false);
					rightButton.setClickable(true);
					leftButton
					.setBackgroundResource(R.drawable.pre_button_invisible);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				} else if (index < 6) {
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
		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index++;
				leftButton.setBackgroundResource(R.drawable.left_btn);
				rightButton.setBackgroundResource(R.drawable.right_btn);
				if (index >= 6) {
					index = 6;
					leftButton.setClickable(true);
					rightButton.setClickable(false);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton
					.setBackgroundResource(R.drawable.next_button_invisible);
				} else if (index > 0) {
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

	private void gotoThoughtsView() {
		thoughtsViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.push_left_in));
		thoughtsViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.push_left_out));
		thoughtsViewFlipper.showNext();
		setDataForThoughtsView();
	}

	private void setDataForThoughtsView() {
		list = tftdApp.getGalleryDataSet();

		int[] to = { R.id.info, R.id.shareButton, R.id.event, R.id.addButton };

		thoughtsAdapter = new ThoughtsAdapter(this, R.layout.gallery_list,
				tftdApp.getImageIDs(), list, to, buttonListener,
				tftdApp.isMdpiTablet());
		gallery.setAdapter(thoughtsAdapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				gallery.setSelection(arg2);
				index = arg2;
				tftdApp.setSelectedPositionOfGallery(arg2);
				if (index == 0) {
					// left button should be slided, right button should be
					// clickable
					leftButton.setClickable(false);
					rightButton.setClickable(true);
					leftButton
					.setBackgroundResource(R.drawable.pre_button_invisible);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				} else if (index == 6) {
					leftButton.setClickable(true);
					rightButton.setClickable(false);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton
					.setBackgroundResource(R.drawable.next_button_invisible);
				} else {
					leftButton.setClickable(true);
					rightButton.setClickable(true);
					leftButton.setBackgroundResource(R.drawable.left_btn);
					rightButton.setBackgroundResource(R.drawable.right_btn);
				}
				title.setText(getYesterDayStr(6 - arg2));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		prepareForShareFunction();

		index = tftdApp.getSelectedPositionOfGallery();
		if (index == -1 || index == 6) {
			index = 6;
			// right button unclickable left button should be clickable
			rightButton.setClickable(false);
			leftButton.setClickable(true);
			leftButton.setBackgroundResource(R.drawable.left_btn);
			rightButton.setBackgroundResource(R.drawable.next_button_invisible);
		} else if (index == 0) {
			// left button should be slided, right button should be clickable
			leftButton.setClickable(false);
			rightButton.setClickable(true);
			leftButton.setBackgroundResource(R.drawable.pre_button_invisible);
			rightButton.setBackgroundResource(R.drawable.right_btn);
		} else {
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

		initFacebook();

		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(ConstantUtil.PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		isNewActivity = true; // samuel.cai 20120601 add...

		this.setContentView(R.layout.home);
		thoughtsViewFlipper = (ViewFlipper) findViewById(R.id.thoughtsViewFlipper);
		getAllWidgetInstance();


		profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile,
					Profile currentProfile) {
				handlePendingAction();
			}
		};

		// Can we present the share dialog for regular links?
		canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
		
		//for twitter
		Log.e("asadfas", "tommy test uri = " + getIntent().getExtras().getString("uriForTwitter"));
		if(getIntent().getStringExtra("uriForTwitter") != null){
			uriForTwitter = Uri.parse(getIntent().getStringExtra("uriForTwitter") );
			closeDialog();
			progressDialog = ProgressDialog.show(ThoughtsActivity.this, getString(R.string.sharing), getString(R.string.please_wait), true, false);
			initControl();
		}
	}

	private void initFacebook(){
		FacebookSdk.sdkInitialize(this.getApplicationContext());

		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				handlePendingAction();
			}

			@Override
			public void onCancel() {
				if (pendingAction != PendingAction.NONE) {
					showAlert();
					pendingAction = PendingAction.NONE;
				}
			}

			@Override
			public void onError(FacebookException exception) {
				if (pendingAction != PendingAction.NONE
						&& exception instanceof FacebookAuthorizationException) {
					showAlert();
					pendingAction = PendingAction.NONE;
				}
			}

			private void showAlert() {
				new AlertDialog.Builder(ThoughtsActivity.this)
				.setTitle(R.string.cancelled)
				.setMessage(R.string.permission_not_granted)
				.setPositiveButton(R.string.ok, null).show();
			}
		});

		shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, shareCallback);

	}


	public String getDate(Date date) {
		String dateEnd = "";
		if(getString(R.string.region_flag).equals("en")) {
			String s = date + "";
		   dateEnd = s.substring(4, 7) + "" + "." + s.substring(8, 10)
					+ "," + s.substring(s.length() - 4, s.length());
		} else {
			SimpleDateFormat sFormat = new SimpleDateFormat("yyyy年MM月dd日");
			dateEnd = sFormat.format(date);
		}
		
		return dateEnd;
	}

	public Date getTodayDate() {
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		return calendar.getTime();
	}

	public static Date getBeforeDate(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR,
				calendar.get(Calendar.DAY_OF_YEAR) - 1);
		return calendar.getTime();
	}

	public static Date getAfterDate(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR,
				calendar.get(Calendar.DAY_OF_YEAR) + 1);
		return calendar.getTime();
	}

	private String getYesterDayStr(int step) {
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				- step);
		return getDate(calendar.getTime());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		tftdApp.setSelectedPositionOfGallery(gallery.getSelectedItemPosition());
		AppEventsLogger.deactivateApp(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isNewActivity) {
			tftdApp.setBooleanFavorite();
			if (thoughtsAdapter != null) {
				thoughtsAdapter.notifyDataSetChanged();
			}
			if (tftdApp.isNeedReloadDataFromBackend()) {
				int childIndex = thoughtsViewFlipper.getDisplayedChild();
				if (childIndex == 1) {
					showSplashView();
				}
			}

			tftdApp.getThoughtsImagesBackground();
		}

		// samuel.cai 20120601 add...
		isNewActivity = false;

		AppEventsLogger.activateApp(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(ConstantUtil.PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	private void showSplashView() {
		thoughtsViewFlipper.clearAnimation();
		thoughtsViewFlipper.showPrevious();
		new Thread(new MyThread()).start();
	}

	private android.content.DialogInterface.OnClickListener onselectedListener = new android.content.DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch (which) {
			case 0: {
				if (!tftdApp.haveAvaliableNetWork()) {
					Toast.makeText(ThoughtsActivity.this, R.string.no_internet,
							Toast.LENGTH_SHORT).show();
					break;
				}
				Intent mailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				mailIntent.setType("text/html"); // use text/plain will list a
				// lot of other apps to
				// react this intent, you we
				// need change to text/html
				// to avoid this
				// situation...
				mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] {});
				mailIntent.putExtra(android.content.Intent.EXTRA_CC,
						new String[] {});
				mailIntent.putExtra(android.content.Intent.EXTRA_BCC,
						new String[] {});

				String subject = ThoughtsActivity.this
						.getString(R.string.email_subject);

				mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

				String emailBody = getFromAssets(getString(R.string.share_html_url));
				if (emailBody == null) {
					Log.d("ivan", "emailBody == null");
					emailBody = "<div><br/><br/>You can download it from the <a href='"
							+ app_url_bitly
							+ "'>Google Market </a> <br/></div> ";
				} else {
					Log.d("ivan", "else");
					emailBody = emailBody.replace("%@",
							list.get(6 - index).getDescription());
				}

				mailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						Html.fromHtml(emailBody));

				// when you use emulator to send email
				// mailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(mailIntent, getString(R.string.need_to_configure_email)));
				// dialog.dismiss();
			}
			break;
			case 1: {
				if (!tftdApp.haveAvaliableNetWork()) {
					Toast.makeText(ThoughtsActivity.this, R.string.no_internet,
							Toast.LENGTH_SHORT).show();
					break;
				}

				onClickPostStatusUpdate();

			}
			break;
			case 2: {
				if (!tftdApp.haveAvaliableNetWork()) {
					Toast.makeText(ThoughtsActivity.this, R.string.no_internet,
							Toast.LENGTH_SHORT).show();
					break;
				}

				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				if (!sharedPreferences.getBoolean(ConstantUtil.PREFERENCE_TWITTER_IS_LOGGED_IN,false) && uriForTwitter == null)
				{
					closeDialog();
					progressDialog = ProgressDialog.show(ThoughtsActivity.this,  getString(R.string.waiting_authorization), getString(R.string.please_wait), true, false); 
					new TwitterAuthenticateTask().execute();
				}
				else
				{
					closeDialog();
					progressDialog = ProgressDialog.show(ThoughtsActivity.this, getString(R.string.sharing), getString(R.string.please_wait), true, false);
					initControl();
				}
			}
			break;
			case 3:
				break;
			}
		}
	};

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
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(fileName));
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
			} else {
				// Toast.makeText(ThoughtsActivity.this,
				// "You have load today's message...",
				// Toast.LENGTH_SHORT).show();
			}
			tftdApp.setNeedReloadDataFromBackend(false);
			gotoThoughtsView();
		}
	}

	class MyThread implements Runnable {
		public void run() {
			Message msg = new Message();
			// 20120322 add...
			if (ConstantUtil.getRegionCode(ThoughtsActivity.this).equals(getString(R.string.region_flag)) && tftdOperater.checkHaveGetTodayNewMessage(tftdApp
					.getYesterdaysStr(0) + "%%")) {
				tftdApp.setThoughtsDataForGallery(false);
				myHandler.sendEmptyMessage(UNNECESSARY_RE_GETDATA_FROM_BACKEND);
			} else {
				// need handle some situations
				// 02-27 16:36:53.473: W/System.err(1420):
				// java.net.SocketException:
				// Connection reset by peer
				// or other exception
				// before communication with the backend ,you must check the
				// internet is or not avaliable ? If not , toast 'There is no
				// Interent'
				// then, you should get data from local sqlite database, then go
				// into Thoughts View.
				if (tftdApp.haveAvaliableNetWork()) {
					String str = HttpClient.getTheTextFromHttp(ThoughtsActivity.this);
					if (!"".equals(str)) {
						try {
							ArrayList<Event> list = EventListModel
									.getInstance().getJson(new JSONObject(str));
							int size = list.size();
							Event event = null;
							for (int i = 0; i < size; i++) {
								event = list.get(i);
								tftdOperater.update(event, i);
							}
							tftdApp.setThoughtsDataForGallery(true);

							myHandler
							.sendEmptyMessage(GETDATA_FROM_BACKEND_SUCCESS);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							tftdApp.setThoughtsDataForGallery(false);
							myHandler
							.sendEmptyMessage(GETDATA_FROM_BACKEND_ERROR_INTERNET);
						}

					} else {
						tftdApp.setThoughtsDataForGallery(false);
						myHandler
						.sendEmptyMessage(GETDATA_FROM_BACKEND_ERROR_INTERNET);
					}
				} else {
					tftdApp.setThoughtsDataForGallery(false);
					myHandler
					.sendEmptyMessage(GETDATA_FROM_BACKEND_NO_INTERNET);
				}
			}
			ConstantUtil.saveRegionCode(ThoughtsActivity.this, getString(R.string.region_flag));
			Log.e("asdf", "tommy test MyThread");

		}
	}

	// samuel.cai 20120605 add...
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (opertor != null) {
			opertor.close();
		}

		if (tftdOperater != null) {
			tftdOperater.close();
		}
		profileTracker.stopTracking();

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


	// add by Tommy, for facebook
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case NONE:
			break;
		case POST_STATUS_UPDATE:
			postStatusUpdate();
			break;
		}
	}

	private void onClickPostStatusUpdate() {
		performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
	}

	private void postStatusUpdate() {
		Profile profile = Profile.getCurrentProfile();
		ShareLinkContent linkContent = new ShareLinkContent.Builder()
		.setContentTitle(ConstantUtil.POST_FACEBOOK_ITEM_TITLE)
		.setContentDescription(ConstantUtil.POST_FACEBOOK_ITEM_SUMMARY)
		.setContentUrl(Uri.parse(ConstantUtil.APP_URL_IN_MARKET))
		.setImageUrl(Uri.parse(ConstantUtil.POST_FACEBOOK_ITEM_ICON_SRC))
		.build();
		if (canPresentShareDialog) {
			shareDialog.show(linkContent);
		} else if (profile != null && hasPublishPermission()) {
			ShareApi.share(linkContent, shareCallback);
		} else {
			pendingAction = PendingAction.POST_STATUS_UPDATE;
		}
	}

	private boolean hasPublishPermission() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null
				&& accessToken.getPermissions().contains("publish_actions");
	}

	private void performPublish(PendingAction action, boolean allowNoToken) {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction();
				return;
			} else {
				// We need to get new permissions, then complete the action when
				// we get called back.
				LoginManager.getInstance().logInWithPublishPermissions(this,
						Arrays.asList(ConstantUtil.PERMISSION));
				return;
			}
		}

		if (allowNoToken) {
			pendingAction = action;
			handlePendingAction();
		}
	}

	//add by Tommy, for twitter
	private void closeDialog(){
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	private void initControl() {
		if (uriForTwitter != null && uriForTwitter.toString().startsWith(ConstantUtil.TWITTER_CALLBACK_URL)) {
			String verifier = uriForTwitter.getQueryParameter(ConstantUtil.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
			new TwitterGetAccessTokenTask().execute(verifier);
		} else
			new TwitterGetAccessTokenTask().execute("");
	}


	class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

		@Override
		protected void onPostExecute(RequestToken requestToken) {
			closeDialog();
			if(requestToken == null){
				Toast.makeText(ThoughtsActivity.this, R.string.no_internet,
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
			startActivity(intent);
			ThoughtsActivity.this.finish();
		}

		@Override
		protected RequestToken doInBackground(String... params) {
			return TwitterUtil.getInstance().getRequestToken();
		}
	}

	class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String userName) {
			String status = ConstantUtil.TWITTER_MESSAGE + ConstantUtil.APP_URL_IN_MARKET;
			if (!StringUtil.isNullOrWhitespace(status)) {
				new TwitterUpdateStatusTask().execute(status);
			} else {
				closeDialog();
				Toast.makeText(getApplicationContext(), getString(R.string.enter_status), Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected String doInBackground(String... params) {

			Twitter twitter = TwitterUtil.getInstance().getTwitter();
			RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
			if (!StringUtil.isNullOrWhitespace(params[0])) {
				try {

					twitter4j.auth.AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString(ConstantUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
					editor.putString(ConstantUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
					editor.putBoolean(ConstantUtil.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
					editor.commit();
					return twitter.showUser(accessToken.getUserId()).getName();
				} catch (TwitterException e) {
					e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
			} else {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String accessTokenString = sharedPreferences.getString(ConstantUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
				String accessTokenSecret = sharedPreferences.getString(ConstantUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
				twitter4j.auth.AccessToken accessToken = new twitter4j.auth.AccessToken(accessTokenString, accessTokenSecret);
				try {
					TwitterUtil.getInstance().setTwitterFactory(accessToken);
					return TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getName();
				} catch (TwitterException e) {
					e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
			}

			return null;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}

	class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			closeDialog();
			if (result)
				Toast.makeText(getApplicationContext(), getString(R.string.tweet_successfully), Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), getString(R.string.make_sure_did_not_share_recently), Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String accessTokenString = sharedPreferences.getString(ConstantUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, "");//PREFERENCE_TWITTER_OAUTH_TOKEN
				String accessTokenSecret = sharedPreferences.getString(ConstantUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");//PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET

				if (!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)) {
					twitter4j.auth.AccessToken accessToken = new twitter4j.auth.AccessToken(accessTokenString, accessTokenSecret);
					twitter4j.Status status = TwitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
					return true;
				}

			} catch (TwitterException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
			return false;  //To change body of implemented methods use File | Settings | File Templates.

		}
	}
}
