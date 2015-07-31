/*
 *
 * ChartGallery.java created at 2012-2-9 ����02:29:31
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
package com.ceosoftcenters.thoughtfortheday.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;


/**
 *
 * @file ChartGallery.java
 * @author Emily.Zhou
 * @date 2012-2-9
 */
public class ThoughtsGallery extends Gallery {

	public ThoughtsGallery(Context context, AttributeSet attrSet) {
		// TODO Auto-generated constructor stub
		super(context, attrSet);
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() < e1.getX();
	}

	private boolean isScrollingRight(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int position = getSelectedItemPosition();
		boolean leftScroll = isScrollingLeft(e1, e2);
		boolean rightScroll = isScrollingRight(e1, e2);

		if (rightScroll) {
			if (getSelectedItemPosition() != 0){
				setSelection(position,true);
			}

		} else if (leftScroll) {
			if (getSelectedItemPosition() != getCount() - 1){
				setSelection(position,true);
			}
		}
		return true;
	}

	/* http://blog.csdn.net/spt110/article/details/7919870
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	
}
