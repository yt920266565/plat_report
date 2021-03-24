package com.yanmade.plat.report.dao;

import com.yanmade.plat.report.entity.SummaryDay;
import com.yanmade.plat.report.entity.SummaryDuty;
import com.yanmade.plat.report.entity.SummaryHour;

import java.util.List;

/**
 * @author 0103379
 */
public interface ImportFileMapper {

    /**
     * 批量插入按天汇总数据
     * @param list
     * @return
     */
    boolean insertBatchSummaryDay(List<SummaryDay> list);

    /**
     * 批量插入班次汇总数据
     * @param list
     * @return
     */
    boolean insertBatchSummaryDuty(List<SummaryDuty> list);

    /**
     * 批量插入按小时汇总数据
     * @param list
     * @return
     */
    boolean insertBatchSummaryHour(List<SummaryHour> list);

}
