package com.yanmade.plat.publish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yanmade.plat.publish.dao.MydoMapper;
import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.entity.VerMain;


@Service
public class MydoServiceImpl implements MydoService {

	@Autowired
	MydoMapper mapper;

	@Autowired
	VerMainMapper vmapper;

	@Override
	public List<Map<String, Object>> getMydo(Map<String, Object> input) {
		String limitStr = "limit";
		if (input.get(limitStr) != null) {
			int page = Integer.parseInt(input.get("page").toString());
			int limit = Integer.parseInt(input.get(limitStr).toString());
			if (page < 1) {
				input.put("page", (page * limit));
			} else {
				input.put("page", (page - 1) * limit);
			}
			input.put(limitStr, limit);
		} else {
			input.put(limitStr, 50);
			input.put("page", 0);
		}
		addIsTestManager(input);
		return mapper.getMydo(input);
	}

	@Override
	public int getMydoCnt(Map<String, Object> input) {
		return mapper.getMydoCnt(input);
	}

	private void addIsTestManager(Map<String, Object> input) {
		// 查询用户所用有的角色，如果是测试负责人，可以处理所有待测试版本
		List<Integer> list = mapper.isTestManager(getLoginUserId());
		if(!list.isEmpty()) {
			input.put("list", list);
		}
		input.put("currenter", getLoginUserId());
	}

	@Override
	public boolean updateMydo(VerMain ver) {
		return mapper.updateMydo(ver);
	}

	@Override
	public List<HashMap<String, Object>> getUsers() {
		return mapper.getUsers();
	}

	@Override
	public List<HashMap<String, Object>> getUsersRoles() {
		return mapper.getUsersRoles();
	}

	@Override
	public List<HashMap<String, Object>> getDistinctUR() {
		return mapper.getDistinctUR();
	}

	@Override
	public void changeTestStatus(String flowNo) {
		int currenter = vmapper.getCurrenter(flowNo);
		// 点击处理的人是当前处理人就改变待测试的状态
		if (currenter == getLoginUserId()) {
			mapper.changeTestStatus(flowNo);
		}
	}

	// 返回当前登录用户的id
	private int getLoginUserId() {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return vmapper.getIdByName(username);
	}

}