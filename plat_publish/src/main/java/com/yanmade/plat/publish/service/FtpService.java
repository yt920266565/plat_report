package com.yanmade.plat.publish.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 使用FTP服务的版本文件服务实现
 * 
 * @author 0103307
 *
 */
@Service
public class FtpService {
	private static final Logger logger = LoggerFactory.getLogger(FtpService.class);
	private FTPClient ftpClient;
	private static final String USERNAME = "software";
	private static final String PASSWORD = "N8#mRm2b";
	private static final int PORT = 21;
	private static final String IP = "192.168.0.160";

	/**
	 * 
	 * 返回当前ftp目录下所有发布文件信息存入实体list
	 * 
	 * @throws IOException
	 */
	public List<HashMap<String, Object>> listPublishFilesByVerDir(String ftpPath) {
		if (Objects.isNull(connFtpServer())) {
			return new ArrayList<>();
		}
		List<HashMap<String, Object>> fNameList = new ArrayList<>();
		ftpClient.setControlEncoding("GBK");
		try {
			FTPFile[] ftpFiles = ftpClient.listFiles(new String(ftpPath.getBytes("GBK"), StandardCharsets.ISO_8859_1));
			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].isDirectory()) {
					continue;
				}
				HashMap<String, Object> map = new HashMap<>();
				String fileName = ftpFiles[i].getName();
				Date editTime = ftpFiles[i].getTimestamp().getTime();
				long size = ftpFiles[i].getSize();
				int ksize = (int) ((size / 1024) == 0 ? 1 : (size / 1024));
				map.put("fileName", fileName);
				map.put("editTime", editTime);
				map.put("size", ksize);
				if (!map.isEmpty()) {
					fNameList.add(map);
				}
			}
		} catch (IOException e) {
			logger.error("获取ftp文件目录失败", e);
			return new ArrayList<>();
		}
		return fNameList;
	}

	public boolean downloadFile(String remoteFileDir, HttpServletResponse response, String fileName) {
		if (Objects.isNull(connFtpServer())) {
			return false;
		}
		ftpClient.setControlEncoding("GBK");
		try (OutputStream out = response.getOutputStream();) {
			if (!ftpClient
					.changeWorkingDirectory(new String(remoteFileDir.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
				logger.error("Failed to changeWorkingDirectory! remoteFileDir={}", remoteFileDir);
				return false;
			}
			fileName = new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1);
			fileWrite(ftpClient, response, fileName);
			return true;
		} catch (IOException e) {
			logger.error("Failed to download file! filename{}", fileName, e);
		}

		return false;
	}

	public boolean downloadAll(String remoteFileDir, String[] files, String zipName, HttpServletResponse response) {
		if (Objects.isNull(connFtpServer())) {
			return false;
		}
		ftpClient.setControlEncoding("GBK");
		
		BufferedInputStream bis = null ;
		String zipFilePath = "D:\\Tomcat\\apache-publish-8085\\app\\zip" + File.separator + zipName;
		Path zipPath = Paths.get(zipFilePath);
		File zip = new File(zipFilePath);
		try (OutputStream out = response.getOutputStream();) {
			if (ftpClient
					.changeWorkingDirectory(new String(remoteFileDir.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
				response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
				response.setContentType("application/octet-stream");

				// 压缩文件
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
				zipFile(ftpClient, files, zos);
				zos.close();

				// 读取压缩多文件并返回给reponse的输出流
				bis = new BufferedInputStream(new FileInputStream(new File(zipFilePath)));
				byte[] buff = new byte[1024 * 1024];
				int len = 0;
				while ((len = bis.read(buff)) > 0) {
					out.write(buff, 0, len);
					out.flush();
				}

				return true;
			} else {
				logger.error("Failed to changeWorkingDirectory! remoteFileDir={}", remoteFileDir);
			}
		} catch (IOException e) {
			logger.error("Failed to download file! zipName={}", zipName, e);
		} finally {
			if(bis != null) {
				try {
					bis.close();
					// 读取的文件流返回后，再删除压缩包
					if (zip.exists() && zip.isFile()) {
						Files.delete(zipPath);
					}
				} catch (IOException e) {
					logger.error("Failed to delete file! zipName{}", zipName, e);
				}
			}
		}

		return false;
	}

	private String zipFile(FTPClient ftpCLient, String[] files, ZipOutputStream zos) throws IOException {
		for (String file : files) {
			// 页面传过来的值是转码过的
			file = URLDecoder.decode(file, "utf-8");
			int size = 0;
			byte[] buffer = new byte[1024 * 1024]; // 设置读取数据缓存大小
			try (InputStream is = ftpCLient
					.retrieveFileStream(new String(file.getBytes("GBK"), StandardCharsets.ISO_8859_1));
					BufferedInputStream bis = new BufferedInputStream(is);) {
				// 将文件写入zip内，即将文件进行打包
				zos.putNextEntry(new ZipEntry(file));
				while ((size = bis.read(buffer)) > 0) {
					zos.write(buffer, 0, size);
				}
				// 循环调用ftpCLient.retrieveFileStream必须加这个方法，否则后面返回的输入流会为null
				ftpClient.completePendingCommand();
			} catch (Exception e) {
				logger.error("多文件压缩流异常{}", e.getMessage());
			}

		}
		return null;
	}

	private void fileWrite(FTPClient ftpClient, HttpServletResponse response, String file) throws IOException {
		response.addHeader("Content-Disposition", "attachment;filename=" + file);
		response.setContentType("application/octet-stream");
		int len = 0;
		byte[] buffer = new byte[1024 * 1024];
		try (InputStream is = ftpClient.retrieveFileStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());) {
			while ((len = bis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
				out.flush();
			}
		} catch (Exception e) {
			logger.error("单文件下载流异常{}", e.getMessage());
		}

	}

	private FTPClient connFtpServer() {
		if (Objects.nonNull(ftpClient)) {
			return ftpClient;
		}

		FTPClient client = new FTPClient();
		try {
			client.connect(IP, PORT);
			if (!client.login(USERNAME, PASSWORD)) {
				logger.error("Failed to login ftp server! ip={},port={},username={}", IP, PASSWORD, USERNAME);
				return null;
			}
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
		} catch (IOException e) {
			logger.error("Failed to connect ftp", e);
			return null;
		}
		ftpClient = client;
		return client;
	}

	/**
	 * 关闭FTP连接
	 */
	public void closeFtpClient() {
		if (ftpClient != null) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error("Failed to connectFtp{}", e);
			}
		}
	}

	public void close() {
		closeFtpClient();
	}

}
