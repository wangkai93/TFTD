/*
 *
 * FavoriteListBaseAdapter.java created at 2012-2-17 ÏÂÎç12:52:09
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
package com.ceosoftcenters.thoughtfortheday.adapter;
import java.util.HashMap;
import java.util.List;
import com.ceosoftcenters.thoughtfortheday.R;
import com.ceosoftcenters.thoughtfortheday.application.TFTDApplication;
import com.ceosoftcenters.thoughtfortheday.sqlite.ThoughtsInfoOperater;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * @file FavoriteListBaseAdapter.java
 * @author Emily.Zhou
 * @date 2012-2-17
 */
public class FavoriteListBaseAdapter extends android.widget.BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
    private int mViewResourceId;
    private int[] mTo;
    private List<HashMap<String,String>> favoriteListData;
    private int flagPos = 0;
    private ThoughtsInfoOperater opertor;
    @SuppressWarnings("unchecked")
	public FavoriteListBaseAdapter(Context context,int viewRourcesID,Object obj,int[] to){
		this.context = context;
    	this.mInflater = LayoutInflater.from(context);
    	this.mViewResourceId = viewRourcesID;
    	this.mTo = to;
    	this.favoriteListData = (List<HashMap<String,String>>) obj;    	
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(favoriteListData==null)
			return 0;
		return favoriteListData.size();
	}
    public void setDate(List<HashMap<String,String>> favoriteListData){
    	this.favoriteListData = favoriteListData;
    }
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		opertor = new ThoughtsInfoOperater(context);
		final ViewHolder holder ;
		if(convertView == null){
			convertView = mInflater.inflate(mViewResourceId, null);
			holder = new ViewHolder();
			holder.leftBt = (Button)convertView.findViewById(mTo[0]);
			holder.title = (TextView)convertView.findViewById(mTo[1]);
			holder.body = (TextView)convertView.findViewById(mTo[2]);
			holder.id = (TextView)convertView.findViewById(mTo[3]);
			holder.rightBt = (Button)convertView.findViewById(mTo[4]);
			convertView.setTag(holder);			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}		
		holder.leftBt.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(holder.id.getText().equals("bt1")){
					holder.rightBt.setVisibility(View.VISIBLE);
					holder.leftBt.setBackgroundResource(R.drawable.bt2);	
					holder.id.setText("bt2");					
				}else{
					holder.rightBt.setVisibility(View.GONE);
					holder.leftBt.setBackgroundResource(R.drawable.bt1);
					holder.id.setText("bt1");
				}	
				
				if(flagPos==position){						
				}else{
					View vv =(View)parent.getChildAt(flagPos);
					Button bv = (Button)(vv.findViewById(R.id.bt));
					Button right = (Button)(vv.findViewById(R.id.del));
					TextView tv = (TextView)(vv.findViewById(R.id.favoriteId));
					if(tv.getText().equals("bt1")){
					}else{
						bv.setBackgroundResource(R.drawable.bt1);
						right.setVisibility(View.GONE);
						tv.setText("bt1");
					}						
				}
				flagPos=position;																							
			}
		});
		holder.rightBt.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				HashMap<String,String> temp = favoriteListData.get(position);
				String id =  temp.get("id");
                opertor.delete(Integer.parseInt(id));		              
				favoriteListData.remove(position);
				notifyDataSetChanged();
				flagPos=0;
				
			}
		});	
		
		holder.leftBt.setBackgroundResource(R.drawable.bt1);
		holder.title.setText(Html.fromHtml(""+this.favoriteListData.get(position).get("title")));
		holder.title.setTypeface(TFTDApplication.boldFont);
		
		String text = ""+this.favoriteListData.get(position).get("context");
//		String p = text.replaceAll("<p>", "").replaceAll("&nbsp;</p>", "").replaceAll("&", "").replaceAll("</p>", "");
//		System.out.println(">>>>> text = " + text);
		holder.body.setText(Html.fromHtml(text));
//		holder.body.setText(p);
		holder.body.setTypeface(TFTDApplication.normalFont);
		holder.body.setTextColor(Color.parseColor("#465380"));	
		
		holder.id.setText("bt1");
		holder.rightBt.setVisibility(View.GONE);
		
		return convertView;
	}
	private  class ViewHolder {
		Button leftBt;
		TextView title;
		TextView body;
		TextView id;
		Button rightBt;		
    }
}
