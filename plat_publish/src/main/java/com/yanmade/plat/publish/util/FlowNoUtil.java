package com.yanmade.plat.publish.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yanmade.plat.framework.util.RedisUtil;
import com.yanmade.plat.publish.entity.VerMain;

@Component
public class FlowNoUtil {

	private FlowNoUtil() {

	}

	@Autowired
	RedisUtil redisUtil;
	
	public String getMainFlowNo(VerMain ver) {
		String date = getDateString();
		String key = getFlowNoKey("FLOWNO:MAIN:");
		int value = redisUtil.increment(key);

		String verType = ver.getVerType() == 1 ? "OV" : "TV";
		return verType + date + filling(5, value);
	}

	public String getOperatorFlowNo() {
		String date = getDateString();
		String key = getFlowNoKey("FLOWNO:OP:");
		int value = redisUtil.increment(key);

		return "OP" + date + filling(5, value);
	}

	public String getFlowNoKey(String prefix) {
		String date = getDateString();
		String key = prefix + date;
		redisUtil.expire(key, 24, TimeUnit.HOURS);
		return key;
	}

	public String getDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(new Date());
	}

	/**
	 * number前面补0,补齐到length长度
	 * 
	 * @param length
	 * @param number
	 * @return
	 */
	public String filling(int length, int number) {
		String f = "%0" + length + "d";
		return String.format(f, number);
	}

}
