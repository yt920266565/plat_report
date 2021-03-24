package com.yanmade.plat.report.service;

import com.yanmade.plat.report.dao.SummaryDayMapper;
import com.yanmade.plat.report.entity.WorkAreaDto;
import com.yanmade.plat.report.entity.SummaryDay;
import com.yanmade.plat.report.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 0103379
 */
@Component
public class IndexService {

    @Autowired
    private SummaryDayMapper summaryDayMapper;

    @Value("${workAreaName}")
    private String workAreaName;

    public List<WorkAreaDto> listWorkAreas(){
        List<WorkAreaDto> workAreaDtos = summaryDayMapper.listWorkAreas();
        return workAreaDtos;
    }

    public List<SummaryDay> getPassCount(Map<String, Object> map){
        //取前7天数据
        DateUtil.checkDateParam(map);

        List<SummaryDay> summaryDays = summaryDayMapper.getPassCountByProductType(map);
        List<SummaryDay> list = new ArrayList<>();
        for (SummaryDay summaryDay : summaryDays) {
            //根据pass数和总数计算良率
            int totalCount = summaryDay.getTotalCount();
            int passCount = summaryDay.getFirstPassCount();
            //保留两位小数
            DecimalFormat df = new DecimalFormat("0.00");
            String ratio = df.format(totalCount == 0 ? 0 : (float) (passCount * 100.0 / totalCount));
            float firstPassRatio = Float.parseFloat(ratio);
            summaryDay.setFirstPassRatio(firstPassRatio);
            list.add(summaryDay);
        }

        return list;
    }

    /**
     * 根据配置文件获取客户区域
     * @return
     */
    public List<String> getWorkAreaNames(){
        String replace = workAreaName.replaceAll("，", ",");
        String[] split = replace.split(",");
        List<String> workAreaNames = Arrays.asList(split);
        return workAreaNames;
    }

    public List<String> listPartNames(String workArea){
        return summaryDayMapper.listPartNames(workArea);
    }

    public List<String> listTestTypes(String workArea){
        return summaryDayMapper.listTestTypes(workArea);
    }

}
