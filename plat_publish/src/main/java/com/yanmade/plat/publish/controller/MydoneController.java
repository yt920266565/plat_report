package com.yanmade.plat.publish.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.publish.entity.VerMain;
import com.yanmade.plat.publish.service.MydoneService;
import com.yanmade.plat.publish.service.VersionService;

@RestController
@RequestMapping("/versions")
public class MydoneController {

	private static final String COUNT = "count";
	private static final String DATA = "data";
	private static final String MSG = "msg";
	private static final String SUCCESS = "success";

	@Autowired
	MydoneService service;

	@Autowired
	VersionService vservice;

	// 我所在申请分组所有的人员
	@GetMapping(value = "/mydone/getApplyGrpAll")
	public Map<String, Object> getApplyGrpAll() {
		Map<String,Object> map = new HashMap<>();
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<HashMap<String, Object>> list = service.getApplyGrpAll(username);
		map.put(DATA, list);
		return map;
	}
	
	// 我处理的
	@GetMapping(value = "/mydone")
	public ApiResponse<List<Map<String, Object>>> getMydone(@RequestParam Map<String, Object> input) {
		List<Map<String, Object>> list = service.getMydone(input);
		int count = service.getMydoneCnt(input);
		return ApiResponseUtil.success(list, count);
	}

	// 更新我申请过的基本信息
	@PutMapping(value = "/mydone/update")
	public ApiResponse<Object> update(@RequestBody VerMain ver) {
		Map<String, Object> map = service.cheVerId(ver);
		int count = Integer.parseInt(map.get(COUNT).toString());
		// 根据verId和verNo查出来的唯一流水号
		String flowNo = "";
		// 传过来的流水号
		String verflowNo = "";
		if (count != 0) {
			flowNo = map.get("flowNo").toString();
			verflowNo = ver.getFlowNo();
		}
		// 软件名称和版本号在ver_main表存在且不是verflowNo的软件名称和版本号
		if (count == 1 && !flowNo.equals(verflowNo)) {
			return ApiResponseUtil.failure(HttpStatus.CONFLICT, ver, ErrMsgEnum.FAILURE);
		}
		
		boolean result = service.update(ver);
		if (!result) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_UPDATE);
		}
		return ApiResponseUtil.success(ver);
	}

	@GetMapping(value = "/{mFlowNo}/getProcess")
	public Map<String, Object> getProcess(@PathVariable String mFlowNo) {
		List<HashMap<String, Object>> list = service.getProcess(mFlowNo);
		Map<String, Object> map = new HashMap<>();
		map.put(DATA, list);
		map.put(MSG, SUCCESS);
		return map;
	}

	@GetMapping(value = "/getNameById")
	public Map<String, Object> getNameById(@RequestParam Map<String, Object> input) {
		Map<String, Object> list = service.getNameById(input);
		Map<String, Object> map = new HashMap<>();
		map.put(DATA, list);
		map.put(MSG, SUCCESS);
		return map;
	}

	// 获取部门
	@GetMapping(value = "/getDeptments")
	public ApiResponse<List<HashMap<String, Object>>> getDeptments(@RequestParam Map<String, Object> input) {
		List<HashMap<String, Object>> list = service.getDeptments(input);
		int count = service.getDeptCnt();
		return ApiResponseUtil.success(list, count);
	}

	// 获取用户
	@GetMapping(value = "/getUsers")
	public ApiResponse<List<HashMap<String, Object>>> getUsers(@RequestParam Map<String, Object> input) {
		List<HashMap<String, Object>> list = service.getUsers(input);
		int count = service.getUsersCnt();
		return ApiResponseUtil.success(list, count);
	}

}
