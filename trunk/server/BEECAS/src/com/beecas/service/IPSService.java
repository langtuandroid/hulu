package com.beecas.service;

import java.util.List;

import com.beecas.model.roster.Presentity;
import com.beecas.model.roster.Show;

public interface IPSService{
	List<String> publishOnline(Presentity presentity);
	List<String> publishStatus(String username, Show show, String status);
	List<String> publishDisconnect(String username);
	Presentity fetch(String username);
	Show getShow(String username);
	String getBlast(String username);
	void delete(String username);
}
