package com.yanmade.plat.publish.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.yanmade.plat.publish.service.ApplyGroupService;

@RestController
@RequestMapping("/applyGroup")
public class ApplyGroupController {
	
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	ApplyGroupService service;
	
	
	@GetMapping
	public ApiResponse<List<Map<String, Object>>> get(@RequestParam Map<String, Object> map){
		List<Map<String, Object>> list = service.get(map);
		int count = service.getCnt();
		
		if(list.toString().equals("[null]")) {
			return ApiResponseUtil.success(new ArrayList<>(), count);
		}
		
		return ApiResponseUtil.success(list, count);
	}
	
	@PutMapping
	public Map<String, Object> put(@RequestBody Map<String, Object> map){
		Map<String,Object> resMap = new HashMap<>();
		boolean res = service.put(map);
		if(!res) {
			resMap.put("msg", FAIL);
			return resMap;
		}
		resMap.put("msg", SUCCESS);
		return resMap;
	}
	
	@PostMapping
	public Map<String, Object> post(@RequestBody Map<String, Object> map){
		Map<String,Object> resMap = new HashMap<>();
		boolean res = service.insertGroup(map);
		if(!res) {
			resMap.put("msg", FAIL);
		}
		resMap.put("msg", SUCCESS);
		return resMap;
	}
	
	@DeleteMapping
	public Map<String, Object> delete(@RequestParam int groupId){
		Map<String,Object> resMap = new HashMap<>();
		boolean res = service.delete(groupId);
		if(!res) {
			resMap.put("msg", FAIL);
		}
		resMap.put("msg", SUCCESS);
		return resMap;
	}
}
