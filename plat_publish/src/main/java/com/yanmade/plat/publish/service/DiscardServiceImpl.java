package com.yanmade.plat.publish.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.handler.CustomException;
import com.yanmade.plat.framework.service.CacheService;
import com.yanmade.plat.publish.dao.DiscardMapper;
import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.dao.VerOperatorMapper;
import com.yanmade.plat.publish.entity.VerMain;
import com.yanmade.plat.publish.entity.VerOperation;
import com.yanmade.plat.publish.util.FlowNoUtil;

/**
 * @author 0103364
 *
 */
@Service
public class DiscardServiceImpl implements DiscardService {

	private static final String FUNCTION = "version.published.query";

	@Autowired
	DiscardMapper mapper;

	@Autowired
	VerMainMapper vmMapper;

	@Autowired
	VerOperatorMapper vopMapper;

	@Autowired
	CacheService cacheService;

	@Autowired
	ApplicationContext context;

	@Autowired
	FlowNoUtil flowNoUtil;

	@Override
	public List<Map<String, Object>> get(Map<String, Object> map) {
		addPage(map);
		addAutority(map);
		return mapper.get(map);
	}

	@Override
	public int getCnt(Map<String, Object> map) {
		return mapper.getCnt(map);
	}

	// 增加sql分页参数
	private void addPage(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		map.put("page", page-1);
		map.put("limit", limit);
	}

	// 添加权限id参数
	private void addAutority(Map<String, Object> map) {
		int id = getCurrenter();
		// 管理员拥有所有权限
		if (cacheService.isAdminCache(id)) {
			map.put("roleId", id);
			return;
		}

		String dept = cacheService.getUserFunctionDepartmentsCache(id, FUNCTION);
		List<Object> deptList = new ArrayList<>();
		// 给一个初始值 防止dept为空时，被当做管理员处理了
		deptList.add(0);
		if (dept != null) {
			deptList = Arrays.asList(dept.split(","));
		}

		map.put("deptList", deptList);
	}

	// 获取登录人用户的id
	private int getCurrenter() {
		// 从spring security获取登录用户工号
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(username.equals("anonymousUser")) {
			return 0;
		}
		return vmMapper.getIdByName(username);
	}

	@Override
	public Map<String, Object> getDiscardOpt(String flowNo) {
		return mapper.getDiscardOpt(flowNo);
	}

	@Override
	public void post(VerOperation vop) {
		//废弃版本
		if(vop.getName().equals("Discard")) {
			context.getBean(DiscardServiceImpl.class).insertDiscard(vop);
			return;
		}
		//恢复版本
		context.getBean(DiscardServiceImpl.class).insertPublish(vop);
	}

	@Transactional
	public void insertDiscard(VerOperation vop) {
		VerMain verMain = mapper.getPublishByFlowNo(vop.getmFlowNo());
		// 状态10 为已废弃版本。数据存在ver_discard表里面
		verMain.setStatus(10);

		// 从ver_publish删除要废弃的版本
		boolean res = mapper.deletePublisByFlowNo(vop.getmFlowNo());
		if (!res) {
			throw new CustomException(ErrMsgEnum.NOT_DELETE);
		}

		// 插入操作表
		insertOpt(vop);

		// 插入ver_discard
		mapper.post(verMain);
	}

	@Transactional
	public void insertPublish(VerOperation vop) {
		VerMain verMain = mapper.getDiscardByFlowNo(vop.getmFlowNo());
		// 状态6为已发布版本。数据存在ver_publish表里面
		verMain.setStatus(6);

		// 从ver_dsicard删除要废弃的版本
		boolean res = mapper.delete(vop.getmFlowNo());
		if (!res) {
			throw new CustomException(ErrMsgEnum.NOT_DELETE);
		}
		
		// 插入操作表
		insertOpt(vop);
		//插入ver_publish
		vmMapper.insertPub(verMain);
	}

	@Override
	public boolean delete(String flowNo) {
		return mapper.delete(flowNo);
	}

	@Override
	public void put(VerOperation vop) {
		insertOpt(vop);
	}

	/**
	 * 插入操作表
	 * 
	 * @param vop
	 */
	public void insertOpt(VerOperation vop) {
		vop.setFlowNo(flowNoUtil.getOperatorFlowNo());
		vop.setOperator(getCurrenter());
		vop.setOptime(new Date());
		vop.setResult(1);
		boolean res = vopMapper.insert(vop);
		if (!res) {
			throw new CustomException(ErrMsgEnum.NOT_INSERT);
		}
	}



}
