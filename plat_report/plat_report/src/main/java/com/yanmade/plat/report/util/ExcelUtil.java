package com.yanmade.plat.report.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    private ExcelUtil(){
    }

    public static void createExcel(XSSFWorkbook workbook, XSSFSheet sheet, List<String> titles, List<Map<String, Object>> datas){
        //有数据的日期列数
        int columnLength = titles.size();

        CellStyle style = getCellStyle(workbook);

        //设置表头行
        XSSFRow titleRow = sheet.createRow(0);

        XSSFCell cell1 = titleRow.createCell(0);
        cell1.setCellValue("机台名称");
        cell1.setCellStyle(style);

        XSSFCell cell2 = titleRow.createCell(1);
        cell2.setCellValue("料号");
        cell2.setCellStyle(style);

        XSSFCell cell3 = titleRow.createCell(2);
        cell3.setCellValue("车间");
        cell3.setCellStyle(style);

        for (int i = 0; i < columnLength; i++) {
            XSSFCell cell = titleRow.createCell(i + 3);
            cell.setCellStyle(style);
            cell.setCellValue(titles.get(i));
        }

        //遍历数据
        for (int i = 0; i < datas.size(); i++) {
            //行数据
            Map<String, Object> stringObjectMap = datas.get(i);

            //从第二行开始
            XSSFRow row = sheet.createRow(i + 1);

            row.createCell(0).setCellValue((String) stringObjectMap.get("machineId"));
            row.createCell(1).setCellValue((String) stringObjectMap.get("partName"));
            row.createCell(2).setCellValue((String) stringObjectMap.get("workArea"));

            //获取表头迭代器
            Iterator<Cell> cellIterator = titleRow.cellIterator();
            int j = 0;
            //遍历表头行， 通过表头取出对应列的数据
            while (cellIterator.hasNext()){
                //先取数据，让游标指向下一个
                String key = cellIterator.next().getStringCellValue();
                if (j < 3){ // 前三列已经设置
                    j++;
                    continue;
                }
                String value = String.valueOf(stringObjectMap.get(key));
                row.createCell(j++).setCellValue(value);
            }
        }
    }

    public static CellStyle getCellStyle(XSSFWorkbook workbook) {
        //创建样式
        CellStyle style = workbook.createCellStyle();

//		style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        //设置自动换行
        style.setWrapText(true);

        //水平对齐方式（居中）
        style.setAlignment(HorizontalAlignment.CENTER);
        //垂直对齐方式（居中）
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //创建字体设置
        Font baseFont = workbook.createFont();
        //字体
        baseFont.setFontName("黑体");
        //大小
        baseFont.setFontHeightInPoints((short) 13);
        style.setFont(baseFont);
        return style;
    }
}
