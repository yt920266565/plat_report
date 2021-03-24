package com.yanmade.plat.publish.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

@Component
public class VerMain {

	private String flowNo; // 版本流水号

	private String projName; // YM项目名称

	private String product; // 被测产品大类

	private String subPartNo; // 子料号序号

	private String testType; // 测试类型：

	private int verType; // 版本类型 正式版本（1），临时版本（2）

	private int deptId; // 产品线（所属部门）

	private String client; // 客户代码

	private int verlevel; // 版本层级： 产品（1） 上位机（2）下位机（3）

	private int pkgType; // 版本包属性：完整版本（1） 补丁版本（2）

	private String verId; // 软件标识

	private String verNo; // 版本号

	private int cfgManager; // 配置管理员

	private int testManager; // 测试人/审批人

	private String verInfo; // 版本信息

	private String buildInfo; // 构建说明

	private String tempInfo; // 临时版本说明

	private String verPath; // 版本提取路径

	private int status; // 版本状态：待申请（1） 待构建（2）待测试（3）待发布（4）待审批（5）流程终止（100）审批拒绝（101）发布拒绝（102）

	private int currenter; // 当前处理人

	private Date createTime; // 版本创建时间

	private int applicant; // 版本申请人

	private Date applyTime; // 版本申请时间

	private Date buildTime; // 版本构建完成时间

	private int builder; // 版本构建人

	private Date testTime; // 版本测试/审批完成时间

	private int testResult; // 版本测试/审批结果

	private int tester; // 版本测试/审批人

	private Date publishTime; // 版本发布时间

	private int publisher; // 版本发布人
	
	private String infoPeople; //发布记录的人员
	
	private String saleInfo;//发布通知的人员，比infoPeople少了姓名，方便发送消息
	
	private String mails;//客户邮箱地址


	/*
	 * 操作环节：Apply:申请构建,Build:构建,Test:测试,Approve:审批,Publish:发布
	 * Maintenance:维护,Rollback:流程回退，Stop:终止流程,Download:下载
	 * 操作表的字段，方便更新ver_main表的时候插入ver_operator表
	 */
	private String name;

	private int result; // 操作结果

	private String comment; // 操作说明

	private String comment1; // 操作说明1

	private String comment2; // 操作说明2

	private String comment3; // 操作说明3

	private Date optime; // 操作时间

	private int operator; // 操作人

	/*
	 * 
	 * 
	 * 测试表的字段，方便更新ver_main表的时候插入ver_test表
	 */

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime; // 测试开始时间

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime; // 测试结束时间

	private int fbuga; // 发现的A级缺陷数

	private int fbugb; // 发现的B级缺陷数

	private int fbugc; // 发现的C级缺陷数

	private int fbugd; // 发现的D级缺陷数

	private String reportPath; // 测试报告存放路径

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

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSubPartNo() {
		return subPartNo;
	}

	public void setSubPartNo(String subPartNo) {
		this.subPartNo = subPartNo;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public int getVerType() {
		return verType;
	}

	public void setVerType(int verType) {
		this.verType = verType;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public int getVerlevel() {
		return verlevel;
	}

	public void setVerlevel(int verlevel) {
		this.verlevel = verlevel;
	}

	public int getPkgType() {
		return pkgType;
	}

	public void setPkgType(int pkgType) {
		this.pkgType = pkgType;
	}

	public String getVerId() {
		return verId;
	}

	public void setVerId(String verId) {
		this.verId = verId;
	}

	public String getVerNo() {
		return verNo;
	}

	public void setVerNo(String verNo) {
		this.verNo = verNo;
	}

	public int getCfgManager() {
		return cfgManager;
	}

	public void setCfgManager(int cfgManager) {
		this.cfgManager = cfgManager;
	}

	public int getTestManager() {
		return testManager;
	}

	public void setTestManager(int testManager) {
		this.testManager = testManager;
	}

	public String getVerInfo() {
		return verInfo;
	}

	public void setVerInfo(String verInfo) {
		this.verInfo = verInfo;
	}

	public String getBuildInfo() {
		return buildInfo;
	}

	public void setBuildInfo(String buildInfo) {
		this.buildInfo = buildInfo;
	}

	public String getTempInfo() {
		return tempInfo;
	}

	public void setTempInfo(String tempInfo) {
		this.tempInfo = tempInfo;
	}

	public String getVerPath() {
		return verPath;
	}

	public void setVerPath(String verPath) {
		this.verPath = verPath;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCurrenter() {
		return currenter;
	}

	public void setCurrenter(int currenter) {
		this.currenter = currenter;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getApplicant() {
		return applicant;
	}

	public void setApplicant(int applicant) {
		this.applicant = applicant;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}

	public int getBuilder() {
		return builder;
	}

	public void setBuilder(int builder) {
		this.builder = builder;
	}

	public Date getTestTime() {
		return testTime;
	}

	public void setTestTime(Date testTime) {
		this.testTime = testTime;
	}

	public int getTestResult() {
		return testResult;
	}

	public void setTestResult(int testResult) {
		this.testResult = testResult;
	}

	public int getTester() {
		return tester;
	}

	public void setTester(int tester) {
		this.tester = tester;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public int getPublisher() {
		return publisher;
	}

	public void setPublisher(int publisher) {
		this.publisher = publisher;
	}
	
	public String getInfoPeople() {
		return infoPeople;
	}

	public void setInfoPeople(String infoPeople) {
		this.infoPeople = infoPeople;
	}
	
	public String getSaleInfo() {
		return saleInfo;
	}

	public void setSaleInfo(String saleInfo) {
		this.saleInfo = saleInfo;
	}
	
	public String getMails() {
		return mails;
	}

	public void setMails(String mails) {
		this.mails = mails;
	}

}
