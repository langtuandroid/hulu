package com.beecas.service;

import java.util.Collection;
import java.util.List;

import com.smartfoxserver.v2.buddylist.BuddyList;
import com.smartfoxserver.v2.buddylist.BuddyVariable;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public interface IBuddyService {
    BuddyList loadBuddyList(String owner);

    void saveBuddyList(BuddyList buddyList);

    boolean initBuddyList(String owner, Collection<String> buddyFb, ISFSObject userInfo, User ownerUser);

    int addFriend(String adding, String added, String groupName);

    boolean checkBuddyInited(String owner);

    List<BuddyVariable> getOfflineVariables(String buddyName);

    ISFSObject getBuddyVariables(String buddyName);
}
