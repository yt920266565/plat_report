package com.yanmade.plat.publish.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.handler.CustomException;
import com.yanmade.plat.framework.util.RedisUtil;
import com.yanmade.plat.publish.dao.ApplyGroupMapper;

@Service
public class ApplyGroupServiceImpl implements ApplyGroupService {

	@Autowired
	ApplyGroupMapper mapper;

	@Autowired
	ApplicationContext context;

	@Autowired
	RedisUtil redisUtil;

	private static final String APPLYGRPID = "apply_group_id";

	@Override
	public List<Map<String, Object>> get(Map<String, Object> map) {
		if (map.get("page") != null) {
			int page = Integer.parseInt(map.get("page").toString());
			int limit = Integer.parseInt(map.get("limit").toString());
			map.put("page", (page - 1) * limit);
			map.put("limit", limit);
		}
		return mapper.get(map);
	}

	@Override
	public boolean delete(int groupId) {
		return mapper.delete(groupId);
	}

	@Override
	public boolean insertGroup(Map<String, Object> map) {
		map.put("groupId", redisUtil.increment(APPLYGRPID));
		if (map.get("list") == null) {
			return mapper.insertGroup(map);
		}
		return context.getBean(ApplyGroupServiceImpl.class).insert(map);
	}

	@Transactional
	public boolean insert(Map<String, Object> map) {
		boolean res = mapper.insertGroup(map);
		if (!res) {
			throw new CustomException(ErrMsgEnum.NOT_INSERT);
		}
		
		boolean res1 = mapper.insertStaff(map);
		if(!res1) {
			throw new CustomException(ErrMsgEnum.NOT_INSERT);
		}
		
		return true;
	}

	@Override
	public boolean insertStaff(Map<String, Object> map) {
		return mapper.insertStaff(map);
	}

	@Override
	public int getCnt() {
		return mapper.getCnt();
	}

	@Override
	public boolean put(Map<String, Object> map) {
		return context.getBean(ApplyGroupServiceImpl.class).update(map);
	}

	@Transactional
	public boolean update(Map<String, Object> map) {
		// 修改组名
		boolean res = mapper.put(map);
		if(!res) {
			throw new CustomException(ErrMsgEnum.NOT_UPDATE);
		}
		
		// 先删除组包含的成员，再插入新的成员
		mapper.deleteStaff(map.get("groupId"));
		
		//有传过来人员list就插入组对应的人员表
		if(map.get("list") != null) {
			boolean res1 =  mapper.insertStaff(map);
			if(!res1) {
				throw new CustomException(ErrMsgEnum.NOT_INSERT);
			}
		}
		return true;
	}

}
