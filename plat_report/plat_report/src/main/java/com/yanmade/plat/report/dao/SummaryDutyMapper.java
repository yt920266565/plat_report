package com.yanmade.plat.report.dao;

import com.yanmade.plat.report.entity.SummaryDuty;

import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
public interface SummaryDutyMapper {

    /**
     * 获取良率报表
     * @param map
     * @return
     */
    List<Map<String, Object>> listDutyReports(Map<String, Object> map);

    /**
     * 获取日期
     * @param map
     * @return
     */
    List<String> listDutyDates(Map<String, Object> map);

    /**
     * 计算良率报表数据条数
     * @param map
     * @return
     */
	int countDutyReportsCnt(Map<String, Object> map);

    /**
     * 查询该条数据是否存在
     * @param summaryDuty
     * @return
     */
	int countSummaryDuty(SummaryDuty summaryDuty);

    /**
     * 根据查询条件列出所有车间
     * @return
     */
    List<String> listWorkshops(Map<String, Object> map);
}
