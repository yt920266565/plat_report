package com.yanmade.plat.report.service;

import com.yanmade.plat.report.dao.SummaryAlarmMapper;
import com.yanmade.plat.report.entity.SummaryAlarmQueryDto;
import com.yanmade.plat.report.entity.SummaryDuty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 0103379
 */
@Component
public class SummaryAlarmService {

    @Autowired
    private SummaryAlarmMapper mapper;

    public List<SummaryDuty> listSummaryAlarm(SummaryAlarmQueryDto queryDto){
        if (queryDto.getLimit() <= 0){
            queryDto.setLimit(20);
        }
        
        queryDto.setPage((queryDto.getPage() - 1) * queryDto.getLimit()); 

        //没传日期默认取昨天
        if (StringUtils.isEmpty(queryDto.getDate())){
            queryDto.setDate(LocalDate.now().plusDays(-1).toString());
        }

        return mapper.listSummaryAlarmBySummaryDuty(queryDto);
    }

    public int countSummaryAlarm(SummaryAlarmQueryDto queryDto){
        return mapper.countSummaryAlarmBySummaryDuty(queryDto);
    }

    /**
     * 查询一台机器一段日期内数量前三的告警
     * @param map
     * @return
     */
    public List<Map<String, Object>> listSingleMachineMultipleDaysTop3AlarmTotal(Map<String, String> map){
        List<SummaryDuty> summaryDuties = mapper.listSingleMachineMultipleDaysTop3AlarmTotal(map);

        Map<String, Integer> top3AlarmMap = new HashMap<>();

        //汇总告警项的告警数
        for (SummaryDuty summaryDuty : summaryDuties) {
            String top1AlarmItem = summaryDuty.getTop1AlarmItem();
            Integer top1AlarmCount = summaryDuty.getTop1AlarmCount();

            putAlarmCount(top3AlarmMap, top1AlarmItem, top1AlarmCount);

            String top2AlarmItem = summaryDuty.getTop2AlarmItem();
            Integer top2AlarmCount = summaryDuty.getTop2AlarmCount();
            putAlarmCount(top3AlarmMap, top2AlarmItem, top2AlarmCount);

            String top3AlarmItem = summaryDuty.getTop3AlarmItem();
            Integer top3AlarmCount = summaryDuty.getTop3AlarmCount();
            putAlarmCount(top3AlarmMap, top3AlarmItem, top3AlarmCount);
        }

        //取告警数前三的告警项
        Set<Map.Entry<String, Integer>> entries = top3AlarmMap.entrySet();
        List<Map.Entry<String, Integer>> sortedList;
        if (entries.size() < 3){
            sortedList = entries.stream().sorted((entry1, entry2) -> {
                return entry2.getValue() - entry1.getValue();
            }).collect(Collectors.toList());
        } else {
            sortedList = entries.stream().sorted((entry1, entry2) -> {
                return entry2.getValue() - entry1.getValue();
            }).limit(3).collect(Collectors.toList());
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, Integer> stringIntegerEntry : sortedList) {
            Map<String, Object> objectMap = new HashMap<>(2);
            objectMap.put("alarmItem", stringIntegerEntry.getKey());
            objectMap.put("alarmCount", stringIntegerEntry.getValue());
            list.add(objectMap);
        }

        return list;
    }

    /**
     * 往map里put值，如果key存在则累加value
     * @param top3AlarmMap
     * @param top1AlarmItem
     * @param top1AlarmCount
     */
    private void putAlarmCount(Map<String, Integer> top3AlarmMap, String top1AlarmItem, Integer top1AlarmCount) {
        //如果等于null初始化为0
        top1AlarmCount = top1AlarmCount == null ? 0 : top1AlarmCount;
        if (!top3AlarmMap.containsKey(top1AlarmItem)){
            top3AlarmMap.put(top1AlarmItem, top1AlarmCount);
        } else {
            top3AlarmMap.put(top1AlarmItem, top3AlarmMap.get(top1AlarmItem) + top1AlarmCount);
        }
    }

    /**
     * 查询多台机器当天内数量前三的告警
     * @param queryDto
     * @return
     */
    public List<SummaryDuty> listMultipleMachineSingleDayTop3Alarm(SummaryAlarmQueryDto queryDto){
        List<SummaryDuty> summaryDuties = listSummaryAlarm(queryDto);

        Map<String, SummaryDuty> top3AlarmMap = new HashMap<>();

        //合计各个机台白班和晚班的top3告警
        for (SummaryDuty summaryDuty : summaryDuties) {
            String machineId = summaryDuty.getMachineId();
            if (!top3AlarmMap.containsKey(machineId)){
                top3AlarmMap.put(machineId, summaryDuty);
            } else {
                SummaryDuty summaryDutyExist = top3AlarmMap.get(machineId);

                //将两个班次的告警都放入map中（如果存在key则累加value）
                Map<String, Integer> alarmMap = new HashMap<>();
                putAlarmCount(alarmMap, summaryDutyExist.getTop1AlarmItem(), summaryDutyExist.getTop1AlarmCount());
                putAlarmCount(alarmMap, summaryDutyExist.getTop2AlarmItem(), summaryDutyExist.getTop2AlarmCount());
                putAlarmCount(alarmMap, summaryDutyExist.getTop3AlarmItem(), summaryDutyExist.getTop3AlarmCount());

                putAlarmCount(alarmMap, summaryDuty.getTop1AlarmItem(), summaryDuty.getTop1AlarmCount());
                putAlarmCount(alarmMap, summaryDuty.getTop2AlarmItem(), summaryDuty.getTop2AlarmCount());
                putAlarmCount(alarmMap, summaryDuty.getTop3AlarmItem(), summaryDuty.getTop3AlarmCount());

                //取告警数前三的告警项
                Set<Map.Entry<String, Integer>> entries = alarmMap.entrySet();
                List<Map.Entry<String, Integer>> sortedList;
                if (entries.size() < 3){
                    sortedList = entries.stream().sorted((entry1, entry2) -> {
                        return entry2.getValue() - entry1.getValue();
                    }).collect(Collectors.toList());
                } else {
                    sortedList = entries.stream().sorted((entry1, entry2) -> {
                        return entry2.getValue() - entry1.getValue();
                    }).limit(3).collect(Collectors.toList());
                }

                if (!sortedList.isEmpty()){
                    Map.Entry<String, Integer> stringIntegerEntry = sortedList.get(0);
                    summaryDutyExist.setTop1AlarmItem(stringIntegerEntry.getKey());
                    summaryDutyExist.setTop1AlarmCount(stringIntegerEntry.getValue());
                }

                if (sortedList.size() > 1){
                    Map.Entry<String, Integer> stringIntegerEntry1 = sortedList.get(1);
                    summaryDutyExist.setTop2AlarmItem(stringIntegerEntry1.getKey());
                    summaryDutyExist.setTop2AlarmCount(stringIntegerEntry1.getValue());
                }

                if (sortedList.size() > 2){
                    Map.Entry<String, Integer> stringIntegerEntry2 = sortedList.get(2);
                    summaryDutyExist.setTop3AlarmItem(stringIntegerEntry2.getKey());
                    summaryDutyExist.setTop3AlarmCount(stringIntegerEntry2.getValue());
                }
            }
        }

        return new ArrayList<>(top3AlarmMap.values());
    }
}
