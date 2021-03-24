package com.yanmade.plat.publish.entity;

import java.util.Date;

public class VerTest {

	private String flowNo; // Ver_operation.flowno

	private String mainFlowNo; // Ver_detail.flowno

	private int tester; // 测试人

	private int result; // 测试结论：0：不通过，1：通过

	private Date startTime; // 测试开始时间

	private Date endTime; // 测试结束时间

	private int fbuga; // 发现的A级缺陷数

	private int fbugb; // 发现的B级缺陷数

	private int fbugc; // 发现的C级缺陷数

	private int fbugd; // 发现的D级缺陷数

	private String reportPath; // 测试报告存放路径

	private String comment; // 测试结论说明

	private Date optime;// 操作时间

	private int operator; // 操作人

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public String getMainFlowNo() {
		return mainFlowNo;
	}

	public void setMainFlowNo(String mainFlowNo) {
		this.mainFlowNo = mainFlowNo;
	}

	public int getTester() {
		return tester;
	}

	public void setTester(int tester) {
		this.tester = tester;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getFbuga() {
		return fbuga;
	}

	public void setFbuga(int fbuga) {
		this.fbuga = fbuga;
	}

	public int getFbugb() {
		return fbugb;
	}

	public void setFbugb(int fbugb) {
		this.fbugb = fbugb;
	}

	public int getFbugc() {
		return fbugc;
	}

	public void setFbugc(int fbugc) {
		this.fbugc = fbugc;
	}

	public int getFbugd() {
		return fbugd;
	}

	public void setFbugd(int fbugd) {
		this.fbugd = fbugd;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getOptime() {
		return optime;
	}

	public void setOptime(Date optime) {
		this.optime = optime;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

}
