package com.yanmade.plat.report.controller;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.report.dao.SummaryDayMapper;
import com.yanmade.plat.report.service.SummaryDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class SummaryDayController {

	@Autowired
    private SummaryDayService service;

    @Autowired
    private SummaryDayMapper mapper;

    @GetMapping("/day/report")
	public ApiResponse<List<Map<String, Object>>> getDutyReports(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> dutyReports = service.getDayReports(map);
        int count = mapper.countDayReportsCnt(map);
		return ApiResponseUtil.success(dutyReports,count);
	}

    @GetMapping("/day/title")
    public List<String> getDutyDate(@RequestParam Map<String, Object> map) {
        return service.getDayDates(map);
    }

    @GetMapping("/top3/excel")
    public void exportExcel(@RequestParam Map<String, Object> map, HttpServletResponse response){
        service.exportExcel(map, response);
    }

    @GetMapping("/top3/workshop")
    public ApiResponse<List<String>> getWorkshop(@RequestParam(required = false) String workArea){
        return ApiResponseUtil.success(service.listWorkshops(workArea));
    }

}
