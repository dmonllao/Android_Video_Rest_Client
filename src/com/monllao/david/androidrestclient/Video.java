package com.monllao.david.androidrestclient;

import java.io.Serializable;

/**
 * POJO of a video
 */
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

	private int id;
	private int userid;
	private String name;
	private String url;
	private String html;
	private int timecreated;
	
	public Video() {
		
	}
	
	public Video(int id, int userid, String name, String url, String html, int timecreated) {
		super();
		this.id = id;
		this.userid = userid;
		this.name = name;
		this.url = url;
		this.html = html;
		this.timecreated = timecreated;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	public int getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(int timecreated) {
		this.timecreated = timecreated;
	}

}
