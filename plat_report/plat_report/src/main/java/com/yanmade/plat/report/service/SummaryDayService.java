package com.yanmade.plat.report.service;

import com.yanmade.plat.report.dao.SummaryDayMapper;
import com.yanmade.plat.report.util.DateUtil;
import com.yanmade.plat.report.util.ExcelUtil;
import com.yanmade.plat.report.util.PageUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
@Component
public class SummaryDayService {

	private static final Logger logger = LoggerFactory.getLogger(SummaryDayService.class);

	@Autowired
	private SummaryDayMapper mapper;

	public List<Map<String, Object>> getDayReports(Map<String, Object> map) {
		DateUtil.checkDateParam(map);
		List<String> dateList = mapper.listDayDates(map);
		if (dateList.isEmpty()) {
			return new ArrayList<>();
		}

		// 拼接日期行转列sql
		StringBuilder sBuilder = new StringBuilder();
		for (String date : dateList) {
			sBuilder.append("MAX(case dutyDate WHEN '" + date
					+ "' THEN CONCAT(top1NgItem,'(', top1NgCount, ')<br>',top2NgItem, '(', top2NgCount, ')<br>' ,top3NgItem, '(', top3NgCount, ')') else '' END) as '"
					+ DateUtil.dateFormat(date) + "',");
		}
		String sql = sBuilder.subSequence(0, sBuilder.length() - 1).toString();
		map.put("sql", sql);
		PageUtil.pageMap(map, 0, 20);

		return mapper.listDayReports(map);
	}

	public List<String> getDayDates(Map<String, Object> map) {
		DateUtil.checkDateParam(map);

		List<String> dateList = mapper.listDayDates(map);

		// 对日期进行排序
		Collections.sort(dateList, (s1, s2) -> s1.compareTo(s2));

		// 转换日期格式 "2020-09-09" -> "0909"
		List<String> resultList = new ArrayList<>();
		for (int i = 0; i < dateList.size(); i++) {
			resultList.add(DateUtil.dateFormat(dateList.get(i)));
		}

		return resultList;
	}

	/**
	 * 导出excel文件
	 * @param map
	 * @param response
	 */
	public void exportExcel(Map<String, Object> map, HttpServletResponse response){
		//获取日期表头（已排序）
		List<String> dayDates = getDayDates(map);

		//获取数据
		List<Map<String, Object>> dayReports = getDayReports(map);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();

		ExcelUtil.createExcel(workbook, sheet, dayDates, dayReports);

		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		response.setHeader("Content-Disposition", "attachment;filename=" + startDate + "~" + endDate +"top3Ng.xlsx");
		try(ServletOutputStream out = response.getOutputStream()) {
			workbook.write(out);
			out.flush();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public List<String> listWorkshops(String workArea){
		return mapper.listWorkshops(workArea);
	}

	public List<String> listProductTypes(String workArea){
		return mapper.listProductTypes(workArea);
	}

}
