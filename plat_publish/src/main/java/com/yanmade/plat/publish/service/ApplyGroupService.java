package com.yanmade.plat.publish.service;

import java.util.List;
import java.util.Map;


public interface ApplyGroupService {
	
	List<Map<String, Object>> get(Map<String, Object> map);
	
	int getCnt();
	
	boolean delete(int groupId);
	
	boolean insertGroup(Map<String, Object> map);
	
	boolean insertStaff(Map<String,Object> map);
	
	boolean put(Map<String, Object> map);

}
