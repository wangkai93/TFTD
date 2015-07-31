/*
 *
 * ConstantUtil.java created at Jan 31, 2012 10:58:12 AM
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
package com.ceosoftcenters.thoughtfortheday.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 *
 * @file ConstantUtil.java
 * @author Samuel.Cai
 * @date Jan 31, 2012
 */
public class ConstantUtil {

	// be used for shareperfrence
	public static final String SETTINGS="app_settings";
	public static final String SOUND_SETTING="is_sound";

	public static final String TODAY="today";
	public static final String TODAYORDER="todays_order";


	public static final String SHAREPREFERENCE_FILENAME = "current_user_info";
	public static final String USERNAME_SP = "username";

//	public static final String URL = "http://54.215.10.171/TFTD/ws/lastWeek"; //"http://50.18.25.81/TFTD/ws/lastWeek"; //this is live server address. 

	//	public static final String EDGAR_CAYCE_HTML_URL = "file:///android_asset/html/edgar_cayce.html";
	//	public static final String ABOUT_ARE_HTML_URL = "file:///android_asset/html/about_are.html";
	//	public static final String STUDY_READINGS_HTML_URL = "file:///android_asset/html/study_readings.htm";
	//	public static final String EC_MATERIALS_HTML_URL = "file:///android_asset/html/ec_materials.htm";
	//
	//	public static final String SHARE_HTML_URL = "file:///android_asset/html/share.htm";

//	public static final String DATABASE_FILE_NAME = "db/TFTD_DB.sqlite.db";
	/** The constant database file name. */
	public static final String DATABASE_NAME = "TFTD_DB.sqlite.db";

	public static final int DATABASE_VERSON = 2; //samuel add 20120627

	public static final String email_say = "Edgar Cayce's 'Thought for the Day' app is a great way to start each day.You cacn download it from the";


	//use for facebook
	public static boolean fb_is_login;
	public static final String APP_ID = "155964484493048";
	public static final String[] PERMISSIONS =  new String[] {"publish_stream"};
	public static final String PERMISSION = "publish_actions";
	public static final String PENDING_ACTION_BUNDLE_KEY =  "com.ceosoftcenters.thoughtfortheday.activity:PendingAction";

	public static final String POST_FACEBOOK_ITEM_TITLE = "Google Market - Edgar Cayce's ThoughtForTheDay";
	public static final String POST_FACEBOOK_ITEM_SUMMARY = "Read reviews, get customer ratings, see screenshots, and learn more about Edgar Cayce's ThoughtForTheDay on the Google Market. Download Edgar Cayce's ThoughtForTheDay and enjoy it on your Android phone, Tablet.";
	//public static final String POST_FACEBOOK_ITEM_URL ="https://market.android.com/details?id=com.ceosoftcenters.smartalert";
	public static final String POST_FACEBOOK_ITEM_ICON_SRC = "http://ceoiphone.s3.amazonaws.com/icons/tftd72.png";

	public static final String APP_URL_IN_MARKET = "https://play.google.com/store/apps/details?id=com.ceosoftcenters.thoughtfortheday";
	public static final String APP_URL_IN_MARKET_BITLY ="http://bit.ly/HzH26t";
	// use for twitter
	public static final String TWITTER_CONSUMER_KEY = "yHNVo2Pv2TX9Udbzkj7QKQ";
	public static final String TWITTER_CONSUMER_SECRET = "Ri1WtxxFPu4EIycT0D5EgkxQPWCozljivhjslJgA";

	public static final String TWITTER_CALLBACK_URL = "twitterapp://com.ceosoftcenters.thoughtfortheday";

	public static final String URL_PARAMETER_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	public static final String PREFERENCE_TWITTER_OAUTH_TOKEN="TWITTER_OAUTH_TOKEN";
	public static final String PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET="TWITTER_OAUTH_TOKEN_SECRET";
	public static final String PREFERENCE_TWITTER_IS_LOGGED_IN="TWITTER_IS_LOGGED_IN";
	public static final String TWITTER_MESSAGE = "I found Edgar Cayce's ThoughtForTheDay app to be a good reference guide to my dreams. Google Market: ";
	
	public static final String SP_REGION_CODE = "sp_region_code";
	public static final String REGION_CODE = "region_code";
	
	public static void saveRegionCode(Context context, String code){
		SharedPreferences sp = context.getSharedPreferences(SP_REGION_CODE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(REGION_CODE, code);
		editor.commit();
		
	}
	
	public static String getRegionCode(Context context){
		SharedPreferences sp = context.getSharedPreferences(SP_REGION_CODE, Context.MODE_PRIVATE);
		String code = sp.getString(REGION_CODE, "");
		return code;
	}
}
