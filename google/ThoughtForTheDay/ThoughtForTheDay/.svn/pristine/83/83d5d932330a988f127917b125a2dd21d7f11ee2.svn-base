/*
 *
 * IntentHelperUtil.java created at Aug 30, 2011 9:56:02 AM
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

import android.content.Context;
import android.content.Intent;

/**
 *
 * @file IntentHelperUtil.java
 * @author Samuel.Cai
 * @date Aug 30, 2011
 */
public class IntentHelperUtil {
	private Intent intent;
	private static IntentHelperUtil intentHelper;
	private IntentHelperUtil(){
		intent = new Intent();
	}
	
	public static IntentHelperUtil getInstance(){
		if(intentHelper == null){
			intentHelper = new IntentHelperUtil();
		}
		return intentHelper;
	}
	
	public Intent getIntent(Context packageContext, Class<?> cls){
		intent.setClass(packageContext, cls);
		return intent;
	}
	
	public Intent getIntentClearPreActivity(Context packageContext, Class<?> cls){
		Intent intent = new Intent(); 
		intent.setClass(packageContext, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		return intent;
	}
	
	public Intent getNewIntent(Context packageContext, Class<?> cls){
		Intent intentNew = new Intent();
		intentNew.setClass(packageContext, cls);
		return intentNew;
	}
}
