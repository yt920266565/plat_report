package com.yanmade.plat.publish.service;

import java.util.List;
import java.util.Map;

import com.yanmade.plat.publish.entity.VerOperation;



public interface DiscardService {
	
	List<Map<String, Object>> get(Map<String, Object> map);
	
	int getCnt(Map<String, Object> map);
	
	public Map<String, Object> getDiscardOpt(String flowNo);
	
    public void post(VerOperation vop);
	
	public boolean delete(String flowNo);
	
	public void put(VerOperation vop);


}
