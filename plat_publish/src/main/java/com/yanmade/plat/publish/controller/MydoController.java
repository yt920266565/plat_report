package com.yanmade.plat.publish.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.publish.dao.MydoMapper;
import com.yanmade.plat.publish.entity.VerMain;
import com.yanmade.plat.publish.service.MydoService;

@RestController
@RequestMapping("/versions")
public class MydoController {

	private static final int COOKIE_SAVE_TIME = 60 * 60 * 24 * 1;
	
	@Value("${spring.index.url}")
	private String indexUrl;

	@Autowired
	MydoService service;
	
	@Autowired 
	MydoMapper mapper;

	@Autowired
	RestTemplate restTemplate;

	// 待我处理
	@GetMapping(value = "/mydo")
	@ResponseBody
	public ApiResponse<List<Map<String, Object>>> getMydo(@RequestParam Map<String, Object> input) {
		List<Map<String, Object>> list = service.getMydo(input);
		String infoPeopleKey = "infoPeople";
		
		for (Map<String, Object> entries:list) {
			
			if(Objects.isNull(entries.get(infoPeopleKey))) {
				continue;
			}
			String initPeople = entries.get(infoPeopleKey).toString();
			initPeople = initPeople.replaceAll("[\u4e00-\u9fa5]+\\(", "");
			initPeople = initPeople.replaceAll("\\)", "");
			entries.put("initPeople", initPeople);
		}
		int count = service.getMydoCnt(input);
		
		return ApiResponseUtil.success(list,count);
	}
	
	//测试阶段点击处理按钮就将状态改为测试中
	@PutMapping("/mydo/changeTestStatus/{flowNo}")
	public void changeTestStatus(@PathVariable String flowNo) {
		service.changeTestStatus(flowNo);
	}
	
	@GetMapping("/mydo/getUserInfoByCode")
	public Map<String,Object> getUserInfoByCode(@RequestParam String[] codeList){
		Map<String, Object> resMap = new HashMap<>();
		List<String> list = mapper.getUserInfoByCode(codeList);
		resMap.put("data", list);
		return resMap;
	}

	// 修改我创建的
	@PutMapping("/{flowNo}")
	public ApiResponse<VerMain> updateMydo(@RequestBody VerMain ver, @PathVariable String flowNo) {
		ver.setFlowNo(flowNo);
		boolean result = service.updateMydo(ver);
		if (!result) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_UPDATE);
		}
		return ApiResponseUtil.success(ver);
	}

	@GetMapping("/users")
	public Map<String, Object> getUsers() {
		List<HashMap<String, Object>> list = service.getUsers();
		Map<String, Object> map = new HashMap<>();
		map.put("data", list);
		return map;
	}

	@GetMapping("/usersRoles")
	public Map<String, Object> getUsersRoles() {
		List<HashMap<String, Object>> list = service.getUsersRoles();
		Map<String, Object> map = new HashMap<>();
		map.put("data", list);
		return map;
	}

	@GetMapping("/getDistinctUR")
	public Map<String, Object> getDistinctUR() {
		List<HashMap<String, Object>> list = service.getDistinctUR();
		Map<String, Object> map = new HashMap<>();
		map.put("data", list);
		return map;
	}
	
	@GetMapping("/getAllDistinctUR")
	public Map<String, Object> getAllDistinctUR() {
		List<HashMap<String, Object>> list = mapper.getAllDistinctUR();
		Map<String, Object> map = new HashMap<>();
		map.put("data", list);
		return map;
	}
	
	@Value("${cookie.domain}")
	private String domain;

	// erp获取token接口
	@GetMapping("/erpToken")
	public void getErpToken(@RequestParam Map<String, Object> input, HttpServletResponse response) throws IOException {
		String loginID = (String) input.get("LoginID");
		if (loginID.equals("")) {
			response.sendRedirect("http://erp.yanmade.com");
			return;
		}
		String url = "http://192.168.0.10:81/api/CommonApi/GetOriginalToken?LoginID=" + loginID;
		JSONObject result = restTemplate.getForObject(url, JSONObject.class);
		if ((int) result.get("state") != 1) {
			response.sendRedirect("http://erp.yanmade.com");
			return;
		}
		String tokenString = result.getString("data");
		Cookie erpcookie = new Cookie("erpToken", URLEncoder.encode(tokenString, "utf-8"));
		erpcookie.setHttpOnly(true);
	//	erpcookie.setSecure(true); 设置这个浏览器无法获取cookie,只有在https协议中才能携带
		erpcookie.setMaxAge(COOKIE_SAVE_TIME);
		erpcookie.setPath("/");
	//	erpcookie.setPath("/versions");
		erpcookie.setDomain(domain);
		response.addCookie(erpcookie);
		response.sendRedirect(indexUrl);
	}

	// 服务段端获取token
	@GetMapping("/getToken")
	public Map<String, Object> getToken(HttpServletRequest request) {
		String resultString = "";
		Map<String, Object> map = new HashMap<>();
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			map.put("msg", "");
			return map;
		}
		String cookName = "erpToken";
		for (Cookie c : cookies) {
			if (c.getName().equals(cookName)) {
				resultString = c.getValue();
				map.put("msg", "success");
				map.put("token", resultString);
				return map;
			}
		}
		return map;
	}


}
