package com.yanmade.plat.report.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author 0103379
 */
public class DateUtil {

    private DateUtil() {
    }

    /**
     * 输入2020-09-09 输出 0909
     * 
     * @param date
     * @return
     */
    public static String dateFormat(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.format(DateTimeFormatter.ofPattern("MMdd"));
    }

    /**
     * 输入2020-09-09 输出 outputFormat
     *
     * @param date
     * @return
     */
    public static String dateFormat(String date, String outputFormat) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.format(DateTimeFormatter.ofPattern(outputFormat));
    }

    /**
     * 如果未传日期参数，则默认查询最近一周的数据
     *
     * @param map
     */
    public static void checkDateParam(Map<String, Object> map) {
        String startDateKey = "startDate";
        String endDateKey= "endDate";

        String startDate = (String)map.get(startDateKey);
        if (StringUtils.isEmpty(startDate)) {
            // startDate设置为7天前
            map.put(startDateKey, LocalDate.now().plusDays(-7).toString());
        }

        String endDate = (String)map.get(endDateKey);
        if (StringUtils.isEmpty(endDate)) {
            //endDate设置为昨天
            map.put(endDateKey, LocalDate.now().plusDays(-1).toString());
        }
    }

}
