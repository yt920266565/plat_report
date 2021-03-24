package com.yanmade.plat.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author 0103379
 */
public class SummaryDay {
    /**
     * 设备ip
     */
    private String ip;
    /**
     * 客户区域
     */
    private String workArea;

    /**
     * 车间
     */
    private String workshop;

    /**
     *产品类型
     */
    private String productType;
    /**
     * 料号
     */
    private String partName;
    /**
     * 测试类型
     */
    private String testType;
    /**
     * 设备类型(自动化/手动)
     */
    private String machineType;
    /**
     * 治具编号
     */
    private String machineId;
    /**
     * 班次日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dutyDate;
    /**
     * 待料时间（单位s）
     */
    private Long waitDuaration;
    /**
     * 告警停机时间（单位s）
     */
    private Long stopDuaration;
    /**
     * 总测试数量
     */
    private Integer totalCount;
    /**
     * 首测良率（%）
     */
    private Float firstPassRatio;
    /**
     * 复测良率(%)
     */
    private Float retestPassRatio;
    /**
     * 首测总数
     */
    private Integer firstTotalCount;
    /**
     * 首测pass数量
     */
    private Integer firstPassCount;
    /**
     * 复测总数
     */
    private Integer retestTotalCount;
    /**
     * 复测pass数量
     */
    private Integer retestPassCount;
    /**
     * top1Ng项
     */
    private String top1NgItem;
    /**
     * top1Ng数量
     */
    private Integer top1NgCount;
    /**
     * top2Ng项
     */
    private String top2NgItem;
    /**
     * top2Ng数量
     */
    private Integer top2NgCount;
    /**
     * top3Ng项
     */
    private String top3NgItem;
    /**
     * top3Ng数量
     */
    private Integer top3NgCount;
    /**
     * top1告警项
     */
    private String top1AlarmItem;
    /**
     * top1告警数
     */
    private Integer top1AlarmCount;
    /**
     * top2告警项
     */
    private String top2AlarmItem;
    /**
     * top2告警数
     */
    private Integer top2AlarmCount;
    /**
     *  top3告警项
     */
    private String top3AlarmItem;
    /**
     * top3告警数
     */
    private Integer top3AlarmCount;
    /**
     * 创建时间
     */
    private Date createtime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public Date getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    public Long getWaitDuaration() {
        return waitDuaration;
    }

    public void setWaitDuaration(Long waitDuaration) {
        this.waitDuaration = waitDuaration;
    }

    public Long getStopDuaration() {
        return stopDuaration;
    }

    public void setStopDuaration(Long stopDuaration) {
        this.stopDuaration = stopDuaration;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Float getFirstPassRatio() {
        return firstPassRatio;
    }

    public void setFirstPassRatio(Float firstPassRatio) {
        this.firstPassRatio = firstPassRatio;
    }

    public Float getRetestPassRatio() {
        return retestPassRatio;
    }

    public void setRetestPassRatio(Float retestPassRatio) {
        this.retestPassRatio = retestPassRatio;
    }

    public Integer getFirstTotalCount() {
        return firstTotalCount;
    }

    public void setFirstTotalCount(Integer firstTotalCount) {
        this.firstTotalCount = firstTotalCount;
    }

    public Integer getFirstPassCount() {
        return firstPassCount;
    }

    public void setFirstPassCount(Integer firstPassCount) {
        this.firstPassCount = firstPassCount;
    }

    public Integer getRetestTotalCount() {
        return retestTotalCount;
    }

    public void setRetestTotalCount(Integer retestTotalCount) {
        this.retestTotalCount = retestTotalCount;
    }

    public Integer getRetestPassCount() {
        return retestPassCount;
    }

    public void setRetestPassCount(Integer retestPassCount) {
        this.retestPassCount = retestPassCount;
    }

    public String getTop1NgItem() {
        return top1NgItem;
    }

    public void setTop1NgItem(String top1NgItem) {
        this.top1NgItem = top1NgItem;
    }

    public Integer getTop1NgCount() {
        return top1NgCount;
    }

    public void setTop1NgCount(Integer top1NgCount) {
        this.top1NgCount = top1NgCount;
    }

    public String getTop2NgItem() {
        return top2NgItem;
    }

    public void setTop2NgItem(String top2NgItem) {
        this.top2NgItem = top2NgItem;
    }

    public Integer getTop2NgCount() {
        return top2NgCount;
    }

    public void setTop2NgCount(Integer top2NgCount) {
        this.top2NgCount = top2NgCount;
    }

    public String getTop3NgItem() {
        return top3NgItem;
    }

    public void setTop3NgItem(String top3NgItem) {
        this.top3NgItem = top3NgItem;
    }

    public Integer getTop3NgCount() {
        return top3NgCount;
    }

    public void setTop3NgCount(Integer top3NgCount) {
        this.top3NgCount = top3NgCount;
    }

    public String getTop1AlarmItem() {
        return top1AlarmItem;
    }

    public void setTop1AlarmItem(String top1AlarmItem) {
        this.top1AlarmItem = top1AlarmItem;
    }

    public Integer getTop1AlarmCount() {
        return top1AlarmCount;
    }

    public void setTop1AlarmCount(Integer top1AlarmCount) {
        this.top1AlarmCount = top1AlarmCount;
    }

    public String getTop2AlarmItem() {
        return top2AlarmItem;
    }

    public void setTop2AlarmItem(String top2AlarmItem) {
        this.top2AlarmItem = top2AlarmItem;
    }

    public Integer getTop2AlarmCount() {
        return top2AlarmCount;
    }

    public void setTop2AlarmCount(Integer top2AlarmCount) {
        this.top2AlarmCount = top2AlarmCount;
    }

    public String getTop3AlarmItem() {
        return top3AlarmItem;
    }

    public void setTop3AlarmItem(String top3AlarmItem) {
        this.top3AlarmItem = top3AlarmItem;
    }

    public Integer getTop3AlarmCount() {
        return top3AlarmCount;
    }

    public void setTop3AlarmCount(Integer top3AlarmCount) {
        this.top3AlarmCount = top3AlarmCount;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
