package com.yanmade.plat.report.dao;

import com.yanmade.plat.report.entity.SummaryAlarmQueryDto;
import com.yanmade.plat.report.entity.SummaryDuty;

import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
public interface SummaryAlarmMapper {

    /**
     * 查询top3告警（根据班次报表）
     * @param queryDto
     * @return
     */
    public List<SummaryDuty> listSummaryAlarmBySummaryDuty(SummaryAlarmQueryDto queryDto);

    /**
     * 查询top3告警接口返回条数(根据班次报表)
     * @param queryDto
     * @return
     */
    public int countSummaryAlarmBySummaryDuty(SummaryAlarmQueryDto queryDto);

    /**
     * 查询一台机器跨日期的top3告警(合计)
     * @param map
     * @return
     */
    public List<SummaryDuty> listSingleMachineMultipleDaysTop3AlarmTotal(Map<String, String> map);

//    /**
//     * 查询多个机器某一天的top3告警
//     * @param map
//     * @return
//     */
//    public List<SummaryDuty> listMultipleMachinesSingleDayTop3Alarm(Map<String, String> map);
}

