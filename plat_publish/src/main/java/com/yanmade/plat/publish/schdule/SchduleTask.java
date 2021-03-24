package com.yanmade.plat.publish.schdule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yanmade.plat.framework.dao.DepartmentMapper;
import com.yanmade.plat.framework.dao.UserMapper;
import com.yanmade.plat.framework.entity.SmDepartment;
import com.yanmade.plat.framework.entity.SmUser;
import com.yanmade.plat.framework.util.RemoteUtil;
import com.yanmade.plat.publish.dao.SchduleMapper;
import com.yanmade.plat.publish.util.PhoneSendUtil;

@Component
public class SchduleTask {
	
	private static final Logger log = LoggerFactory.getLogger(SchduleTask.class);

	@Autowired
	SchduleMapper mapper;
	
	@Autowired
	PhoneSendUtil phoneSendUtil;
	
	@Autowired
    RemoteUtil remoteUtil;
	
	@Autowired
	DepartmentMapper depMapper;
	
	@Autowired
	UserMapper userMapper;
	
    //工作日的每天9点10分执行
	@Scheduled(cron = "0 10 9 * * ?")
	public void remindMydo()  {
		log.info("定时推送待处理开始");
		List<HashMap<String, Object>> verList = mapper.getAllVerMain();
		if(verList.isEmpty()) {
			return;
		}
		//休息日不推送
		LocalDate now = LocalDate.now();
		Map<String, Object> fmap = mapper.isFreeDay(now);
		if(fmap != null) {
			return;
		}
		
		//判断对应的申请之后流程是否已经超过一天未处理，未处理则推送消息
		for(HashMap<String, Object> map:verList) {
			String status = map.get("status").toString();
			String verType = map.get("verType").toString();
			String regexp = "\\.\\d";
			String pattern = "yyyy-MM-dd HH:mm:ss";
			String applyTime = map.get("applyTime").toString().replaceAll(regexp, "");
			if(status.equals("2") && verType.equals("1")) {//正式版本申请完之后，待构建
				LocalDateTime dateTime = LocalDateTime.parse(applyTime,DateTimeFormatter.ofPattern(pattern));
				remindUser(map,dateTime);
			}
			
			if(status.equals("5") ) {//临时版本申请完之后，待审批
				LocalDateTime dateTime = LocalDateTime.parse(applyTime,DateTimeFormatter.ofPattern(pattern));
				remindUser(map,dateTime);
			}
			
			if(status.equals("3")) {//正式版本构建完之后，待测试
				String buildTime = map.get("buildTime").toString().replaceAll(regexp, "");
				LocalDateTime dateTime = LocalDateTime.parse(buildTime,DateTimeFormatter.ofPattern(pattern));
				remindUser(map,dateTime);
			}
			
			if(status.equals("2") && verType.equals("2")) {//临时版本审批完之后，待构建
				String testTime = map.get("testTime").toString().replaceAll(regexp, "");
				LocalDateTime dateTime = LocalDateTime.parse(testTime,DateTimeFormatter.ofPattern(pattern));
				remindUser(map,dateTime);
			}
			
		}
	}
	
	/**
	 * 每天早上九点定时同步oa的用户，部门数据到系统
	 */
	@Scheduled(cron = "0 0 9 * * ?")
	public void synchUserAndDept() {
		log.info("定时同步用户部门数据开始");
		List<SmUser> users = remoteUtil.getUserList();
		List<SmDepartment> depts = remoteUtil.getDepartmentsList();
		userMapper.insertBatch(users);
		depMapper.insertBatch(depts);
	}
	
	private void remindUser(Map<String, Object> map,LocalDateTime dateTime) {
		String appCode = map.get("appCode").toString();
		String appName = map.get("appName").toString();
		String curCode = map.get("curCode").toString();
		String curName = map.get("curName").toString();
		String projName = map.get("projName").toString();
		//一天后的日期
		LocalDateTime endDateTime = dateTime.plusDays(1);
		//计算待处理时间到现在为止是否已经超过一天
		if(endDateTime.isBefore(LocalDateTime.now())) {
			String msg = "<br>项目名:"+projName+",</br>你申请的项目未被审批,请登录<br>erp.yanmade.com</br>进入版本管理,在我处理的查看流程节点";
			String msg1 = "<br>项目名:"+projName+",</br>待你审批,请登录<br>erp.yanmade.com</br>进入版本管理,在待我处理审批";
			phoneSendUtil.sendMydo(appCode, appName, msg);
			phoneSendUtil.sendMydo(curCode, curName, msg1);
		}
	}

}
