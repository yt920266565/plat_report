package com.yanmade.plat.publish.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.publish.dao.MyGroupMapper;
import com.yanmade.plat.publish.service.MyGroupService;

@RestController
@RequestMapping("/myGroup")
public class MyGroupController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	MyGroupService service;

	@Autowired
	MyGroupMapper mapper;

	@PostMapping("/insertUser")
	public Map<String, Object> insertUser(@RequestBody Map<String, Object> map) {
		boolean res = service.insertUser(map);
		Map<String, Object> rMap = new HashMap<>();
		if (!res) {
			rMap.put("msg", FAIL);
			return rMap;
		}
		rMap.put("msg", SUCCESS);
		return rMap;
	}

	@GetMapping("/getUserGroup")
	public ApiResponse<List<HashMap<String, Object>>> getUserGroup(@RequestParam Map<String, Object> map) {
		List<HashMap<String, Object>> list = service.getUserGroup(map);
		int count = service.getUserGrpCnt(map);
		return ApiResponseUtil.success(list, count);
	}

	@PutMapping("/update")
	public Map<String, Object> update(@RequestBody Map<String, Object> map) {
		boolean res = service.update(map);
		Map<String, Object> rMap = new HashMap<>();
		if (!res) {
			rMap.put("msg", FAIL);
			return rMap;
		}
		rMap.put("msg", SUCCESS);
		return rMap;
	}
	
	@DeleteMapping("/delete")
	public Map<String, Object> delete(@RequestParam Map<String, Object> map) {
		boolean res = service.delete(map);
		Map<String, Object> rMap = new HashMap<>();
		if (!res) {
			rMap.put("msg", FAIL);
			return rMap;
		}
		rMap.put("msg", SUCCESS);
		return rMap;
	}
	
	@GetMapping("/getMyGroup")
	public Map<String, Object> getMyGroup() {
		List<HashMap<String, Object>> list = mapper.getMyGroup(getUserId());
		Map<String, Object> rMap = new HashMap<>();
		rMap.put("data", list);
		return rMap;
	}
	
	//当前登录人的工号
	private String getUserId() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
	
	@GetMapping("/getStaff")
	public Map<String, Object> getStaff(@RequestParam String groupId) {
		List<HashMap<String, Object>> list = mapper.getStaff(groupId);
		Map<String, Object> rMap = new HashMap<>();
		rMap.put("data", list);
		return rMap;
	}

}
