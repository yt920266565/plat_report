package com.yanmade.plat.publish.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yanmade.plat.publish.dao.PhoneMapper;
import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.entity.VerMain;
import com.yanmade.plat.publish.service.FtpService;
import com.yanmade.plat.publish.service.MailService;
import com.yanmade.plat.publish.service.VersionService;
import com.yanmade.plat.publish.util.PhoneSendUtil;

@RestController
@RequestMapping("/phone")
public class PhoneController {
	
	private static final Logger log = LoggerFactory.getLogger(PhoneController.class);

	@Autowired
	PhoneMapper mapper;

	@Autowired
	VersionService service;

	@Autowired
	VerMainMapper vmapper;

	@Autowired
	VerMain ver;
	
	@Autowired
	PhoneSendUtil phoneSendUtil;
	
	@Autowired
	MailService mailService;

	@GetMapping("/published")
	public Map<String, Object> getPublished() {
		List<HashMap<String, Object>> list = mapper.getPublished();
		Map<String, Object> map = new HashMap<>();
		map.put("data", list);
		map.put("code", 0);
		return map;
	}

	@GetMapping("{sendSale}/updateSale")
	public Map<String, Object> sendSale(@RequestParam Map<String, Object> rmap, @PathVariable String sendSale) {
		Map<String, Object> map = new HashMap<>();
		boolean result = mapper.updateSale(rmap);
		if (!result) {
			map.put("msg", "uperror");
			return map;
		}
		String msg = "";
		if (sendSale.length() > 0) {
			int publisher = Integer.parseInt(rmap.get("publisher").toString());
			map = vmapper.getUserById(publisher);
			ver.setFlowNo(rmap.get("flowNo").toString());
			ver.setProjName(rmap.get("projName").toString());
			ver.setVerId(rmap.get("verId").toString());
			ver.setVerNo(rmap.get("verNo").toString());
			ver.setVerInfo(rmap.get("verInfo").toString());
			sendSale = sendSale.replace(",", "|");
			if (sendSale.endsWith("|")) {
				sendSale = sendSale.replace("|", "");
			}
			msg = phoneSendUtil.updateSale(sendSale, ver, map);
			try {
				mailService.sendMail(mapper.getVerPublishByFlowNo(ver.getFlowNo()), "update");
			} catch (MessagingException e) {
				log.error("发送邮件参数错误:{}",e.getMessage());
			}
		}
		map.put("msg", msg);
		return map;
	}

	// 下载版本文件
	@GetMapping("/{flowNo}/download")
	public void downloadFile(@PathVariable String flowNo, @RequestParam Map<String, Object> map,
			HttpServletResponse response) throws IOException {
		Map<String, Object> vermap = service.getVerPath(flowNo);
		String verPath = vermap.get("verPath").toString();
		verPath = verPath.replace("\\", "/");
		if (!verPath.endsWith("/")) {
			verPath = verPath + File.separator;
		}
		verPath = verPath.replace("\\", "/");
		String fileString = map.get("files").toString();
		if (fileString.equals("")) {
			return;
		}
		String[] files = fileString.split(",");
		FtpService ftpService = new FtpService();
		if (files.length > 1) {
			String[] verArray = verPath.split("/");
			String zipName = verArray[verArray.length - 2] + "_" + verArray[verArray.length - 1] + ".zip";
			ftpService.downloadAll(verPath, files, zipName, response);
		} else {
			// 前端传过来的文件名是转过码的
			String file = URLDecoder.decode(files[0], "utf-8");
			ftpService.downloadFile(verPath, response, file);
		}
	}

}
