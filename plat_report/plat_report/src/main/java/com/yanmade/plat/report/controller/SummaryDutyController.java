package com.yanmade.plat.report.controller;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.report.dao.SummaryDutyMapper;
import com.yanmade.plat.report.service.SummaryDayService;
import com.yanmade.plat.report.service.SummaryDutyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class SummaryDutyController {

	@Autowired
    private SummaryDutyService service;

	@Autowired
	private SummaryDayService summaryDayService;

    @Autowired
    private SummaryDutyMapper mapper;

    @GetMapping("/duty/report")
	public ApiResponse<List<Map<String, Object>>> getDutyReports(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> dutyReports = service.getDutyReports(map);
		int count = mapper.countDutyReportsCnt(map);
		return ApiResponseUtil.success(dutyReports, count);
	}

    @GetMapping("/duty/title")
    public List<String> getDutyDate(@RequestParam Map<String, Object> map) {
        return service.getDutyDates(map);
    }

    @GetMapping("/fcy/excel")
    public void exportExcel(@RequestParam Map<String, Object> map, HttpServletResponse response){
        service.exportExcel(map, response);
    }

    @GetMapping("/fcy/workshop")
    public ApiResponse<List<String>> getWorkArea(@RequestParam(required = false) String workArea){
        //取按天统计报表的车间（数据量少）
        return ApiResponseUtil.success(summaryDayService.listWorkshops(workArea));
    }
}
