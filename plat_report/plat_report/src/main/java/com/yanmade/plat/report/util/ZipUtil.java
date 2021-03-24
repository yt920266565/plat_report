package com.yanmade.plat.report.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtil {
	
	private ZipUtil() {
		
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);
	private static TreeMap<String, String> treeMap;
	
	@SuppressWarnings("rawtypes")
	public static void unZipFile(ZipFile zip, String baseDir) throws IOException {
		treeMap = new TreeMap<>();
		
		//entries()返回ZIP文件条目中的枚举
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			
			//判断是否是文件，如果文件名有/，zipEntryName会解析成错误的名字
			String separator = "/";
			String isfile = zipEntryName.substring(zipEntryName.lastIndexOf(separator) + 1);
			if(StringUtils.isEmpty(isfile)) {
				continue;
			}
			
			//zipEntryName = zipEntryName.substring(zipEntryName.indexOf(separator));
			Pattern numberPattern = Pattern.compile("\\d+");
			Matcher matcher = numberPattern.matcher(zipEntryName);
			//匹配路径中20201108这种数字，存入treeMap中之后自动升序排序
			if(matcher.find()) {
				treeMap.put(matcher.group(), matcher.group());
			}

			String outPath = baseDir+File.separator+zipEntryName;
			File mkdir = new File(outPath);
			if(!mkdir.getParentFile().exists() && !mkdir.getParentFile().mkdir()) {
				continue;
			}
			try (InputStream in = zip.getInputStream(entry);
					BufferedInputStream bis = new BufferedInputStream(in);
					OutputStream out = new FileOutputStream(new File(outPath));) {
				
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} catch (Exception e) {
				logger.error("解压文件异常,原因{}",e.getMessage());
			}
		}
		zip.close();
	}
	
	public static Map<String, String> geTreeMap(){
		return treeMap;
	}

}
