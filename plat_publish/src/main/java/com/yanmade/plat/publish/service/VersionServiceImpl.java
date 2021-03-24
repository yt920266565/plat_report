package com.yanmade.plat.publish.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanmade.plat.framework.entity.SmRole;
import com.yanmade.plat.framework.service.CacheService;
import com.yanmade.plat.framework.service.RoleService;
import com.yanmade.plat.framework.service.UserService;
import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.dao.VerOperatorMapper;
import com.yanmade.plat.publish.entity.VerMain;
import com.yanmade.plat.publish.entity.VerOperation;
import com.yanmade.plat.publish.entity.VerTest;
import com.yanmade.plat.publish.util.FlowNoUtil;
import com.yanmade.plat.publish.util.PhoneSendUtil;

@Service
public class VersionServiceImpl implements VersionService {

	private static final String PAGE = "page";
	private static final String LIMIT = "limit";
	private static final String ROLEID = "roleId";
	private static final String FUNCTION = "version.published.query";
	private static final String USERNAME = "username";
	private static final Logger Logger = LoggerFactory.getLogger(VersionServiceImpl.class);

	@Autowired
	RoleService roleService;

	@Autowired
	private VerMainMapper mapper;

	@Autowired
	private VerOperatorMapper opmapper;

	@Autowired
	private UserService userService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private VerOperation vop;

	@Autowired
	private FlowNoUtil flowNoUtil;

	@Autowired
	private PhoneSendUtil phoneSendUtil;
	
	@Autowired
	private MailService mailService;

	@Override
	@Transactional
	public boolean insert(VerMain ver) {
		String mFlowNo = flowNoUtil.getMainFlowNo(ver);
		ver.setFlowNo(mFlowNo);
		Date date = new Date();
		ver.setCreateTime(date);
		ver.setApplyTime(date);

		// 获取当前用户的username
		int id = getCurrenter();
		ver.setApplicant(id);
		int verType = ver.getVerType();
		int currenter = 0;
		// verType==1为正式版本
		if (verType == 1) {
			ver.setCurrenter(ver.getCfgManager());
			currenter = ver.getCfgManager();
		} else {
			ver.setCurrenter(ver.getTestManager());
			currenter = ver.getTestManager();
		}

		// 没有选择发布人就以申请人作为版本发布人
		if (ver.getPublisher() == 0) {
			ver.setPublisher(id);
		}
		// 插入ver_main表
		if (!mapper.insert(ver)) {
			return false;
		}
		// 插入操作表ver_operation
		if (!insertOpt(ver, id)) {
			return false;
		}
		
		sendMydo(currenter, ver.getProjName());
		return true;
	}

	@Override
	@Transactional
	public boolean update(VerMain ver) {
		// 先检查该流程是否被管理员更改了审批人
		if (!isCurrenter(ver)) {
			return false;
		}
		VerMain originVer = mapper.getVerMain(ver.getFlowNo());
		int status = ver.getStatus();
		String name = ver.getName();
		int currenter = 0;

		// 申请正式版本
		if (status == 2 && name.equals("Apply")) {
			ver.setApplyTime(new Date());
			ver.setCurrenter(ver.getCfgManager());
			currenter = ver.getCfgManager();
			// 申请临时版本
		} else if (status == 2 && name.equals("Approve")) {
			ver.setTestTime(new Date());
			ver.setCurrenter(originVer.getCfgManager());
			currenter = originVer.getCfgManager();
			// 临时版本审批完成，处于待构建状态
		} else if (status == 5) {
			ver.setApplyTime(new Date());
			ver.setCurrenter(originVer.getTestManager());
			currenter = originVer.getTestManager();
			// 正式版本构建完成处于待测试状态
		} else if (status == 3) {
			ver.setBuildTime(new Date());
			ver.setCurrenter(originVer.getTestManager());
			currenter = originVer.getTestManager();
		}
		// 不走最后的发布流程 else if (status == 4) { if (name.equals("Test")) {
		/*
		 * ver.setTestTime(new Date()); } else if (name.equals("Build")) {
		 * ver.setBuildTime(new Date()); } ver.setCurrenter(ver.getPublisher());
		 * currenter = ver.getPublisher(); }
		 */else if (status == 101) {
			return insertOver(ver);
		}

		if (!mapper.update(ver)) {
			return false;
		}

		if (!insertOpt(ver, 0)) {
			return false;
		}
		sendMydo(currenter, originVer.getProjName());
		// 发送测试包给测试人员下载
		if (status == 3) {
			sendTestManager(originVer, currenter);
		}
		return true;
	}

