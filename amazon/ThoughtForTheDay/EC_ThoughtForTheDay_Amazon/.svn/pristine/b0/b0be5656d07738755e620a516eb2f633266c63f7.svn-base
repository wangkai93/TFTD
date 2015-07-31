/*
 *
 * LockLisnter.java created at Sep 15, 2011 2:33:41 PM
 *
 *
 *  Copyright (C) 2009-2011 Suzhou CEO Softcenters Co. Ltd.
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
*
* @file SceenStateChangeListener.java
* @author Emily.Zhou
* @date Feb 6, 2012
*/

public class SceenStateChangeListener extends BroadcastReceiver {
	private Context mContext;
	public SceenStateChangeListener(Context context){
		this.mContext = context;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (Intent.ACTION_SCREEN_OFF.equals(action)) {			
			Intent i = new Intent();
			i.setAction("com.emily.off");
			mContext.sendBroadcast(i);
		}
		
		if (Intent.ACTION_SCREEN_ON.equals(action)) {
			Intent i = new Intent();
			i.setAction("com.emily.on");
			mContext.sendBroadcast(i);
		}
	}
	
}
