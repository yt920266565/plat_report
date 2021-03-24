package com.yanmade.plat.report.service;

import com.yanmade.plat.report.dao.SummaryHourMapper;
import com.yanmade.plat.report.util.ExcelUtil;
import com.yanmade.plat.report.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
@Component
public class SummaryHourService {

    private static final Logger logger = LoggerFactory.getLogger(SummaryHourService.class);

    private static final String[] DAY_SHIFT = new String[]{"7点", "8点", "9点", "10点", "11点", "12点", "13点", "14点", "15点", "16点",
            "17点", "18点"};

    private static final String[] NIGHT_SHIFT = new String[]{"19点", "20点", "21点", "22点", "23点", "0点", "1点", "2点", "3点", "4点",
            "5点", "6点"};

    @Autowired
    private SummaryHourMapper mapper;

    public List<Map<String, Object>> getHoursReport(Map<String, Object> map) {
        // 检查日期参数是否有传,如果没有传默认取当天日期
        checkDateParam(map);

        List<String> hourList = mapper.listDateHours(map);
        if (hourList.isEmpty()) {
            return new ArrayList<>();
        }

        // 小时行转列
        StringBuilder sBuilder = new StringBuilder();
        for (String hour : hourList) {
            sBuilder.append(
                    "max(case testHour when '" + hour + "' then uphPassCount else '' end) as '" + hour + "点" + "',");
        }
        String sql = sBuilder.subSequence(0, sBuilder.length() - 1).toString();
        map.put("sql", sql);
        PageUtil.pageMap(map, 0, 10);

        List<Map<String, Object>> summaryHours = mapper.listHourReports(map);

        int dayPassCount = 0; // 白班合计
        int nightPassCount = 0; // 晚班合计

        boolean isChart = Boolean.parseBoolean((String) map.get("isChart"));
        if (!isChart){
            sumTotalCount(summaryHours, dayPassCount, nightPassCount);
        }

        return summaryHours;
    }

    /**
     * 按班次合计测试总数
     * @param summaryHours
     * @param dayPassCount
     * @param nightPassCount
     */
    private void sumTotalCount(List<Map<String, Object>> summaryHours, int dayPassCount, int nightPassCount) {
        // 7点-18点 合计白班uph, 19点-6点 合计晚班uph
        for (Map<String, Object> resultMap : summaryHours) {
            for (String dayHour : DAY_SHIFT) {
                if (resultMap.containsKey(dayHour)) {
                    String result = (String)resultMap.get(dayHour);
                    int count = 0;
                    if (StringUtils.isNotEmpty(result)) {
                        count = Integer.parseInt(result);
                    }
                    dayPassCount += count;
                }
            }

            for (String nightHour : NIGHT_SHIFT) {
                if (resultMap.containsKey(nightHour)) {
                    String result = (String) resultMap.get(nightHour);
                    int count = 0;
                    if (StringUtils.isNotEmpty(result)){
                        count = Integer.parseInt(result);
                    }
                    nightPassCount += count;
                }
            }

            resultMap.put("白班合计", dayPassCount);
            resultMap.put("晚班合计", nightPassCount);
        }
    }

    public List<String> getDateHours() {
        String[] hours = new String[]{"7点", "8点", "9点", "10点", "11点", "12点", "13点", "14点", "15点", "16点", "17点", "18点", "白班合计",
                "19点", "20点", "21点", "22点", "23点", "0点", "1点", "2点", "3点", "4点", "5点", "6点", "晚班合计"};

        return Arrays.asList(hours);
    }

    /**
     * 导出excel文件
     * @param map
     * @param response
     */
    public void exportExcel(Map<String, Object> map, HttpServletResponse response){
        //获取有数据的日期（已排序）
        List<String> dutyDates = getDateHours();

        //获取数据
        List<Map<String, Object>> dutyReports = getHoursReport(map);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        ExcelUtil.createExcel(workbook, sheet, dutyDates, dutyReports);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String date = (String) map.get("date");
        response.setHeader("Content-Disposition", "attachment;filename="+ date + "uph.xlsx");
        try(ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public List<String> listWorkshops(Map<String, Object> map){
        return mapper.listWorkshops(map);
    }

    /**
     * 如果未传日期参数，则默认查询昨天的数据
     *
     * @param map
     */
    private void checkDateParam(Map<String, Object> map) {
        String dateKey = "date";
        if (!map.containsKey(dateKey) || (map.containsKey(dateKey) && map.get(dateKey) == null)) {
            map.put(dateKey, LocalDate.now().plusDays(-1).toString()); // 取前一天日期
        }
    }

}
