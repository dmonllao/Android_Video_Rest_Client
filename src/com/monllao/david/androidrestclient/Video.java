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
	private int timecreated;
	
	public Video() {
		
	}
	
	public Video(int id, int userid, String name, int timecreated) {
		super();
		this.id = id;
		this.userid = userid;
		this.name = name;
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
	public int getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(int timecreated) {
		this.timecreated = timecreated;
	}

}
