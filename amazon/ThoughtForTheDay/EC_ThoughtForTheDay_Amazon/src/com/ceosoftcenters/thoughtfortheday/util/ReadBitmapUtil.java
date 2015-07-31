/*
 *
 * ReadBitmapUtil.java created at Sep 27, 2011 11:13:25 AM
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

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * use this class to readBitMap by res_id, To avoid memory overflow
 * @file ReadBitmapUtil.java
 * @author Samuel.Cai
 * @date Sep 27, 2011
 */
public class ReadBitmapUtil {
	/**
     * read bitmap by the least low price
     * @param context
     * @param resId
     * @return
     */  
    public static Bitmap readBitMap(Context context, int resId){  
       BitmapFactory.Options opt = new BitmapFactory.Options();  
       opt.inPreferredConfig = Bitmap.Config.RGB_565;   
       opt.inPurgeable = true;  
       opt.inInputShareable = true;  
      // opt.inJustDecodeBounds = false;
       //get resource image
       InputStream is = context.getResources().openRawResource(resId);  
       return BitmapFactory.decodeStream(is,null,opt);  
   }

}
