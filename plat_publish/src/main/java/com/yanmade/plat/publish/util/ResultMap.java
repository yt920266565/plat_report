package com.yanmade.plat.publish.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMap {
    private ResultMap() {
    	
    }
    
    public static Map<String, Object> data(List<Map<String, Object>> list,int count){
    	Map<String, Object> resMap = new HashMap<>();
    	resMap.put("data", list);
    	resMap.put("count", count);
    	resMap.put("code", 0);
    	return resMap;
    }
    
}
