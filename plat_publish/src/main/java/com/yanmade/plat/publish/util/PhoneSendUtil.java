package com.yanmade.plat.publish.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.yanmade.plat.publish.entity.VerMain;

@Component
public class PhoneSendUtil {
	private PhoneSendUtil() {

	}

	// 避免测试的时候，发消息，放到服务器时改为true
	@Value("${spring.qywx.sendMessage}")
	private boolean sendMessage;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.qywx.sendDownloadUrl}")
	private String sendDownloadUrl;

	private static final Logger logger = LoggerFactory.getLogger(PhoneSendUtil.class);
	private static final String CORPID = "ww16d99bc9fb8cc6a0"; // 企业号 private final String
	private static final String SECRET = "qlgvrYMvzMVD9CRpHrDGjjs4EBd7AYQy3JyabMDdWdc"; // 应用的Secret String
	private static final String URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESSTOKEN";
	private static final String TEXTCARD = "textcard";
	private static final String ERRCODE = "errcode";
	private static final String ERRMSG = "消息推送失败 原因{}";
	private static final String SUCCMSG = "消息推送成功";
	private static final String UTFCODE = "utf-8";
	private static final String ACCESSTOKEN = "ACCESSTOKEN";
	private static final String ERROR = "error";

	// 发送待我处理企业微信提醒
	public void sendMydo(String userName, String realName, String msg) {
		if (!sendMessage) {
			return;
		}
		String url = URL;
		String accessToken = getAccesToken();
		if (accessToken.equals(ERROR)) {
			return;
		}
		url = url.replace(ACCESSTOKEN, accessToken);
		JSONObject object = getBody(userName, "text");
		JSONObject cobject = new JSONObject();
		// 企业微信推送消息结构体，具体查看企业微信推送消息api
		String content = userName + " " + realName + msg;
		cobject.put("content", content);
		object.put("text", cobject);
		JSONObject jObject = post(url, object.toJSONString());
		if (jObject == null) {
			return;
		}
		if (!StringUtils.isEmpty(jObject.toJSONString()) && jObject.getInteger(ERRCODE) == 0) {
			logger.info(SUCCMSG);
		} else {
			String logObject = jObject.toJSONString();
			logger.info(ERRMSG, logObject);
		}
	}

	// 版本发布企业微信提醒售后人员
	public void sendSale(String userName, VerMain ver, Map<String, Object> map) {
		if (!sendMessage) {
			return;
		}
		String url = URL;
		String accessToken = getAccesToken();
		if (accessToken.equals(ERROR)) {
			return;
		}
		url = url.replace(ACCESSTOKEN, accessToken);
		JSONObject object = getBody(userName, TEXTCARD);
		JSONObject cobject = getTextCard(ver, "first", map);
		object.put(TEXTCARD, cobject);
		JSONObject jObject = post(url, object.toJSONString());
		if (jObject == null) {
			return;
		}
		if (!StringUtils.isEmpty(jObject.toJSONString()) && jObject.getInteger(ERRCODE) == 0) {
			logger.info(SUCCMSG);
		} else {
			String logObject = jObject.toJSONString();
			logger.info(ERRMSG, logObject);
		}
	}

	// 版本更新企业微信提醒售后人员
	public String updateSale(String userName, VerMain ver, Map<String, Object> map) {
		if (!sendMessage) {
			return "fail";
		}
		String url = URL;
		String str = "filed";
		String accessToken = getAccesToken();
		if (accessToken.equals(ERROR)) {
			return str;
		}
		url = url.replace(ACCESSTOKEN, accessToken);
		JSONObject object = getBody(userName, TEXTCARD);
		JSONObject cobject = getTextCard(ver, "update", map);
		object.put(TEXTCARD, cobject);
		JSONObject jObject = post(url, object.toJSONString());
		if (jObject == null) {
			return str;
		}
		if (!StringUtils.isEmpty(jObject.toJSONString()) && jObject.getInteger(ERRCODE) == 0) {
			logger.info(SUCCMSG);
			str = "success";
		} else {
			String logObject = jObject.toJSONString();
			logger.info(ERRMSG, logObject);
		}
		return str;
	}

	// 构建完成发送软件包给测试人员下载
	public void sendTestManager(String userName, VerMain ver, Map<String, Object> map) {
		if (!sendMessage) {
			return;
		}
		String url = URL;
		String accessToken = getAccesToken();
		if (accessToken.equals(ERROR)) {
			return;
		}
		url = url.replace(ACCESSTOKEN, accessToken);
		JSONObject object = getBody(userName, TEXTCARD);
		JSONObject cobject = getTextCard(ver, "testManager", map);
		object.put(TEXTCARD, cobject);
		JSONObject jObject = post(url, object.toJSONString());
		if (jObject == null) {
			return;
		}
		if (!StringUtils.isEmpty(jObject.toJSONString()) && jObject.getInteger(ERRCODE) == 0) {
			logger.info(SUCCMSG);
		} else {
			String logObject = jObject.toJSONString();
			logger.info(ERRMSG, logObject);
		}
	}

	/**
	 * POST请求的RAW参数传递
	 *
	 * @param url
	 * @param body
	 * @return
	 */
	public JSONObject post(String url, String body) {
		JSONObject jsonObject = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000)
					.build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new StringEntity(body, UTFCODE));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			jsonObject = com.alibaba.fastjson.JSON.parseObject(EntityUtils.toString(httpEntity, UTFCODE));
			return jsonObject;
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return jsonObject;
	}

	public String getAccesToken() {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + CORPID + "&corpsecret=" + SECRET;
		try {
			JSONObject result = restTemplate.getForObject(url, JSONObject.class);
			if (result.isEmpty()) {
				logger.info("access_token获取出错");
				return ERROR;
			}
			return result.get("access_token").toString();
		} catch (Exception e) {
			logger.error("企业微信获取access_token出错,原因{}", e.getMessage());
		}
		return ERROR;
	}

	private JSONObject getBody(String userName, String type) {
		JSONObject object = new JSONObject();
		object.put("msgtype", type);
		object.put("agentid", 1000032);
		object.put("touser", userName);
		object.put("toparty", "");
		object.put("totag", "");
		object.put("safe", 0);
		return object;
	}

	private JSONObject getTextCard(VerMain ver, String cardType, Map<String, Object> map) {
		String userName = "";
		String realName = "";
		if (!Objects.isNull(map)) {
			userName = map.get("username").toString();
			realName = map.get("realname").toString();
		}
		
		JSONObject cobject = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String title = "";
		String description = "";
		String comTime = "<div class='gray'>时间:";
		String comDiv = "</div>";
		String url = sendDownloadUrl + ver.getFlowNo();
		String btntxt = "点击下载";
		String descript = "项目名:" + ver.getProjName() + "\n软件名称:" + ver.getVerId() + "\n软件版本:" + ver.getVerNo()
				+ "\n版本描述:" + ver.getVerInfo();
		if (cardType.equals("first")) {
			title = "版本发布消息提醒";
			description = comTime + sdf.format(ver.getPublishTime()) + comDiv + descript + "\n版本发布人:" + realName + "("
					+ userName + ")";
		} else if (cardType.equals("testManager")) {
			title = "测试人员专用软件版本";
			description = comTime + sdf.format(new Date()) + comDiv + descript + "\n版本申请人:" + realName + "(" + userName
					+ ")" + "<div class='highlight'>该软件版本等待你的测试</div>";
		} else {
			title = "版本已更新，请认准最新版本";
			description = comTime + sdf.format(new Date()) + comDiv + descript + "\n版本发布人:" + realName + "(" + userName
					+ ")";
		}
		cobject.put("title", title);
		cobject.put("description", description);
		cobject.put("url", url);
		cobject.put("btntxt", btntxt);
		return cobject;
	}

	/**
	 * @desc ：微信上传素材的请求方法
	 * 
	 * @param requestUrl 微信上传临时素材的接口url
	 * @param file       要上传的文件
	 * @return String 上传成功后，微信服务器返回的消息
	 */
	/*
	 * public String httpRequest(String requestUrl, File file) { StringBuffer buffer
	 * = new StringBuffer(); try { // 1.建立连接 URL url = new URL(requestUrl);
	 * HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection(); //
	 * 打开链接 // 1.1输入输出设置 httpUrlConn.setDoInput(true);
	 * httpUrlConn.setDoOutput(true); httpUrlConn.setUseCaches(false); //
	 * post方式不能使用缓存 // 1.2设置请求头信息 httpUrlConn.setRequestProperty("Connection",
	 * "Keep-Alive"); httpUrlConn.setRequestProperty("Charset", UTFCODE); // 1.3设置边界
	 * String BOUNDARY = "----------" + System.currentTimeMillis();
	 * httpUrlConn.setRequestProperty("Content-Type",
	 * "multipart/form-data; boundary=" + BOUNDARY); // 请求正文信息 // 第一部分： //
	 * 2.将文件头输出到微信服务器 StringBuilder sb = new StringBuilder(); sb.append("--"); //
	 * 必须多两道线 sb.append(BOUNDARY); sb.append("\r\n");
	 * sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" +
	 * file.length() + "\";filename=\"" + file.getName() + "\"\r\n");
	 * sb.append("Content-Type:application/octet-stream\r\n\r\n"); byte[] head =
	 * sb.toString().getBytes(UTFCODE); // 获得输出流 OutputStream outputStream = new
	 * DataOutputStream(httpUrlConn.getOutputStream()); // 将表头写入输出流中：输出表头
	 * outputStream.write(head); // 3.将文件正文部分输出到微信服务器 // 把文件以流文件的方式 写入到微信服务器中
	 * DataInputStream in = new DataInputStream(new FileInputStream(file)); int
	 * bytes = 0; byte[] bufferOut = new byte[1024]; while ((bytes =
	 * in.read(bufferOut)) != -1) { outputStream.write(bufferOut, 0, bytes); }
	 * in.close(); // 4.将结尾部分输出到微信服务器 byte[] foot = ("\r\n--" + BOUNDARY +
	 * "--\r\n").getBytes(UTFCODE);// 定义最后数据分隔线 outputStream.write(foot);
	 * outputStream.flush(); outputStream.close(); // 5.将微信服务器返回的输入流转换成字符串
	 * java.io.InputStream inputStream = httpUrlConn.getInputStream();
	 * InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
	 * UTFCODE); BufferedReader bufferedReader = new
	 * BufferedReader(inputStreamReader); String str = null; while ((str =
	 * bufferedReader.readLine()) != null) { buffer.append(str); }
	 * bufferedReader.close(); inputStreamReader.close(); // 释放资源
	 * inputStream.close(); inputStream = null; httpUrlConn.disconnect(); } catch
	 * (IOException e) { System.out.println("发送POST请求出现异常！" + e);
	 * e.printStackTrace(); } return buffer.toString(); }
	 */

}
