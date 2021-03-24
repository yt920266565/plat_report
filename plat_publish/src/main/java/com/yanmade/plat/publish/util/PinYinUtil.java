package com.yanmade.plat.publish.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class PinYinUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PinYinUtil.class);
	
	private static String mailSuffix = "@yanmade.com";
	
	private PinYinUtil() {
		
	}
	
	/**中文转拼音,加上@yanmade.com，组成邮箱
	 * @param obj
	 * @return
	 */
	public static String chToPinYin(String obj) {
		try {
			return PinyinHelper.convertToPinyinString(obj, "", PinyinFormat.WITHOUT_TONE)+mailSuffix; 
		} catch (PinyinException e) {
			logger.error("汉字转换拼音失败,{}",e.getMessage());
		}
        return null;
	}

}
