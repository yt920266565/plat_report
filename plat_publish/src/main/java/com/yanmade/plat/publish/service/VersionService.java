package com.yanmade.plat.publish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.yanmade.plat.publish.entity.VerMain;

@Service
public interface VersionService {

	public int getIdByName(String username);

	public boolean insert(VerMain ver);

	public boolean update(VerMain ver);

	public boolean delete(String flowNo);

	public VerMain getVerMain(String flowNo);

	public int verIsRept(VerMain ver);

	public boolean insertOpt(VerMain ver, int id);

	public boolean insertOver(VerMain ver);

	public boolean insertPub(VerMain ver, String sendSale);

	public List<HashMap<String, Object>> getPublished(Map<String, Object> input);

	public int getPubCnt(Map<String, Object> input);

	public List<HashMap<String, Object>> getMaintain(Map<String, Object> input);

	public int getMaintainCnt(Map<String, Object> input);

	public Boolean verMaintain(VerMain ver);

	public Boolean rollBack(VerMain ver);

	public List<HashMap<String, Object>> getErpDic();

	public List<Map<String, Object>> getUnpublished();

	public Map<String, Object> getVerPath(@PathVariable(value = "flowNo") String flowNo);

	public List<String> getFlowNo(Map<String, Object> map);

	public List<HashMap<String, Object>> getPubExcel(Map<String, Object> map);


}
