package com.yanmade.plat.publish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yanmade.plat.publish.entity.VerMain;

@Service
public interface MydoneService {

	public List<Map<String, Object>> getMydone(Map<String, Object> input);

	public int getMydoneCnt(Map<String, Object> input);

	public Map<String, Object> getNameById(Map<String, Object> input);

	public List<HashMap<String, Object>> getProcess(String mFlowNo);

	public List<HashMap<String, Object>> getDeptments(Map<String, Object> input);

	public List<HashMap<String, Object>> getUsers(Map<String, Object> input);

	public int getDeptCnt();

	public int getUsersCnt();

	public boolean update(VerMain ver);

	public Map<String, Object> cheVerId(VerMain ver);
	
	List<HashMap<String, Object>> getApplyGrpAll(String staffCode);

}
