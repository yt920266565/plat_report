package com.yanmade.plat.report.entity;

public class ImportFileDto {

    //文件夹路径
    private String folderPath;

    //已经存在的是否更新
    private boolean update;

    //开始日期
    private String startDate;

    //结束日期
    private String endDate;

    //按天导入
    private boolean byDay;

    //按班次导入
    private boolean byDuty;

    //按小时导入
    private boolean byHour;

    //导入类型（自动/手动）
    private boolean auto;

    //客户区域
    private String workArea;

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isByDay() {
        return byDay;
    }

    public void setByDay(boolean byDay) {
        this.byDay = byDay;
    }

    public boolean isByDuty() {
        return byDuty;
    }

    public void setByDuty(boolean byDuty) {
        this.byDuty = byDuty;
    }

    public boolean isByHour() {
        return byHour;
    }

    public void setByHour(boolean byHour) {
        this.byHour = byHour;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }
}
