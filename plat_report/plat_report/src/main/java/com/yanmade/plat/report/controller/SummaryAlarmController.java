package com.yanmade.plat.report.controller;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.report.entity.SummaryAlarmQueryDto;
import com.yanmade.plat.report.entity.SummaryDuty;
import com.yanmade.plat.report.service.SummaryAlarmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
@RestController
public class SummaryAlarmController {

    @Autowired
    private SummaryAlarmService service;

    @GetMapping("/summary/alarm")
    public ApiResponse<List<SummaryDuty>> listSummaryAlarm(SummaryAlarmQueryDto summaryAlarmQueryDto){
        List<SummaryDuty> summaryDuties = service.listSummaryAlarm(summaryAlarmQueryDto);
        int count = service.countSummaryAlarm(summaryAlarmQueryDto);
        return ApiResponseUtil.success(summaryDuties, count);
    }

    /**
     * 单个机台一段日期top3告警饼状图
     * @param map
     * @return
     */
    @GetMapping("/alarm/machine/days/total")
    public ApiResponse<List<Map<String, Object>>> listSingleMachineTop3Alarm(@RequestParam Map<String, String> map){
        if (StringUtils.isEmpty(map.get("machineId")) || StringUtils.isEmpty(map.get("startDate")) || StringUtils.isEmpty(map.get("endDate"))){
            return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, null, ErrMsgEnum.INVALID_PARAMETER);
        }
        List<Map<String, Object>> maps = service.listSingleMachineMultipleDaysTop3AlarmTotal(map);
        return ApiResponseUtil.success(maps);
    }

    /**
     * 多个机台一天的top3告警对比
     * @param summaryAlarmQueryDto
     * @return
     */
    @GetMapping("/alarm/machines/day")
        public ApiResponse<List<SummaryDuty>> listTop3Alarm(SummaryAlarmQueryDto summaryAlarmQueryDto){
        List<SummaryDuty> entries = service.listMultipleMachineSingleDayTop3Alarm(summaryAlarmQueryDto);
        return ApiResponseUtil.success(entries);
    }
}
