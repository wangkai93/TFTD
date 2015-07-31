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
package com.ceosoftcenters.thoughtfortheday.util;

import com.ceosoftcenters.thoughtfortheday.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.WindowManager;

/**
*
* @file DialogBuilderUtil.java
* @author Emily.Zhou
* @date Feb 6, 2012
*/

public class DialogBuilderUtil {
	/**
	 * 
	 * Use it when the app should open the web.
	 */
	public static void showOpenWebsiteDialog(final Context context,final String url) {
		new AlertDialog.Builder(context)
		.setTitle(context.getText(R.string.open_website))
		.setMessage(url)
		.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}})
		.setPositiveButton(context.getResources().getString(R.string.open),new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://"+url)));
			}}).show();
	}
	
	/**
	 * 
	 * Show the dialog when the network doesn't work or has some other error.
	 */
	public static void showErrorDialog(final Context context,final String msg) {
		new AlertDialog.Builder(context)
		.setTitle("Error")
		.setMessage(msg)
		.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}}).show();
	}
	
	private static ProgressDialog progressDialog = null;

	/**
	 * 
	 *Show the dialog only has waiting(used for wait for loading)
	 */
	public static void showProgressDialog(Context context){
		dismissProgressDialog();
		progressDialog = new ProgressDialog(context);
		String loadingStr = context.getResources().getString(R.string.loading);
		String waitStr = context.getResources().getString(R.string.wait);
		progressDialog.setTitle(loadingStr);
		progressDialog.setMessage(waitStr);
		//progressDialog.setCancelable(false);
		try{
			progressDialog.show();
		}catch(WindowManager.BadTokenException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Show the progress dialog.
	 */
	public static void showProgressDialog(Context context,String title,String message){
		dismissProgressDialog();
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		//progressDialog.setCancelable(false);
		try{
			progressDialog.show();
		}catch(WindowManager.BadTokenException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Show the progress dialog whose title and message from app resource.
	 */
	public static void showProgressDialog(Context context,int title,int message){
		dismissProgressDialog();
		progressDialog = new ProgressDialog(context);
		String titleStr = context.getResources().getString(title);
		String messageStr = context.getResources().getString(message);
		progressDialog.setTitle(titleStr);
		progressDialog.setMessage(messageStr);
		//progressDialog.setCancelable(false);
		
		try{
			progressDialog.show();
		}catch(WindowManager.BadTokenException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * dismiss the dialog.
	 * 
	 */
	public static void dismissProgressDialog(){
		if(progressDialog != null){
			try{
				progressDialog.dismiss();
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
	}
}
