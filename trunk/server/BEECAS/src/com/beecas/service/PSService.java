package com.beecas.service;

import java.util.List;

import com.beecas.model.roster.Presentity;
import com.beecas.model.roster.Show;
import com.google.inject.Inject;

public class PSService implements IPSService {

    private static final String PRESENTITY_PREFIX = "Presentity:";

    private static final int CACHE_TIME = 7 * 24 * 3600;

    private final ICacheService cacheService;

    private final ISubscriberService subscriberService;
    
    @Inject
    private PSService(ICacheService cacheService, ISubscriberService subscriberService) {
        this.cacheService = cacheService;
        this.subscriberService = subscriberService;
    }

    @Override
    public void delete(String username) {
        cacheService.getCache().delete(PRESENTITY_PREFIX + username);
    }

    @Override
    public Presentity fetch(String username) {
        return (Presentity) cacheService.getCache().get(PRESENTITY_PREFIX + username);
    }

    @Override
    public String getBlast(String username) {
        Presentity presentity = (Presentity) cacheService.getCache().get(PRESENTITY_PREFIX + username);
        if (presentity != null) {
            return presentity.getBlast();
        }
        return null;
    }

    @Override
    public Show getShow(String username) {
        Presentity presentity = (Presentity) cacheService.getCache().get(PRESENTITY_PREFIX + username);
        if (presentity != null) {
            return presentity.getShow();
        }
        return Show.Offline;
    }

    @Override
    public List<String> publishDisconnect(String username) {
        Show currentShow = getShow(username);
        cacheService.getCache().delete(PRESENTITY_PREFIX + username);
        if (Show.Online.equals(currentShow)) {
            List<String> subscribers = subscriberService.getSubscribers(username);
            if (subscribers != null) {
                //				ChangeStatusBean statusBean = new ChangeStatusBean();
                //				statusBean.setFriendName(username);
                //				statusBean.setStatus(Show.Offline);
                //				Message statusMsg = new Message(NetworkConstant.CHANGE_STATUS_BEAN, statusBean);
                //				communicatorService.sendMessage(subscribers, statusMsg);
                return subscribers;
            }
        }
        return null;
    }

    @Override
    public List<String> publishOnline(Presentity presentity) {
        String username = presentity.getUserName();
        cacheService.getCache().set(PRESENTITY_PREFIX + username, CACHE_TIME, presentity);
        if (presentity.getShow().equals(Show.Online)) {
            List<String> subscribers = subscriberService.getSubscribers(username);
            if (subscribers != null) {
                //				ChangeStatusBean statusBean = new ChangeStatusBean();
                //				statusBean.setFriendName(username);
                //				if (presentity.getShow() != Show.Invisible) { 
                //					statusBean.setStatus(presentity.getShow());
                //					Message statusMsg = new Message(NetworkConstant.CHANGE_STATUS_BEAN, statusBean);
                //					ChangeBlastBean blastBean = new ChangeBlastBean();
                //					blastBean.setFriend(username);
                //					blastBean.setBlast(presentity.getBlast());
                //					Message blastMsg = new Message(NetworkConstant.CHANGE_BLAST_BEAN, blastBean);
                //					communicatorService.sendMessage(subscribers, statusMsg, blastMsg);
                //				}
                return subscribers;
            }
        }
        return null;
    }

    @Override
    public List<String> publishStatus(String username, Show show, String blast) {
        Presentity presentity = (Presentity) cacheService.getCache().get(PRESENTITY_PREFIX + username);
        if (show != null) {
            presentity.setShow(show);
        }
        if (blast != null) {
            presentity.setBlast(blast);
        }
        cacheService.getCache().set(PRESENTITY_PREFIX + username, CACHE_TIME, presentity);
        List<String> subscribers = subscriberService.getSubscribers(username);
        if (subscribers != null) {
            //			if (show != null) {
            //				ChangeStatusBean statusBean = new ChangeStatusBean();
            //				statusBean.setFriendName(username);
            //				if (show == Show.Invisible) {
            //					show = Show.Offline;
            //				}
            //				statusBean.setStatus(show);
            //				Message statusMsg = new Message(NetworkConstant.CHANGE_STATUS_BEAN, statusBean);
            //				communicatorService.sendMessage(subscribers, statusMsg);
            //			}
            //			if (blast != null) {
            //				ChangeBlastBean blastBean = new ChangeBlastBean();
            //				blastBean.setFriend(username);
            //				blastBean.setBlast(presentity.getBlast());
            //				Message blastMsg = new Message(NetworkConstant.CHANGE_BLAST_BEAN, blastBean);
            //				communicatorService.sendMessage(subscribers, blastMsg);
            //			}
            return subscribers;
        }
        return null;
    }
}
