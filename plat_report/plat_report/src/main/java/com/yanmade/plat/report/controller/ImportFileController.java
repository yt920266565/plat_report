package com.yanmade.plat.report.controller;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.report.entity.ImportFileDto;
import com.yanmade.plat.report.service.ImportFileService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author 0103379
 */
@RestController
public class ImportFileController {

    @Autowired
    private ImportFileService service;

    @Value("${import.path}")
    private String configPath;

    @PostMapping("/file/folder")
    public ApiResponse<String> importFileByFolderPath(@RequestBody ImportFileDto dto){
        String message = service.importFileByFolderPath(dto);
        if (message.isEmpty()){
            return ApiResponseUtil.success(message);
        }
        return ApiResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR, message, ErrMsgEnum.FAILURE);
    }

    @GetMapping("/config/path")
    public String getConfigPath(){
        return configPath;
    }
    
    @PostMapping("/importFile/upload")
    public ApiResponse<String> uploadZipFile(HttpServletRequest request) {
    	if(!service.uploadZipFile(request, configPath)) {
    		return ApiResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR, "", ErrMsgEnum.FAILURE);
    	}
    	return ApiResponseUtil.success("");
    }

}
