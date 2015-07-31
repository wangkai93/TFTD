/*
 *
 * InfoActivity.java created at Jan 31, 2012 10:50:18 AM
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
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;
import com.ceosoftcenters.thoughtfortheday.util.ConstantUtil;
import com.ceosoftcenters.thoughtfortheday.util.ReadBitmapUtil;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 *
 * @file InfoActivity.java
 * @author Emily.Zhou
 * @date Jan 31, 2012
 */

public class InfoActivity extends BaseActivity {
	private SharedPreferences settings;
	private SeekBar musicControl;;
	private ImageView musicClose;
	public AudioManager audioManager = null;
	private TFTDApplication tftdApp;
	private RelativeLayout infoRL ;
	private boolean haveMusic = false;
	protected static final int PROGRESS_CHANGED = 0x101;
	
	private TextView copyrightLine1,copyrightLine2;
	
	private ImageView copyRightImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.info);

		tftdApp = (TFTDApplication)getApplication();

		getWidgetInstance();
		
		setMusicController();
		new Thread(new myVolThread()).start();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if(haveMusic){
			tftdApp.startBgMusic();
		}
	}

	private void setMusicController(){
		haveMusic = tftdApp.isHaveMusic();
		audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		musicControl.setMax(maxVolume);
		int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		musicControl.setProgress(currentVolume);
		tftdApp.setMusicVolume(currentVolume);
		if(haveMusic){
			musicClose.setImageResource(R.drawable.switch_on);
			musicClose.setTag("musicOn");
		}else{
			musicClose.setImageResource(R.drawable.switch_off);
			musicClose.setTag("musicOff");
		}
		musicControl.setProgress(tftdApp.getMusicVolume());
		musicControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				tftdApp.setMusicVolume(progress);
				tftdApp.addJustMusicVolume(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}});
	}

	public void getWidgetInstance(){
		infoRL = (RelativeLayout)findViewById(R.id.info);
		infoRL.setBackgroundDrawable(new BitmapDrawable(ReadBitmapUtil.readBitMap(this, R.drawable.info_bg)));
		
		copyrightLine1 = (TextView)findViewById(R.id.copyrightLine1);
		copyrightLine2 = (TextView)findViewById(R.id.copyrightLine2);
		copyrightLine1.setTypeface(tftdApp.normalFont);
		copyrightLine2.setTypeface(tftdApp.normalFont);
		
		musicClose = (ImageView)findViewById(R.id.soundClose);
		musicClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String temp = musicClose.getTag().toString();
				if("musicOn".equals(temp)){
					musicClose.setImageResource(R.drawable.switch_off);
					musicClose.setTag("musicOff");
					tftdApp.pauseOrStopBGMusic();
					haveMusic = false;
				}else{
					musicClose.setImageResource(R.drawable.switch_on);
					musicClose.setTag("musicOn");
					tftdApp.startBgMusic();
					haveMusic=true;	
				}				
				tftdApp.saveMusicSettings(haveMusic);
			}
		});
		settings = getSharedPreferences(ConstantUtil.SETTINGS, 0);
		musicControl=(SeekBar)findViewById(R.id.sound);	
		
		copyRightImage = (ImageView)findViewById(R.id.copyRightImage);
		copyRightImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = "http://www.edgarcayce.org/";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent .setData(Uri.parse(url));
				startActivity(intent);
			}});
	}
	
	Thread myVolThread = null;
    Handler myHandler = new Handler(){
    	public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case PROGRESS_CHANGED:  
            	musicControl.setProgress(tftdApp.getMusicVolume());
                break;
           }
        }
    };
    
    class myVolThread implements Runnable {  
        public void run() { 
             while (!Thread.currentThread().isInterrupted()) {   
                      
                  Message message = new Message();  
                  message.what = PROGRESS_CHANGED;  
                  myHandler.sendMessage(message);  
                  try {  
                       Thread.sleep(100);   
                  } catch (InterruptedException e) {  
                       Thread.currentThread().interrupt();  
                  }  
             }  
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
