package com.yanmade.plat.publish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface MyGroupService {

	boolean insert(Map<String, Object> map);

	boolean insertUser(Map<String, Object> map);

	List<HashMap<String, Object>> getUserGroup(Map<String, Object> map);

	int getUserGrpCnt(Map<String, Object> map);

	boolean update(Map<String, Object> map);

	boolean deleteUser(Map<String, Object> map);

	boolean delete(Map<String, Object> map);

}