	// 发送测试包给测试人员下载
	private void sendTestManager(VerMain originVer, int currenter) {
		Map<String, Object> map = mapper.getUserById(originVer.getApplicant());
		Map<String, Object> testManagerMap = mapper.getUserById(currenter);
		phoneSendUtil.sendTestManager(testManagerMap.get(USERNAME).toString(), originVer, map);
	}

	private void sendMydo(int currenter, String projName) {
		Map<String, Object> map = mapper.getUserById(currenter);
		// 当前处理人已经离职，sm_user删除了该用户
		if (Objects.isNull(map)) {
			Logger.info("待我处理的审批人可能已经离职,该用户id={}", currenter);
			return;
		}
		String userName = map.get(USERNAME).toString();
		String realName = map.get("realname").toString();
		// 企业微信消息提醒
		phoneSendUtil.sendMydo(userName, realName,
				"<br>项目名:" + projName + ",</br>待你审批,请登录<br>erp.yanmade.com</br>进入版本管理,在待我处理查看");
	}

	@Override
	public VerMain getVerMain(String flowNo) {
		return mapper.getVerMain(flowNo);
	}

	public boolean delete(String flowNo) {
		return mapper.delete(flowNo);
	}

	@Override
	@Transactional
	public boolean insertOpt(VerMain ver, int id) {
		int status = ver.getStatus();
		// int testResult = ver.getTestResult();
		String name = ver.getName();
		// 获取当前登录的用户
		if (id == 0) {
			id = getCurrenter();
		}
		if (id == 0) {
			return false;
		}
		vop.setmFlowNo(ver.getFlowNo());
		String flowNo = flowNoUtil.getOperatorFlowNo();
		vop.setFlowNo(flowNo);
		vop.setName(ver.getName());
		vop.setResult(ver.getResult());
		vop.setComment(ver.getComment());
		vop.setOptime(new Date());
		vop.setOperator(id);
		// 插入操作表
		if (!opmapper.insert(vop)) {
			return false;
		}
		// 测试环节需要插入测试表，测试通过和不通过两张情况
		if ((status == 6 && name.equals("Test")) || status == 0 && name.equals("Test")) {
			VerTest vtTest = new VerTest();
			vtTest.setFlowNo(flowNo);
			vtTest.setMainFlowNo(ver.getFlowNo());
			vtTest.setTester(ver.getTester());
			vtTest.setResult(ver.getTestResult());
			vtTest.setStartTime(ver.getStartTime());
			vtTest.setEndTime(ver.getEndTime());
			vtTest.setFbuga(ver.getFbuga());
			vtTest.setFbugb(ver.getFbugb());
			vtTest.setFbugc(ver.getFbugc());
			vtTest.setFbugd(ver.getFbugd());
			vtTest.setReportPath(ver.getReportPath());
			vtTest.setComment(ver.getComment());
			vtTest.setOptime(new Date());
			vtTest.setOperator(id);
			// 插入测试表
			if (!opmapper.insertTest(vtTest)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Transactional
	public boolean insertOver(VerMain ver) {
		VerMain originVer = mapper.getVerMain(ver.getFlowNo());
		Date date = new Date();
		originVer.setResult(ver.getResult());
		originVer.setComment(ver.getComment());
		originVer.setName(ver.getName());
		originVer.setStatus(ver.getStatus());
		originVer.setInfoPeople(ver.getInfoPeople());
		originVer.setMails(ver.getMails());
		// 测试不通过插入测试表的数据
		if (ver.getStatus() == 0) {
			originVer.setTestTime(date);
			originVer.setFbuga(ver.getFbuga());
			originVer.setFbugb(ver.getFbugb());
			originVer.setFbugc(ver.getFbugc());
			originVer.setFbugd(ver.getFbugd());
			originVer.setTester(ver.getTester());
			originVer.setTestResult(ver.getTestResult());
			originVer.setReportPath(ver.getReportPath());
			originVer.setStartTime(ver.getStartTime());
			originVer.setEndTime(ver.getEndTime());
		}
		originVer.setPublishTime(date);
		// 插入流程异常结束表
		if (!mapper.insertOver(originVer)) {
			return false;
		}
		// 插入操作表
		if (!insertOpt(originVer, 0)) {
			return false;
		}
		return mapper.delete(ver.getFlowNo());
	}

	// 处理人是否是当前登录的人员，可能会在版本维护里面被修改了
	private boolean isCurrenter(VerMain ver) {
		int cur = mapper.getCurrenter(ver.getFlowNo());
		return getCurrenter() == cur;
	}

	/**
	 * 插入版本发布表ver_publish
	 */
	@Override
	@Transactional
	public boolean insertPub(VerMain ver, String sendSale) {
		/* int publish = ver.getPublisher(); */
		VerMain originVer = mapper.getVerMain(ver.getFlowNo());
		int publish = 0;
		Date date = new Date();
		originVer.setResult(ver.getResult());
		originVer.setComment(ver.getComment());
		originVer.setName(ver.getName());
		originVer.setStatus(ver.getStatus());
		originVer.setInfoPeople(ver.getInfoPeople());
		originVer.setMails(ver.getMails());

		// 正式版本测试后发布需要插入测试表和操作表，临时版本不需要插入测试表
		if (originVer.getVerType() == 1) {
			originVer.setTestTime(date);
			publish = originVer.getTestManager();
			originVer.setFbuga(ver.getFbuga());
			originVer.setFbugb(ver.getFbugb());
			originVer.setFbugc(ver.getFbugc());
			originVer.setFbugd(ver.getFbugd());
			originVer.setTester(ver.getTester());
			originVer.setTestResult(ver.getTestResult());
			originVer.setReportPath(ver.getReportPath());
			originVer.setStartTime(ver.getStartTime());
			originVer.setEndTime(ver.getEndTime());
		} else {
			originVer.setBuildTime(date);
			publish = originVer.getCfgManager();
			originVer.setVerPath(ver.getVerPath());
		}

		// 插入已经发布表ver_publish
		Map<String, Object> map = mapper.getUserById(publish);
		originVer.setPublishTime(date);
		originVer.setPublisher(publish);
		if (!mapper.insertPub(originVer)) {
			return false;
		}
		// 插入操作表ver_operation
		if (!insertOpt(originVer, 0)) {
			return false;
		}
		
		//发邮件给通知人员
		try {
			mailService.sendMail(originVer,"publish");
		} catch (MessagingException e) {
			Logger.error("发送邮件参数错误:{}",e.getMessage());
		}

		// 页面上选定要通知的人员，构造企业微信给多人发消息的格式
		sendSale = sendSale.replace(",", "|");
		if (sendSale.endsWith("|")) {
			sendSale = sendSale.replace("|", "");
		}

		// 版本发布默认给版本申请人发布消息
		Map<String, Object> applicantMap = mapper.getUserById(originVer.getApplicant());
		// 离职的话，同步用户操作会在sm_user表里面删除该人员
		if (applicantMap != null && !sendSale.contains(applicantMap.get(USERNAME).toString())) {
			sendSale += "|" + applicantMap.get(USERNAME);
		}

		// 版本发布，消息推送给选定的通知人员
		phoneSendUtil.sendSale(sendSale, originVer, map);
		return mapper.delete(ver.getFlowNo());
	}
	
	@Override
	public List<HashMap<String, Object>> getPublished(Map<String, Object> input) {
		if (input.get(LIMIT) != null) {
			int page = Integer.parseInt(input.get(PAGE).toString());
			int limit = Integer.parseInt(input.get(LIMIT).toString());
			if (page < 1) {
				input.put(PAGE, (page * limit));
			} else {
				input.put(PAGE, (page - 1) * limit);
			}
			input.put(LIMIT, limit);
		} else {
			input.put(LIMIT, 5);
			input.put(PAGE, 0);
		}
		
		List<HashMap<String, Object>> pubList = new ArrayList<>();
		// 获取当前用户的username，判断是什么角色，除了管理员角色都需要获取有哪些数据权限deptid
		int id = getCurrenter();
		List<SmRole> list = userService.getRolesByUserId(id);
		for (SmRole srole : list) {
			if (srole.getId() == -1) {
				input.put(ROLEID, -1);
				pubList.addAll(mapper.getPublished(input));
				listAddDiscard(pubList,-1);
				listAddInitPeople(pubList);
				return pubList;
			}
		}
		
		String functionName = FUNCTION;
		getPubInput(functionName, input, id);
		pubList.addAll(mapper.getPublished(input));
		listAddDiscard(pubList,0);
		listAddInitPeople(pubList);
		return pubList;
	}

	@Override
	public int getPubCnt(Map<String, Object> input) {
		return mapper.getPubCnt(input);
	}

	// 将数据权限即deptId放入input参数里面
	public void getPubInput(String functionName, Map<String, Object> input, int id) {
		String depId = cacheService.getUserFunctionDepartmentsCache(id, functionName);
		String[] deplist = { "0" };
		if (depId != null) {
			deplist = depId.split(",");
		}
		input.put("deplist", deplist);
	}

	@Override
	public List<HashMap<String, Object>> getMaintain(Map<String, Object> input) {
		if (input.get(LIMIT) != null) {
			int page = Integer.parseInt(input.get(PAGE).toString());
			int limit = Integer.parseInt(input.get(LIMIT).toString());
			if (page < 1) {
				input.put(PAGE, (page * limit));
			} else {
				input.put(PAGE, (page - 1) * limit);
			}
			input.put(LIMIT, limit);
		}
		
		List<HashMap<String, Object>> mainList = new ArrayList<>();
		// 获取当前用户的username，判断是什么角色，除了管理员角色都需要获取有哪些数据权限deptid
		int id = getCurrenter();
		List<SmRole> list = userService.getRolesByUserId(id);
		for (SmRole srole : list) {
			if (srole.getId() == -1) {
				input.put(ROLEID, -1);
				mainList.addAll(mapper.getMaintain(input));
				listAddInitPeople(mainList);
				return mainList;
			}
		}
		
		String functionName = "version.maintenance";
		getPubInput(functionName, input, id);
		mainList.addAll(mapper.getMaintain(input));
		listAddInitPeople(mainList);
		return mainList;
	}

	@Override
	public int getMaintainCnt(Map<String, Object> input) {
		return mapper.getMaintainCnt(input);
	}

	/**添加initPeople，用作已通知人员的初始化
	 * @param list
	 */
	private void listAddInitPeople(List<HashMap<String, Object>> list) {
		String infoPeopleKey = "infoPeople";
		for (Map<String, Object> entries : list) {

			if (Objects.isNull(entries.get(infoPeopleKey))) {
				continue;
			}
			String initPeople = entries.get(infoPeopleKey).toString();
			initPeople = initPeople.replaceAll("[\u4e00-\u9fa5]+\\(", "");
			initPeople = initPeople.replaceAll("\\)", "");
			entries.put("initPeople", initPeople);
		}
	}

	/**添加版本废弃权限，用作页面的废弃按钮的判断
	 * @param list
	 * @param role
	 */
	private void listAddDiscard(List<HashMap<String, Object>> list,int role) {
		if(role == -1) {
			for (HashMap<String, Object> m : list) {
				m.put("discard", true);
			}
			return;
		}
		
		// 查询当前用户的功能权限
		Map<String, Object> funMap = userService.getFunctionsByUserId(getCurrenter());
		Object[] funList = (Object[]) funMap.get("authority");
		for (Object f : funList) {
			// 有版本废弃权限
			if (f.equals("version.discard")) {
				for (HashMap<String, Object> m : list) {
					m.put("discard", true);
				}
				return;
			}
		}
	}

	// 获取当前登录的用户id
	private int getCurrenter() {
		String loginString = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return mapper.getIdByName(loginString);
	}

	/**
	 * 版本维护的操作:编辑,回滚,终止流程
	 */
	@Override
	@Transactional
	public Boolean verMaintain(VerMain ver) {
		String projName = ver.getProjName();
		int status = ver.getStatus();
		int currenter = 0;
		// 版本回滚
		if (ver.getProduct() == null) {
			// 回滚之后，流程状态为待测试或者待审批
			if (status == 3 || status == 5) {
				ver.setCurrenter(ver.getTestManager());
				currenter = ver.getTestManager();
			} else if (status == 2) {
				ver.setCurrenter(ver.getCfgManager());
				currenter = ver.getCfgManager();
			} else if (status == 1) {
				ver.setCurrenter(ver.getApplicant());
				currenter = ver.getApplicant();
			}
			// 回滚
			rollBack(ver);

			// 给上一个流程处理人发消息
			sendMydo(currenter, projName);
			return true;
		} else if (status == 100) {
			// 版本流程终止
			return insertOver(ver);
		}

		// 版本维护修改，先检查是否已经被原来处理的人发布了
		int count = mapper.checkPub(ver.getFlowNo());
		if (count == 0) {
			return false;
		}

		Map<String, Object> map = getSendFlag(ver, currenter);
		currenter = (int) map.get("currenter");
		ver.setCurrenter(currenter);

		// 更新ver_main表数据
		mapper.verMaintain(ver);
		// 插入操作表ver_operation
		insertOpt(ver, 0);

		// 修改了当前流程状态的处理人，发送消息
		if ((boolean) map.get("sendFlag")) {
			sendMydo(currenter, projName);
			if (status == 3 || status == 7) {
				sendTestManager(mapper.getVerMain(ver.getFlowNo()), currenter);
			}
		}
		return true;
	}

	// 检查版本维护是否修改对应状态的审批人
	private Map<String, Object> getSendFlag(VerMain ver, int currenter) {
		boolean sendFlag = false;
		VerMain originVer = mapper.getVerMain(ver.getFlowNo());
		int cfgManager = originVer.getCfgManager();
		int testManager = originVer.getTestManager();
		int status = originVer.getStatus();
		// int publisher = originVer.getPublisher();

		// 检查维护时修改的是否是当前状态的处理人，是的话需要发送消息给新的处理人
		// 1--申请人，2--配置管理员，3、7、5--测试负责人
		if (status == 1) {
			currenter = ver.getApplicant();
		} else if (status == 2) {
			currenter = ver.getCfgManager();
			if (currenter != cfgManager) {
				sendFlag = true;
			}
		} else if (status == 3 || status == 5 || status == 7) {
			currenter = ver.getTestManager();
			if (currenter != testManager) {
				sendFlag = true;
			}
		} /*
			 * 不走发布流程，暂时注解 else if (status == 4) { currenter = ver.getPublisher(); if
			 * (currenter != publisher) { sendFlag = true; } }
			 */
		Map<String, Object> map = new HashMap<>();
		map.put("sendFlag", sendFlag);
		map.put("currenter", currenter);
		return map;
	}

	@Override
	@Transactional
	public Boolean rollBack(VerMain ver) {
		if (!mapper.rollBack(ver)) {
			return false;
		}
		return insertOpt(ver, 0);
	}

	@Override
	public int verIsRept(VerMain ver) {
		return mapper.verIsRept(ver);
	}

	@Override
	public int getIdByName(String username) {
		return mapper.getIdByName(username);
	}

	@Override
	public List<HashMap<String, Object>> getErpDic() {
		return opmapper.getErpDic();
	}

	@Override
	public List<Map<String, Object>> getUnpublished() {
		int id = getCurrenter();
		String deptIds = "";
		if (!cacheService.isAdminCache(id)) { // 不是管理员才拼接sql
			deptIds = cacheService.getUserFunctionDepartmentsCache(id, FUNCTION);
		}

		List<String> deptList = new ArrayList<>();
		if (!StringUtils.isEmpty(deptIds)) {
			deptList = Arrays.asList(deptIds.split(","));
		}

		return mapper.getUnpublished(deptList);
	}

	@Override
	public Map<String, Object> getVerPath(String flowNo) {
		return mapper.getVerPath(flowNo);
	}

	@Override
	public List<String> getFlowNo(Map<String, Object> map) {
		return mapper.getFlowNo(map);
	}

	@Override
	public List<HashMap<String, Object>> getPubExcel(Map<String, Object> map) {
		return mapper.getPubExcel(map);
	}

}
