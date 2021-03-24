package com.yanmade.plat.report.controller;

import com.alibaba.fastjson.JSONObject;
import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.report.entity.WorkAreaDto;
import com.yanmade.plat.report.entity.SummaryDay;
import com.yanmade.plat.report.service.IndexService;
import com.yanmade.plat.report.service.SummaryDayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 * 首页controller
 */
@RestController
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private static final int COOKIE_SAVE_TIME = 60 * 60 * 24 * 7;

    @Autowired
    private IndexService indexService;

    @Autowired
    private SummaryDayService summaryDayService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${index.url}")
    private String indexUrl;

    @Value("${spring.datasource.url}")
    private String url;

    @PostConstruct
    public void print(){
        logger.info("url : {}", url);
    }

    /**
     * 获取客户区域以及接入机台数
     * @return
     */
    @GetMapping("/index/workarea")
    public ApiResponse<List<WorkAreaDto>> getWorkArea(){
        List<WorkAreaDto> workAreaDtos = indexService.listWorkAreas();
        return ApiResponseUtil.success(workAreaDtos);
    }

    /**
     * 根据日期，客户区域，产品类型，料号，测试类型条件获取测试总数，良率，pass数
     * @param map
     * @return
     */
    @GetMapping("/index/passcount")
    public ApiResponse<List<SummaryDay>> getPassCountByProductType(@RequestParam Map<String, Object> map){
        String workArea = (String) map.get("workArea");
        String productType = (String) map.get("productType");
        if (StringUtils.isEmpty(workArea) || StringUtils.isEmpty(productType)){
            return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, null, ErrMsgEnum.INVALID_PARAMETER);
        }

        List<SummaryDay> summaryDays = indexService.getPassCount(map);
        return ApiResponseUtil.success(summaryDays);
    }

    /**
     * 获取某个客户区域内的所有产品类型
     * @param workArea
     * @return
     */
    @GetMapping("/index/productTypes")
    public ApiResponse<List<String>> listProductTypes(@RequestParam String workArea){
        if (StringUtils.isEmpty(workArea)){
            return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, null, ErrMsgEnum.INVALID_PARAMETER);
        }
        return ApiResponseUtil.success(summaryDayService.listProductTypes(workArea));
    }

    /**
     * 获取配置文件配置的客户区域
     * @return
     */
    @GetMapping("/index/workAreaNames")
    public ApiResponse<List<String>> getWorkAreaNames(){
       return ApiResponseUtil.success(indexService.getWorkAreaNames());
    }

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
        erpcookie.setMaxAge(COOKIE_SAVE_TIME);
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

    /**
     * 获取某个客户区域内的所有料号
     * @param workArea
     * @return
     */
    @GetMapping("/index/partNames")
    public ApiResponse<List<String>> listPartNames(@RequestParam String workArea){
        if (StringUtils.isEmpty(workArea)){
            return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, null, ErrMsgEnum.INVALID_PARAMETER);
        }
        return ApiResponseUtil.success(indexService.listPartNames(workArea));
    }

    /**
     * 获取某个客户区域内的所有测试类型
     * @param workArea
     * @return
     */
    @GetMapping("/index/testTypes")
    public ApiResponse<List<String>> listTestTypes(@RequestParam String workArea){
        if (StringUtils.isEmpty(workArea)){
            return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, null, ErrMsgEnum.INVALID_PARAMETER);
        }
        return ApiResponseUtil.success(indexService.listTestTypes(workArea));
    }

}
