/*
 *
 * TFTDApplication.java created at Feb 1, 2012 2:41:00 PM
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
package com.ceosoftcenters.thoughtfortheday.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.R.integer;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.model.pojo.Event;
import com.ceosoftcenters.thoughtfortheday.service.ListenScreenStateChangeService;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsForTheDayOperater;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsInfoOperater;
import com.ceosoftcenters.thoughtfortheday.util.ConstantUtil;
import com.ceosoftcenters.thoughtfortheday.util.IntentHelperUtil;

/**
 *
 * @file TFTDApplication.java
 * @author Emily.Zhou
 * @date Feb 6, 2012
 */

public class TFTDApplication extends Application {
	private boolean needReloadDataFromBackend;


	public boolean isNeedReloadDataFromBackend() {
		return needReloadDataFromBackend;
	}

	public void setNeedReloadDataFromBackend(boolean needReloadDataFromBackend) {
		this.needReloadDataFromBackend = needReloadDataFromBackend;
	}

	private boolean isMdpiTablet;

	public boolean isMdpiTablet() {
		return isMdpiTablet;
	}

	public void setMdpiTablet(boolean isMdpiTablet) {
		this.isMdpiTablet = isMdpiTablet;
	}

	private MediaPlayer pageTurnMedia,backgroundMedia;
	private int currentPositionOfBGMedia;
	//private boolean needMonitorPowerButton = false;
	//private StatusOfSceenChangedReceiver stateOfSceenChangeReceiver;

	public static int[] bolFavourite = {0,0,0,0,0,0,0};
	private ArrayList<Event> thoughtsGalleryDataListSet;   //read data to background		
	private int selectedPositionOfGallery = -1;// -1 means the first enter into thoughtsActivity

	public static Typeface normalFont;
	public static Typeface boldFont;

	public  int[] imageIDs = {R.drawable.message_1,R.drawable.message_2,
			R.drawable.message_3,R.drawable.message_4,
			R.drawable.message_5,R.drawable.message_6,
			R.drawable.message_7};

	public  int[] getImageIDs() {
		return imageIDs;
	}

	public  void setImageIDs(int[] imageIDs) {
		this.imageIDs = imageIDs;
	}

	private int musicVolume = 0;
	private boolean haveMusic = false;
	private SharedPreferences sp;

	private AudioManager audioManager=null;

	public static int position;

	public ArrayList<Event> getGalleryDataSet() {
		return thoughtsGalleryDataListSet;
	}

	public void setGalleryDataSet(ArrayList<Event> list) {
		this.thoughtsGalleryDataListSet = list;
	}

	public int getSelectedPositionOfGallery() {
		return selectedPositionOfGallery;
	}

	public void setSelectedPositionOfGallery(int selectedPositionOfGallery) {
		this.selectedPositionOfGallery = selectedPositionOfGallery;
	}

	public boolean isHaveMusic() {
		return haveMusic;
	}

	public void setHaveMusic(boolean haveMusic) {
		this.haveMusic = haveMusic;
	}

