package com.beecas.model.roster;

import java.io.Serializable;

public class Presentity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private long lastUpdate;
	private Show show;
	private String blast;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public String getBlast() {
		return blast;
	}

	public void setBlast(String blast) {
		this.blast = blast;
	}

}
