package com.yanmade.plat.report.service;

import com.yanmade.plat.report.dao.ImportFileLogMapper;
import com.yanmade.plat.report.entity.ImportFileLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
@Component
public class ImportFileLogService {

    @Autowired
    private ImportFileLogMapper mapper;

    public boolean insertLog(ImportFileLog importFileLog){
        return mapper.insertLog(importFileLog);
    }

    public List<ImportFileLog> selectLog(int count){
        return mapper.selectLog(count);
    }
    
    public List<ImportFileLog> getAllLog(Map<String, Object> map){
    	pageMap(map);
    	return mapper.getAllLog(map);
    }
    
    private void pageMap(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
	}
}
