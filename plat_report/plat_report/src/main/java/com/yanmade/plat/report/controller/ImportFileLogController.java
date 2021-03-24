package com.yanmade.plat.report.controller;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.report.dao.ImportFileLogMapper;
import com.yanmade.plat.report.entity.ImportFileLog;
import com.yanmade.plat.report.service.ImportFileLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
@RestController
public class ImportFileLogController {

    @Autowired
    private ImportFileLogService service;
    
    @Autowired
    private ImportFileLogMapper mapper;

    @GetMapping("/import/log")
    public ApiResponse<Object> getLog(@RequestParam int count){
        if (count <= 0 ){
            return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, "请求参数不正确", ErrMsgEnum.INVALID_PARAMETER );
        }
        List<ImportFileLog> importFileLogs = service.selectLog(count);
        if (importFileLogs == null) {
            return ApiResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误", ErrMsgEnum.FAILURE);
        }

        return ApiResponseUtil.success(importFileLogs);
    }
    
    @GetMapping("/import/allLog")
    public ApiResponse<List<ImportFileLog>> getallLog(@RequestParam Map<String, Object> map){
    	List<ImportFileLog> list = service.getAllLog(map);
    	int count = mapper.getAllLogCnt();
    	return ApiResponseUtil.success(list, count);
    }
}

