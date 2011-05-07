package com.beecas.service;

import java.util.List;

public interface ISubscriberService {
	public void subscribe(String id, List<String> subscribers);
	public void subscribe(String id, String subscriber);
	public List<String> getSubscribers(String id);
	public void unSubscribe(String id, String subscriber);
}
