package com.beecas.buddylist.storage;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Common;
import com.beecas.constants.UserConstants;
import com.beecas.service.IBuddyService;
import com.smartfoxserver.v2.buddylist.BuddyList;
import com.smartfoxserver.v2.buddylist.BuddyListManager;
import com.smartfoxserver.v2.buddylist.BuddyVariable;
import com.smartfoxserver.v2.buddylist.storage.BuddyStorage;
import com.smartfoxserver.v2.exceptions.SFSBuddyListNotFoundException;

public class BeecasBuddyStorage implements BuddyStorage {
    private final Logger log;

    private BuddyListManager buddyListManager;

    public BeecasBuddyStorage() {
        log = LoggerFactory.getLogger(UserConstants.BEECAS_LOG);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public BuddyListManager getBuddyListManager() {
        return buddyListManager;
    }

    @Override
    public List<BuddyVariable> getOfflineVariables(String buddyName) throws IOException {
        System.out.println("getOfflineVariables: " + buddyName);
        try {
            IBuddyService buddyService = BeecasServiceManager.getService(IBuddyService.class);
            List<BuddyVariable> result = buddyService.getOfflineVariables(buddyName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public BuddyList loadList(String ownerName) throws SFSBuddyListNotFoundException, IOException {
        IBuddyService buddyService = BeecasServiceManager.getService(IBuddyService.class);
        BuddyList buddyList = buddyService.loadBuddyList(ownerName);
        if (buddyList == null) {
            throw new SFSBuddyListNotFoundException("BuddyList not found for: " + ownerName);
        }
        return buddyList;
    }

    @Override
    public void saveList(BuddyList buddyList) throws IOException {
        IBuddyService buddyService = BeecasServiceManager.getService(IBuddyService.class);
        buddyService.saveBuddyList(buddyList);
    }

    @Override
    public void setBuddyListManager(BuddyListManager manager) {
        if (this.buddyListManager != null) {
            throw new IllegalStateException("Can't re-assign buddyListManager.");
        }
        buddyListManager = manager;
    }

}
