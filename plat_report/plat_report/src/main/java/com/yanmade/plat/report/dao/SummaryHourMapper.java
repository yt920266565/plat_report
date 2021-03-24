package com.yanmade.plat.report.dao;

import com.yanmade.plat.report.entity.SummaryHour;

import java.util.List;
import java.util.Map;

public interface SummaryHourMapper {

    /**
     * 获取测试总数报表
     * @param map
     * @return
     */
    List<Map<String, Object>> listHourReports(Map<String, Object> map);

    /**
     * 获取小时列表
     * @param map
     * @return
     */
    List<String> listDateHours(Map<String, Object> map);

    /**
     * 获取测试总数报表数据条数
     * @param map
     * @return
     */
    int countHoursReportCnt(Map<String, Object> map);

    /**
     * 查询该条数据是否存在
     * @param summaryHour
     * @return
     */
    int countSummaryHour(SummaryHour summaryHour);

    /**
     * 查询车间
     * @param map
     * @return
     */
    List<String> listWorkshops(Map<String, Object> map);
}
