package com.yanmade.plat.publish.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.entity.VerMain;
import com.yanmade.plat.publish.util.PinYinUtil;

@Service
public class MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	@Autowired
	VerMainMapper mainMapper;

	@Autowired
	JavaMailSenderImpl mainSend;

	@Value("${mail.from}")
	private String from;

	@Value("${spring.qywx.sendDownloadUrl}")
	private String downUrl;

	// 避免测试的时候，发邮件
	@Value("${spring.qywx.sendMessage}")
	private boolean sendMessage;

	@Async("threadPoolExecutor")
	public void sendMail(VerMain ver, String subjType) throws MessagingException {
		if (!sendMessage) {
			return;
		}

		MimeMessage parentMimeMessage = mainSend.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(parentMimeMessage, true, "utf-8");
		messageHelper.setFrom(from);
		messageHelper.setSentDate(new Date());
		List<String> mails = messageHelperSet(ver, messageHelper, subjType);
		if (mails.isEmpty()) {
			return;
		}

		for (int j = 0; j < mails.size(); j++) {
			try {
				messageHelper.setTo(mails.get(j));
				mainSend.send(parentMimeMessage);
				logger.info("{}发送邮件成功", mails.get(j));
			} catch (Exception e) {
				logger.error("{}发送邮件异常,{}", mails.get(j), e.getMessage());
			}
		}

	}

	/**
	 * 设置发送邮件的主题subject，内容content，收件人mailList
	 * 
	 * @param ver
	 * @param cardType
	 * @param messageHelper
	 * @return
	 * @throws MessagingException
	 */
	private List<String> messageHelperSet(VerMain ver, MimeMessageHelper messageHelper, String subjType)
			throws MessagingException {
		String descript = "<br>项目名:" + ver.getProjName() + "<br>软件名称:" + ver.getVerId() + "<br>软件版本:" + ver.getVerNo()
				+ "<br>版本描述:" + ver.getVerInfo();
		String comTime = "发布时间:";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int publish = 0;
		if (ver.getVerType() == 1) {
			publish = ver.getTestManager();
		} else {
			publish = ver.getCfgManager();
		}

		Map<String, Object> userMap = mainMapper.getUserById(publish);
		String userName = "";
		String realName = "";
		if (!Objects.isNull(userMap)) {
			userName = userMap.get("username").toString();
			realName = userMap.get("realname").toString();
		}

		String infoPeople = ver.getInfoPeople();
		infoPeople = infoPeople.replaceAll("\\(\\d+\\)", "");
		String[] mailList = infoPeople.split(",");

		String subject = "";
		if (subjType.equals("publish")) {
			subject = "版本发布提醒";
		} else if (subjType.equals("update")) {
			subject = "版本更新提醒，请认准最新版本";
		}
		String content = comTime + sdf.format(ver.getPublishTime()) + descript + "<br>版本发布人:" + realName + "-"
				+ userName + "<br><a href=" + downUrl + ver.getFlowNo() + ">下载软件包</a>";

		messageHelper.setSubject(subject);
		messageHelper.setText(content, true);
		
		List<String> resultList = new ArrayList<>();
		//通知人员人名转拼音+@yanmade.com
		for (int i = 0; i < mailList.length; i++) {
			String address = PinYinUtil.chToPinYin(mailList[i]);
			if (StringUtils.isEmpty(address)) {
				continue;
			}
			mailList[i] = address;
		}
		resultList.addAll(Arrays.asList(mailList));
		
		//客户邮箱
		if(StringUtils.isNotBlank(ver.getMails())) {
			String mails = ver.getMails().replaceAll("，", ",");
			String[] marray = mails.split(",");
			resultList.addAll(Arrays.asList(marray));
		}
		return resultList;
	}

}
