package com.ceosoftcenters.thoughtfortheday.facebook;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 *
 * @file PostFacebookItem.java
 * @author Samuel.Cai
 * @date Mar 1, 2012
 */
public class PostFacebookItem {
	
	private String title;
	private String url;
	private String body;
	
	private String icon_src ;

	public PostFacebookItem() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 *
	 * @param title
	 * @param url
	 * @param body
	 * @param icon_src
	 */
	public PostFacebookItem(String title, String url, String body,
			String icon_src) {
		super();
		this.title = title;
		this.url = url;
		this.body = body;
		this.icon_src = icon_src;
	}



	public JSONObject toJSONObject(){
		JSONObject obj = new JSONObject();
		try {
			obj.put("title", title);
			obj.put("url", url);
			obj.put("summary", body);
			obj.put("icon_src",icon_src);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	public String getIcon_src() {
		return icon_src;
	}

	public void setIcon_src(String icon_src) {
		this.icon_src = icon_src;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}
}
