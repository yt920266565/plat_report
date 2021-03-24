package com.yanmade.plat.publish.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.publish.service.DepartKeyService;

@RequestMapping("/departKey")
@RestController
public class DepartKeyController {

	@Autowired
	DepartKeyService service;

	@PostMapping
	public ApiResponse<Map<String, String>> insert(@RequestBody Map<String, String> map) {
		if (StringUtils.isEmpty(map.get("departId")) || Objects.isNull(map.get("keywords"))) {
			return ApiResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR, map, ErrMsgEnum.INVALID_PARAMETER);
		}
		String result = service.insert(map);
		if (!StringUtils.isEmpty(result)) {
			return ApiResponseUtil.failure(HttpStatus.UNAUTHORIZED, map, "", result);
		}

		return ApiResponseUtil.success(map);
	}

	@DeleteMapping
	public ApiResponse<Integer> delete(@RequestParam int departId) {
		String result = service.delete(departId);
		if (!StringUtils.isEmpty(result)) {
			return ApiResponseUtil.failure(HttpStatus.UNAUTHORIZED, departId, "", result);
		}

		return ApiResponseUtil.success(departId);
	}

	@GetMapping
	public ApiResponse<List<Map<String, Object>>> getDeptKeywords() {
		List<Map<String, Object>> list = service.getDeptKeywords();
		int count = service.getDeptKeywordsCnt();
		return ApiResponseUtil.success(list, count);
	}

	@GetMapping("/getKeyWordsByDeptId")
	public ApiResponse<List<String>> getKeyWordsByDeptId(@RequestParam String departId) {
		List<String> list = service.getKeyWordsByDeptId(departId);
		return ApiResponseUtil.success(list);
	}

}
