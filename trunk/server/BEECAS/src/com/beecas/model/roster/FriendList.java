package com.beecas.model.roster;

import java.util.ArrayList;
import java.util.List;

public class FriendList {
	private String username;
	private long version;
	private List<Item> friends;
	private List<String> requests;
			
	public FriendList() {
		friends = new ArrayList<Item>();
		requests = new ArrayList<String>();
	}

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public List<Item> getFriends() {
		return friends;
	}

	public void setFriends(List<Item> friends) {
		this.friends = friends;
	}	
	
}
