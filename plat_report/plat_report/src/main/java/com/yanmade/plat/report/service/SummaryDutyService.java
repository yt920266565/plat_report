package com.yanmade.plat.report.service;

import com.yanmade.plat.report.dao.SummaryDutyMapper;
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
public class SummaryDutyService {

    private static final Logger logger = LoggerFactory.getLogger(SummaryDutyService.class);

    @Autowired
    private SummaryDutyMapper mapper;

    public List<Map<String, Object>> getDutyReports(Map<String, Object> map) {
        DateUtil.checkDateParam(map);
        List<String> titleList = mapper.listDutyDates(map);
        if (titleList.isEmpty()) {
            return new ArrayList<>();
        }

        // 日期行转列
        StringBuilder sBuilder = new StringBuilder();
        for (String title : titleList) {
            sBuilder.append("max(case concat(dutyDate, ',', duty) when '" + title
                    + "' then CONCAT(firstPassRatio,'%') else '' end) as '" + transform(title)
                    + "',");
        }
        String sql = sBuilder.subSequence(0, sBuilder.length() - 1).toString();
        map.put("sql", sql);
        PageUtil.pageMap(map, 0 , 20);

        return mapper.listDutyReports(map);
    }

    public List<String> getDutyDates(Map<String, Object> map) {
        DateUtil.checkDateParam(map);

        List<String> titleList = mapper.listDutyDates(map);

        // 对日期进行排序
        Collections.sort(titleList, (s1, s2) -> {
            String[] arr1 = s1.split(",");
            String[] arr2 = s2.split(",");

            int value = arr1[0].compareTo(arr2[0]);
            // 如果日期一致,按班次排序
            if (value == 0) {
                value = arr2[1].compareTo(arr1[1]);
            }
            return value;
        });

        List<String> resultList = new ArrayList<>();
        // 转换表头格式 "2020-09-09,0" -> "0909晚"
        for (String title : titleList) {
            resultList.add(transform(title));
        }

        return resultList;
    }

    /**
     * 导出excel文件
     * @param map
     * @param response
     */
    public void exportExcel(Map<String, Object> map, HttpServletResponse response){
        //获取有数据的日期（已排序）
        List<String> dutyDates = getDutyDates(map);

        //获取数据
        List<Map<String, Object>> dutyReports = getDutyReports(map);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        ExcelUtil.createExcel(workbook, sheet, dutyDates, dutyReports);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");
        response.setHeader("Content-Disposition", "attachment;filename="+ startDate + "~" + endDate + "fcy.xlsx");
        try (ServletOutputStream out = response.getOutputStream()){
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public List<String> listWorkshops(Map<String, Object> map){
        return mapper.listWorkshops(map);
    }

    private String transform(String title) {
        String[] titles = title.split(",");
        if ("0".equals(titles[1])) {
            return DateUtil.dateFormat(titles[0]) + "晚";
        } else {
            return DateUtil.dateFormat(titles[0]) + "白";
        }
    }

}
