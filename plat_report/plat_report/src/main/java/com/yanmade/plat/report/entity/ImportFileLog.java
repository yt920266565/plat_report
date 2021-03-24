package com.yanmade.plat.report.entity;

import java.util.Date;

/**
 * 导入文件日志记录表
 */
public class ImportFileLog {

    /**
     * 导入数量
     */
    private int count;

    /**
     * 自动导入或手动导入
     */
    private boolean auto;

    /**
     * 导入人用户名
     */
    private String username;

    /**
     * 导入时间
     */
    private Date createtime;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
