package com.yanmade.plat.report.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PageUtil {
    private static final Logger logger = LoggerFactory.getLogger(PageUtil.class);

    private PageUtil(){}

    /**
     * 如果没传page,limit参数，默认给一个值
     * @param map
     * @param page
     * @param limit
     * @return
     */
    public static Map<String, Object> pageMap(Map<String, Object> map, int page, int limit) {
        try {
            page = Integer.parseInt(map.get("page").toString());
            limit = Integer.parseInt(map.get("limit").toString());
        } catch (Exception e) {
            logger.error("", e);
        }
        if (page > 0) {
            page = (page - 1) * limit;
        }
        map.put("page", page);
        map.put("limit", limit);
        return map;
    }
}
