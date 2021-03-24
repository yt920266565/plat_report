package com.yanmade.plat.report.entity;

/**
 * @author 0103379
 */
public class ImportResultDto {

    /**
     * 导入结果
     */
    private boolean success;

    /**
     * 导入数据条数
     */
    private int count;

    /**
     * 错误信息
     */
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
