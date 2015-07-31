/*
 *
 * EventModel.java created at 2012-2-6 ÏÂÎç05:17:10
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
package com.ceosoftcenters.thoughtfortheday.service;

import com.ceosoftcenters.thoughtfortheday.util.SceenStateChangeListener;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
*
* @file ListenScreenStateChangeService.java
* @author Emily.Zhou
* @date Feb 6, 2012
*/

public class ListenScreenStateChangeService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void registerIntentReceivers() 
	{ 
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF); 
		filter.addAction(Intent.ACTION_SCREEN_ON);
		SceenStateChangeListener receiver = new SceenStateChangeListener(this.getApplicationContext());  
		registerReceiver(receiver, filter); 
	} 
	
	public void onCreate(){
		registerIntentReceivers();
	}

	public void onDestroy() {
		super.onDestroy();
	}

}
