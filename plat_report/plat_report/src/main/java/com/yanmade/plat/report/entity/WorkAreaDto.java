package com.yanmade.plat.report.entity;

/**
 * @author 0103379
 */
public class WorkAreaDto {

    /**
     * 客户区域
     */
    private String workArea;

    /**
     * 机台数量
     */
    private int machineCount;

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public int getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(int machineCount) {
        this.machineCount = machineCount;
    }
}
