package com.beecas.model.roster;

import java.util.Hashtable;

public class Blacklist {
	private String username;
	private Hashtable<String, Boolean> blacklist;
 
	public Blacklist() {
	    blacklist = new Hashtable<String, Boolean>();
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Hashtable<String, Boolean> getBlacklist() {
		return blacklist;
	}

	public boolean addBlacklist(String username) {
	    if(!blacklist.contains(username)) {
	        blacklist.put(username, true);
	        return true;
	    }
	    return false;
	}
	
	public void RemoveBlacklist(String username) {
	    blacklist.remove(username);
	}

}
