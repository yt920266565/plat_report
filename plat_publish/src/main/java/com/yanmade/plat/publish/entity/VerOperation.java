package com.yanmade.plat.publish.entity;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class VerOperation {

	private String flowNo; // 操作流水号

	private String mFlowNo; // Ver_detail.flowno

	/*
	 * 操作环节：Apply:申请构建,Build:构建,Test:测试,Approve:审批,Publish:发布
	 * Maintenance:维护,Rollback:流程回退，Stop:终止流程,Download:下载
	 */
	private String name;

	private int result; // 操作结果

	private String comment; // 操作说明

	private String comment1; // 操作说明1

	private String comment2; // 操作说明2

	private String comment3; // 操作说明3

	private Date optime; // 操作时间

	private int operator; // 操作人

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public String getmFlowNo() {
		return mFlowNo;
	}

	public void setmFlowNo(String mFlowNo) {
		this.mFlowNo = mFlowNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment1() {
		return comment1;
	}

	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}

	public String getComment2() {
		return comment2;
	}

	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}

	public String getComment3() {
		return comment3;
	}

	public void setComment3(String comment3) {
		this.comment3 = comment3;
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
