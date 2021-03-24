package com.yanmade.plat.publish.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.service.UserService;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.entity.VerOperation;
import com.yanmade.plat.publish.service.DiscardService;

@RestController
@RequestMapping("/versions/discard")
public class DiscardController {

	@Autowired
	DiscardService service;

	@Autowired
	UserService userService;

	@Autowired
	VerMainMapper vmMapper;

	@GetMapping
	public ApiResponse<List<Map<String, Object>>> get(@RequestParam Map<String, Object> map) {
		List<Map<String, Object>> list = service.get(map);
		int count = service.getCnt(map);

		// 查询当前用户的功能权限
		Map<String, Object> funMap = userService.getFunctionsByUserId(getCurrenterId());
		Object[] funList = (Object[]) funMap.get("authority");
		for (Object f : funList) {
			// 有版本废弃权限,放到list的目的是layui数据表格的工具栏判断
			if (f.equals("version.discard")) {
				for (Map<String, Object> m : list) {
					m.put("discard", true);
				}
				break;
			}
		}
		return ApiResponseUtil.success(list, count);
	}

	@PutMapping
	public ApiResponse<VerOperation> put(@RequestBody VerOperation vop) {
		service.put(vop);
		return ApiResponseUtil.success(vop);
	}

	@PostMapping
	public ApiResponse<Object> post(@RequestBody VerOperation vop) {
		service.post(vop);
		return ApiResponseUtil.success(vop);
	}

	// 获取单个已废弃版本
	@GetMapping("/getDiscardOpt/{flowNo}")
	public ApiResponse<Object> getDiscardOpt(@PathVariable String flowNo) {
		Map<String, Object> map = service.getDiscardOpt(flowNo);
		return ApiResponseUtil.success(map);
	}

	// 获取当前登录的用户id
	private int getCurrenterId() {
		String loginString = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(loginString.equals("anonymousUser")) {
			return 0;
		}
		return vmMapper.getIdByName(loginString);
	}
	

}