	public int getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
	}

	public void getThoughtsImagesBackground(){
		if(sp == null){
			sp=getSharedPreferences(ConstantUtil.SETTINGS, 0);
		}
		if(sp != null){
			String today = sp.getString(ConstantUtil.TODAY, "");
			String todayOrder = sp.getString(ConstantUtil.TODAYORDER, "");	

			String arrayImage="";
			String data=getYesterdaysStr(0);

			if("".equals(today) || !today.equals(data)){
				getRandomOrder();
				int size = imageIDs.length;
				for (int i = 0; i < size; i ++){
					if (i == size-1){
						arrayImage+=imageIDs[i];
					}else{
						arrayImage+=imageIDs[i]+":"; 
					}			   
				}
				sp.edit().putString(ConstantUtil.TODAYORDER, arrayImage).putString(ConstantUtil.TODAY, data).commit();
			}else{
				String[] tt = todayOrder.split(":");
				int size = tt.length;
				for (int i = 0; i < size; i ++){
					imageIDs[i] = Integer.parseInt(tt[i]);
				}
			}
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sp=getSharedPreferences(ConstantUtil.SETTINGS, 0);
		haveMusic =sp.getBoolean(ConstantUtil.SOUND_SETTING, false);
		audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);
		addJustMusicVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

		prepareMusic();

		// monitor 'Home button'
//		new Thread(new Runnable(){
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				Process mLogcatProc = null;
//				BufferedReader reader = null;
//				try {
//					//get logcat information
//					mLogcatProc = Runtime.getRuntime().exec(new String[] { "logcat","ActivityManager:D *:S" });
//					reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));
//					String line;
//					while ((line = reader.readLine()) != null) {
//						if(line.contains("Intent { act=android.intent.action.MAIN cat=[android.intent.category.HOME]") && line.contains("Starting")){
//							needMonitorPowerButton = false;
//							pauseOrStopBGMusic();
//						}else if(line.contains("Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER]") && line.contains("Starting")){
//							if(line.contains("com.ceosoftcenters.thoughtfortheday/.activity.MyTabActivity")){
//								needMonitorPowerButton = true;
//								needReloadDataFromBackend = true;
//								if(haveMusic==true){
//									startBgMusic();
//								}
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}}).start();
//
//		// use new method to monitor the power button click event
//		startService(IntentHelperUtil.getInstance().getIntent(this, ListenScreenStateChangeService.class));
//		stateOfSceenChangeReceiver = new StatusOfSceenChangedReceiver();
//		registerStatusOfSceenChangedReceiver();

		copyDBFileToDatabaseDir();

		thoughtsTableOperator = new ThoughtsForTheDayOperater(this);

		normalFont = Typeface.createFromAsset(getAssets(), "fonts/Helvetica CE Regular.ttf");
		boldFont = Typeface.createFromAsset(getAssets(), "fonts/Helvetica CE Bold.ttf");

		getThoughtsImagesBackground();
	}

	/**
	 * 
	 * @return void
	 * @author Emily.Zhou
	 * @date 2012-3-20
	 */
	private int[] getRandomOrder() {
		// TODO Auto-generated method stub
		Random rdm = new Random();
		for (int j = 0; j < 7; j++)
		{
			int pos = rdm.nextInt(7);
			int temp = imageIDs[pos];
			imageIDs[pos] = imageIDs[j];
			imageIDs[j] = temp;
		}
		return imageIDs;
	}

	/**
	 * @return 
	 * @return boolean
	 * @author Samuel.Cai
	 * @date Jun 1, 2012
	 */
	public boolean checkSoundON(){
		sp=getSharedPreferences(ConstantUtil.SETTINGS, 0);
		haveMusic =sp.getBoolean(ConstantUtil.SOUND_SETTING, false);
		return haveMusic;
	}
	/**
	 * 
	 * start background media
	 */
	public void startBgMusic(){
		if(backgroundMedia != null){
			if(!backgroundMedia.isPlaying()){
				backgroundMedia.start();
			}
		}else{
			backgroundMedia  = MediaPlayer.create(this, R.raw.bg_music);
			backgroundMedia.start();
		}		
	}

	public MediaPlayer getBackgroundMedia() {
		return backgroundMedia;
	}

	public void setBackgroundMedia(MediaPlayer backgroundMedia) {
		this.backgroundMedia = backgroundMedia;
	}

	public void addJustMusicVolume(int volume){
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.MODE_NORMAL);
	}
	public void saveMusicSettings(boolean haveMusic){
		this.haveMusic = haveMusic;
		sp.edit().putBoolean(ConstantUtil.SOUND_SETTING, haveMusic).commit();
	}

	public void reSaveMusicVolume(){
		int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		this.musicVolume = currentVolume;
	}

	private void prepareMusic(){
		if(haveMusic==true){
			pageTurnMedia = MediaPlayer.create(this, R.raw.bg_music);		
			startBgMusic();
		}
	}

//	public class StatusOfSceenChangedReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if("com.emily.off".equals(intent.getAction()) && needMonitorPowerButton){
//				pauseOrStopBGMusic();
//			}
//			if("com.emily.on".equals(intent.getAction()) && needMonitorPowerButton){
//				if(haveMusic==true){
//					startBgMusic();
//				}
//			}
//		}
//	}

	public void pauseOrStopBGMusic(){
		if(backgroundMedia != null){
			currentPositionOfBGMedia = backgroundMedia.getCurrentPosition();
			backgroundMedia.pause();
		}
	}

