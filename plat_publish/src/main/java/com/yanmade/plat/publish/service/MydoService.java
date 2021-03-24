package com.yanmade.plat.publish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yanmade.plat.publish.entity.VerMain;

@Service
public interface MydoService {
	public List<Map<String, Object>> getMydo(Map<String, Object> input);

	public int getMydoCnt(Map<String, Object> input);

	public boolean updateMydo(VerMain ver);

	public List<HashMap<String, Object>> getUsers();

	public List<HashMap<String, Object>> getUsersRoles();

	public List<HashMap<String, Object>> getDistinctUR();
	
	public void changeTestStatus(String flowNo);
	
}
