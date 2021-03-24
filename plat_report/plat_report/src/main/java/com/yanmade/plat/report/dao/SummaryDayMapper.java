package com.yanmade.plat.report.dao;

import com.yanmade.plat.report.entity.WorkAreaDto;
import com.yanmade.plat.report.entity.SummaryDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
public interface SummaryDayMapper {

    /**
     * 查询top3不良数据
     * @param map
     * @return
     */
    List<Map<String, Object>> listDayReports(Map<String, Object> map);

    /**
     * 获取日期
     * @param map
     * @return
     */
    List<String> listDayDates(Map<String, Object> map);

    /**
     * 查询top3不良数据条数
     * @param map
     * @return
     */
    int countDayReportsCnt(Map<String, Object> map);

    /**
     * 查询当前数据是否存在
     * @param summaryDay
     * @return
     */
    int countSummaryDay(SummaryDay summaryDay);

    /**
     * 根据查询条件列出所有车间
     * @return
     */
    List<String> listWorkshops(@Param("workArea")String workArea);

    /**
     * 列出所有客户区域以及机台数量
     * @return
     */
    List<WorkAreaDto> listWorkAreas();

    /**
     * 获取良率，pass总数
     * @param map
     * @return
     */
    List<SummaryDay> getPassCountByProductType(Map<String, Object> map);

    /**
     * 列出所有产品类型
     * @return
     */
    List<String> listProductTypes(String workArea);

    /**
     * 列出所有料号
     * @return
     */
    List<String> listPartNames(String workArea);

    /**
     * 列出所有测试类型
     * @return
     */
    List<String> listTestTypes(String workArea);
}
