package com.yanmade.plat.report.controller;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.report.dao.SummaryHourMapper;
import com.yanmade.plat.report.service.SummaryDayService;
import com.yanmade.plat.report.service.SummaryHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
@RestController
public class SummaryHourController {

    @Autowired
    private SummaryHourService service;

    @Autowired
    private SummaryHourMapper mapper;

    @Autowired
    private SummaryDayService summaryDayService;

    @GetMapping("/hour/report")
    public ApiResponse<List<Map<String, Object>>> getHourReport(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> hoursReport = service.getHoursReport(map);
        int count = mapper.countHoursReportCnt(map);
        return ApiResponseUtil.success(hoursReport, count);
    }

    @GetMapping("/hour/title")
    public List<String> getDutyDate() {
        return service.getDateHours();
    }

    @GetMapping("/totalcount/excel")
    public void exportExcel(@RequestParam Map<String, Object> map, HttpServletResponse response){
        service.exportExcel(map, response);
    }

    @GetMapping("/totalcount/workshop")
    public ApiResponse<List<String>> getWorkArea(@RequestParam(required = false) String workArea){
        return ApiResponseUtil.success(summaryDayService.listWorkshops(workArea));
    }
}
