package com.yanmade.plat.publish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanmade.plat.framework.util.RedisUtil;
import com.yanmade.plat.publish.dao.MyGroupMapper;

@Service
public class MyGroupServiceImpl implements MyGroupService{
	
	private static final String GROUPID = "groupId";
	private static final String USERID = "userId";
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	MyGroupMapper mapper;

	@Override
	public boolean insert(Map<String, Object> map) {
		return mapper.insert(map);
	}

	@Override
	@Transactional
	public boolean insertUser(Map<String, Object> map) {
		if(!islogin()) {
			return false;
		}
		map.put(USERID, getUserId());
		int count = mapper.checkGrpName(map);
		//组名重复
		if(count > 0) {
			return false;
		}
		
		int groupId = redisUtil.increment(GROUPID);
		map.put(GROUPID, groupId);
		if(!mapper.insertUser(map)) {
			return false;
		}
		
		//只填了组名没有 填写人员
		if(map.get("list").toString().equals("[]")) {
			return true;
		}
		return insert(map);
	}
	
	private boolean islogin() {
		String loginString = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return !loginString.equals("anonymousUser");
	}

	@Override
	public List<HashMap<String, Object>> getUserGroup(Map<String, Object> map) {
		if(map.get("page") != null) {
			int page = Integer.parseInt(map.get("page").toString());
			int limit = Integer.parseInt(map.get("limit").toString());
			map.put("page", (page - 1) * limit);
			map.put("limit", limit);
		}
		//获取当前登录人工号
		map.put(USERID, getUserId());
		return mapper.getUserGroup(map);
	}

	@Override
	public int getUserGrpCnt(Map<String, Object> map) {
		map.put(USERID, getUserId());
		return mapper.getUserGrpCnt(map);
	}
	
	//当前登录人的工号
	private String getUserId() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}

	@Override
	@Transactional
	public boolean update(Map<String, Object> map) {
		map.put(USERID, getUserId());
		int count = mapper.checkGrpName(map);
		
		//组名重复,这里是查询除了本身以外的组名
		if(count > 0) {
			return false;
		}
		boolean res = mapper.update(map);
		if(!res) {
			return false;
		}
		//先删除该分组对应的人员
		deleteUser(map);
		
		//前端没有传组人员过来，直接返回
		if(map.get("list").toString().equals("[]")) {
			return true;
		}
		return insert(map);
	}

	@Override
	public boolean deleteUser(Map<String, Object> map) {
		return mapper.deleteUser(map);
	}

	@Override
	public boolean delete(Map<String, Object> map) {
		//删除组
		 mapper.delete(map);
		 
		//如果组里面有人员,删除组对应人员
		 if(mapper.hasGroupUser(map) > 0) {
			 return deleteUser(map);
		 }
		 
		 return true;
	}

}
