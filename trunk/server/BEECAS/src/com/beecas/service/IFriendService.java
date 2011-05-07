package com.beecas.service;

import java.util.Collection;

import com.beecas.model.roster.FriendList;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public interface IFriendService {
	FriendList getFriendList(String username);
	long addFriend(String addingUser, String addedUser, String groupName);//both adding-> added : to; added -> adding : from
	long accept(String username, String addingUser, String groupName);//both adding-> added : both ; added -> adding : both
	long deny(String username, String addingUser);//both adding-> added : none ; added -> adding : none
	long removeFriend(String user, String removedUser);//direct adding -> added : remove
	long changeGroup(String user, String changedUser, String newGroup);//direct
	long addBlacklist(String user, String blackedUser);//direct adding -> added : ignore
	long removeBlacklist(String user, String blackedUser);//direct adding -> added : remove
	FriendList initBuddyList(String owner, Collection<String> buddyFb, User ownerUser);
}
