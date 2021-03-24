package com.yanmade.plat.publish.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanmade.plat.framework.service.CacheService;
import com.yanmade.plat.publish.dao.DepartKeyMapper;
import com.yanmade.plat.publish.util.ErrMsgUtil;

@Service
public class DepartKeyService {
	
	@Autowired
	DepartKeyMapper mapper;
	
	@Autowired
	CacheService cacheService;
	
	@Transactional
	public String insert(Map<String, String> map) {
		int departId = Integer.parseInt(map.get("departId"));
		if(!StringUtils.isEmpty(isDepartment(departId))) {
			return isDepartment(departId);
		}
		mapper.delete(departId);
		
		String keywords = map.get("keywords");
		if(StringUtils.isEmpty(keywords)) {
			return ErrMsgUtil.EMPTY;
		}
		
		List<String> list = new ArrayList<>();
		if(keywords.contains(",") || keywords.contains("，")) {
			keywords = keywords.replaceAll("，", ",");
			list.addAll(Arrays.asList(keywords.split(",")));
		}else {
			list.add(keywords);
		}
		
		for (int i = list.size()-1; i >= 0; i--) {
			String keyword = list.get(i);
			if(StringUtils.isEmpty(keyword)) {
				list.remove(i);
				continue;
			}
			
			if(keyword.length() > 30) {
				list.set(i,keyword.substring(0,29));
			}
		}
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("departId", departId);
		paraMap.put("list", list);
		
		mapper.insert(paraMap);
		return ErrMsgUtil.EMPTY;
	}
	
	public String delete(int departId) {
		if(!StringUtils.isEmpty(isDepartment(departId))) {
			return isDepartment(departId);
		}
		mapper.delete(departId);
		return ErrMsgUtil.EMPTY;
	}
	
	/**判断登录人所在部门与要操作数据的部门是否一致
	 * @param departId 要操作的部门数据
	 * @return
	 */
	private String isDepartment(int departId) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(username.equals("anonymousUser")) {
			return ErrMsgUtil.NO_AUTHENCATED;
		}
		
		Map<String, Object> userMap = mapper.getUserInfoByName(username);
		int userId = Integer.parseInt(userMap.get("id").toString());
		if(cacheService.isAdminCache(userId)) {
			return ErrMsgUtil.EMPTY;
		}
		
		int deptId = Integer.parseInt(userMap.get("deptid").toString());
		int pid = mapper.getPidById(deptId);
		if(departId != deptId && pid != departId) {
			return ErrMsgUtil.ONLY_ROLE;
		}
		
		return ErrMsgUtil.EMPTY;
	}
	
	public List<Map<String, Object>> getDeptKeywords(){
		return mapper.getDeptKeywords();
	}
	
	public int getDeptKeywordsCnt(){
		return mapper.getDeptKeywordsCnt();
	}
	
	public List<String> getKeyWordsByDeptId(String departId){
		return mapper.getKeyWordsByDeptId(departId);
	}

}