//	public void registerStatusOfSceenChangedReceiver(){
//		IntentFilter filter = new IntentFilter("com.emily.off"); 
//		filter.addAction("com.emily.on"); 
//		registerReceiver(stateOfSceenChangeReceiver, filter); 
//	}
//
//	public void UnregisterStatusOfSceenChangedReceiver(){
//		if(stateOfSceenChangeReceiver != null){
//			try{
//				this.unregisterReceiver(stateOfSceenChangeReceiver);
//			}catch(IllegalArgumentException e){
//			}
//		}
//	}
//
//	public void stopListenScreenStateChangeService(){
//		this.stopService(IntentHelperUtil.getInstance().getIntent(this, ListenScreenStateChangeService.class));
//		UnregisterStatusOfSceenChangedReceiver();
//	}

	/**
	 * copay the db file to the direction of 'data/data/databases'
	 */
	public void copyDBFileToDatabaseDir(){
		File dbFileDir = this.getDatabasePath(ConstantUtil.DATABASE_NAME);
		//		System.out.println("DDP:  dbFileDir = " + dbFileDir.getAbsolutePath() );
		//File dbFileDir = new File( appFilesDir.getAbsolutePath() + ConstantUtil.DATABASE_NAME);
		// you must create directory for db_flie before you invoke dbFileDir.createNewFile();
		String path = dbFileDir.getAbsolutePath();
		int lastIndex = path.lastIndexOf("/");

		File dirFile = new File(path.substring(0, lastIndex));
		dirFile.mkdirs();

		if(dbFileDir.exists() && dbFileDir.isFile()){
			//			System.out.println("^^^^^^^^ db file already exist in app file dir !"); 
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(ConstantUtil.DATABASE_NAME), null); 
			int oldVersion = database.getVersion();
			if(ConstantUtil.DATABASE_VERSON > oldVersion){
				System.out.println("DDF: you have update the version of database manually.  oldVersion = " + oldVersion + " now version = " + ConstantUtil.DATABASE_VERSON);
				
				// you should get oldData from db
				ThoughtsInfoOperater operater = new ThoughtsInfoOperater(this);
				ArrayList<Event> oldFavorites = operater.getAllFavorites();
				operater.close();
				
				overRideDBfile(dbFileDir);
				
				operater = new ThoughtsInfoOperater(this);
				operater.insertFavorite(oldFavorites);
				operater.close();
			}
			
			database.close();
		}else{
			// create this file , read stream from draw dir to app file
			try{
				if(dbFileDir.createNewFile()){
					overRideDBfile(dbFileDir);
				}
			}catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param dbFileDir
	 * @return void
	 * @author Samuel.Cai
	 * @date Apr 9, 2012
	 */
	private void overRideDBfile(File dbFileDir){
		try{
			InputStream is = getAssets().open(ConstantUtil.DATABASE_FILE_NAME);
			FileOutputStream fos = new FileOutputStream(dbFileDir);
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *
	 * @return 
	 * @return boolean
	 * @author samuel.cai
	 * @date Dec 16, 2011
	 */
	public boolean haveAvaliableNetWork(){
		return (isWiFiActive() || isNetworkAvailable());
	}

	/**
	 * 
	 * determine if the wifi is working.
	 */
	public  boolean isWiFiActive() {
		WifiManager mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
			return true;
		} else {
			return false;   
		}
	}

	/**
	 * 
	 * determine if the network is working.
	 */
	public  boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if(info == null){
				return false;
			}else{
				if(info.isAvailable()){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return void
	 * @author Samuel.Cai
	 * @date Mar 9, 2012
	 */
	public void setThoughtsDataForGallery(boolean haveInternet) {
//      2012.04.25 emily       ArrayIndexOutOfBoundsException
//		if(thoughtsGalleryDataListSet == null ){
		    thoughtsGalleryDataListSet = new ArrayList<Event>(7);
//		}else{
//		}
		thoughtsGalleryDataListSet.clear();

		Event temp = null;
		for(int i = 0; i < 7; i ++){
			temp = thoughtsTableOperator.queryByCreateDateStr(getYesterdaysStr(i)+"%%");
			if(temp == null){
				temp = thoughtsTableOperator.query(7-i);
			}
			thoughtsGalleryDataListSet.add(temp);
		}
		setBooleanFavorite();
	}

	private String createDateStrPrefix = null;
	private ThoughtsForTheDayOperater thoughtsTableOperator = null;
	private ThoughtsInfoOperater favoriteTableOperator = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Calendar calendar = Calendar.getInstance();

	public String getYesterdaysStr(int step){
		calendar.setTime(new Date());  
		calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - step); 
		createDateStrPrefix = dateFormat.format(calendar.getTime());
		return createDateStrPrefix ;
	}

	public void  setBooleanFavorite(){ // favoriteTableOperator would be null, so...
		if(thoughtsGalleryDataListSet != null){
			favoriteTableOperator = new ThoughtsInfoOperater(this);
			int size = thoughtsGalleryDataListSet.size();
			if(thoughtsGalleryDataListSet != null &&  size > 0){
				for(int i = 0 ; i < size ; i ++){
					if(favoriteTableOperator.alreadyExists(thoughtsGalleryDataListSet.get(i).getCreateDateStr())){
						bolFavourite[i] = 1;
					}else{
						bolFavourite[i] = 0;
					}
				}
			}
		}
	}

}
