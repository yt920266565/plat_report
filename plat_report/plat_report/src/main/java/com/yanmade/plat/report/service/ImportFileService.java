package com.yanmade.plat.report.service;

import com.yanmade.plat.report.dao.ImportFileMapper;
import com.yanmade.plat.report.dao.SummaryDayMapper;
import com.yanmade.plat.report.dao.SummaryDutyMapper;
import com.yanmade.plat.report.dao.SummaryHourMapper;
import com.yanmade.plat.report.entity.ImportFileDto;
import com.yanmade.plat.report.entity.ImportResultDto;
import com.yanmade.plat.report.entity.ImportFileLog;
import com.yanmade.plat.report.entity.SummaryDay;
import com.yanmade.plat.report.entity.SummaryDuty;
import com.yanmade.plat.report.entity.SummaryHour;
import com.yanmade.plat.report.filter.FolderFilter;
import com.yanmade.plat.report.util.CsvUtil;
import com.yanmade.plat.report.util.DateUtil;
import com.yanmade.plat.report.util.ZipUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 0103379
 */
@Component
public class ImportFileService {

	private static final Logger logger = LoggerFactory.getLogger(ImportFileService.class);
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT = "yyyyMMdd";
	public static final String PATH_ERROR = "路径不正确";

	@Autowired
	private ImportFileMapper mapper;

	@Autowired
	private SummaryDayMapper summaryDayMapper;

	@Autowired
	private SummaryDutyMapper summaryDutyMapper;

	@Autowired
	private SummaryHourMapper summaryHourMapper;

	@Autowired
	private ImportFileLogService importFileLogService;

	public String importFileByFolderPath(ImportFileDto importFileDto) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		if (importFileDto.isByDay()) {
			ImportResultDto importResultDto1 = importDayFileByFolderPath(importFileDto);
			if (!importResultDto1.getMessage().isEmpty()) {
				sb.append(importResultDto1.getMessage() + ",");
			}
			count += importResultDto1.getCount();
		}

		if (importFileDto.isByDuty()) {
			ImportResultDto importResultDto2 = importDutyFileByFolderPath(importFileDto);
			if (!importResultDto2.getMessage().isEmpty()) {
				sb.append(importResultDto2.getMessage() + ",");
			}
			count += importResultDto2.getCount();
		}

		if (importFileDto.isByHour()) {
			ImportResultDto importResultDto3 = importHourFileByFolderPath(importFileDto);
			if (!importResultDto3.getMessage().isEmpty()) {
				sb.append(importResultDto3.getMessage() + ",");
			}
			count += importResultDto3.getCount();
		}

		// 记录导入日志
		ImportFileLog importFileLog = new ImportFileLog();
		importFileLog.setCount(count);
		importFileLog.setAuto(importFileDto.isAuto());
		if (importFileLog.isAuto()) {
			importFileLog.setUsername("系统");
		} else {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				String username = ((UserDetails) principal).getUsername();
				importFileLog.setUsername(username);
			} else {
				importFileLog.setUsername((String) principal);
			}
		}
		importFileLog.setCreatetime(new Date());
		importFileLogService.insertLog(importFileLog);

		return sb.toString();
	}

	/**
	 * 导入按班次汇总报表
	 * 
	 * @param importFileDto
	 * @return
	 */
	public ImportResultDto importDutyFileByFolderPath(ImportFileDto importFileDto) {
		ImportResultDto importResultDto = new ImportResultDto();
		String path = importFileDto.getFolderPath();
		String startDate = DateUtil.dateFormat(importFileDto.getStartDate(), FORMAT);
		String endDate = DateUtil.dateFormat(importFileDto.getEndDate(), FORMAT);

		if (StringUtils.isEmpty(path)) {
			logger.info("文件夹路径为空，请填写正确的文件夹路径");
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage("文件夹路径不能为空");
			return importResultDto;
		}

		logger.info("文件夹路径：{}", path);
		String folderPath = path + "\\summaryDuty";
		File folder = new File(folderPath);
		if (!folder.isDirectory()) {
			logger.info("{}期望是文件夹", folderPath);
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage(path + PATH_ERROR);
			return importResultDto;
		}
		List<SummaryDuty> list = new ArrayList<>();
		try {
			FolderFilter folderFilter = new FolderFilter();
			// 产品类型文件夹
			File[] typeFolders = folder.listFiles(folderFilter);
			for (File typeFolder : typeFolders) {

				// 日期文件夹
				File[] dateFiles = typeFolder.listFiles(folderFilter);
				for (File dateFile : dateFiles) {
					String date = dateFile.getName();

					// 日期符合条件的文件夹才会插入
					if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
						// 要解析的文件
						File[] parsedFiles = dateFile.listFiles();
						for (File parsedFile : parsedFiles) {
							List<SummaryDuty> summaryDaties = parseDutyFiles(parsedFile, importFileDto);
							list.addAll(summaryDaties);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("遍历文件夹失败", e);
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage("导入按班次统计报表失败");
			return importResultDto;
		}

		logger.info("当前导入班次报表数据总共:{}条", list.size());
		insertBatchSummaryDuty(list);
		logger.info("按班次统计报表导入成功");

		importResultDto.setSuccess(true);
		importResultDto.setCount(list.size());
		importResultDto.setMessage("");
		return importResultDto;
	}

	/**
	 * 导入按天汇总报表
	 * 
	 * @param importFileDto
	 * @return
	 */
	public ImportResultDto importDayFileByFolderPath(ImportFileDto importFileDto) {
		String path = importFileDto.getFolderPath();
		String startDate = DateUtil.dateFormat(importFileDto.getStartDate(), FORMAT);
		String endDate = DateUtil.dateFormat(importFileDto.getEndDate(), FORMAT);

		logger.info("文件夹路径：{}", path);
		ImportResultDto importResultDto = new ImportResultDto();
		if (StringUtils.isEmpty(path)) {
			logger.info("文件夹路径为空，请填写正确的文件夹路径");
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage("路径为空，请填写正确的文件夹路径");
			return importResultDto;
		}

		String folderPath = path + "\\summaryDay";
		File folder = new File(folderPath);
		if (!folder.isDirectory()) {
			logger.info("{}期望是文件夹", path);
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage(path + PATH_ERROR);
			return importResultDto;
		}
		List<SummaryDay> list = new ArrayList<>();
		try {
			// 文件过滤器
			FolderFilter folderFilter = new FolderFilter();

			// 产品类型文件夹
			File[] typeFolders = folder.listFiles(folderFilter);
			for (File typeFolder : typeFolders) {

				// 日期文件夹
				File[] dateFiles = typeFolder.listFiles(folderFilter);
				for (File dateFile : dateFiles) {
					String date = dateFile.getName();

					// 日期符合条件的文件夹才会插入
					if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
						// 要解析的文件
						File[] parsedFiles = dateFile.listFiles();
						for (File parsedFile : parsedFiles) {
							List<SummaryDay> summaryDays = parseDayFiles(parsedFile, importFileDto);
							list.addAll(summaryDays);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("遍历文件夹失败", e);
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage("按天统计报表导入失败");
			return importResultDto;
		}

		logger.info("当前导入按天报表数据总共:{}条", list.size());
		insertBatchSummaryDay(list);
		logger.info("按天统计报表导入成功");

		importResultDto.setSuccess(true);
		importResultDto.setCount(list.size());
		importResultDto.setMessage("");
		return importResultDto;
	}

	/**
	 * 导入按小时汇总报表
	 * 
	 * @param importFileDto
	 * @return
	 */
	public ImportResultDto importHourFileByFolderPath(ImportFileDto importFileDto) {
		String path = importFileDto.getFolderPath();
		String startDate = DateUtil.dateFormat(importFileDto.getStartDate(), FORMAT);
		String endDate = DateUtil.dateFormat(importFileDto.getEndDate(), FORMAT);

		ImportResultDto importResultDto = new ImportResultDto();
		if (StringUtils.isEmpty(path)) {
			logger.info("文件夹路径为空，请填写正确的路径");
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage("文件夹路径为空，请填写正确的路径");
			return importResultDto;
		}

		logger.info("文件夹路径：{}", path);
		String pathname = path + "\\summaryHour";
		File folder = new File(pathname);
		if (!folder.isDirectory()) {
			logger.info("{}期望是一个文件夹", pathname);
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage(path + PATH_ERROR);
			return importResultDto;
		}
		List<SummaryHour> list = new ArrayList<>();
		try {
			FolderFilter folderFilter = new FolderFilter();
			// 产品类型文件夹
			File[] typeFolders = folder.listFiles(folderFilter);
			for (File typeFolder : typeFolders) {

				// 日期文件夹
				File[] dateFiles = typeFolder.listFiles(folderFilter);
				for (File dateFile : dateFiles) {
					String date = dateFile.getName();
					if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
						// 要解析的文件
						File[] parsedFiles = dateFile.listFiles();
						// logger.info("文件夹{}下要解析的文件数量：{}", dateFiles, parsedFiles.length);
						for (File parsedFile : parsedFiles) {
							List<SummaryHour> summaryHours = parseHourFiles(parsedFile, importFileDto);
							list.addAll(summaryHours);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("遍历文件夹失败", e);
			importResultDto.setSuccess(false);
			importResultDto.setCount(0);
			importResultDto.setMessage("导入小时报表失败");
			return importResultDto;
		}

		logger.info("当前导入小时报表数据总共:{}条", list.size());
		insertBatchSummaryHour(list);
		logger.info("按小时统计报表导入成功");

		importResultDto.setSuccess(true);
		importResultDto.setCount(list.size());
		importResultDto.setMessage("");
		return importResultDto;
	}

	private void insertBatchSummaryDay(List<SummaryDay> list) {
		// 一次插入两千条到数据库
		List<SummaryDay> tempList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			tempList.add(list.get(i));
			if ((i % 2000 == 0 && i > 0) || (i == list.size() - 1)) {
				try {
					mapper.insertBatchSummaryDay(tempList);
				} catch (Exception e) {
					logger.error("插入数据异常:{}", e.getMessage());
				}
				tempList.clear();
			}
		}
	}

	private void insertBatchSummaryDuty(List<SummaryDuty> list) {
		// 一次插入两千条到数据库
		List<SummaryDuty> templist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			templist.add(list.get(i));
			if ((i % 2000 == 0 && i > 0) || (i == list.size() - 1)) {
				try {
					mapper.insertBatchSummaryDuty(templist);
				} catch (Exception e) {
					logger.error("插入数据异常:{}", e.getMessage());
				}
				templist.clear();
			}
		}
	}

	private void insertBatchSummaryHour(List<SummaryHour> list) {
		// 一次插入两千条到数据库
		List<SummaryHour> templist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			templist.add(list.get(i));
			if ((i % 2000 == 0 && i > 0) || (i == list.size() - 1)) {
				try {
					mapper.insertBatchSummaryHour(templist);
				} catch (Exception e) {
					logger.error("插入数据异常:{}", e.getMessage());
				}
				templist.clear();
			}
		}
	}

	private List<SummaryDay> parseDayFiles(File file, ImportFileDto importFileDto) {
		boolean update = importFileDto.isUpdate();
		try {
			List<List<String>> contents = CsvUtil.getCsvDataList(file.getAbsolutePath());
			if (contents.size() <= 1) {
				logger.info("{}文件只有一行", file.getAbsolutePath());
				return new ArrayList<>();
			}

			List<SummaryDay> summaryDays = new ArrayList<>();

			for (int i = 1; i < contents.size(); i++) {
				List<String> content = contents.get(i);
				SummaryDay summaryDay = getSummaryDay(content, importFileDto);

				// 如果勾选了不更新并且数据已存在则直接返回
				if (i == 1 && !update && summaryDayMapper.countSummaryDay(summaryDay) > 0) {
					break;
				}

				summaryDays.add(summaryDay);
			}

			if (summaryDays.isEmpty()) {
				return new ArrayList<>();
			}

			return summaryDays;

		} catch (Exception e) {
			logger.error("解析文件{}失败", file.getName(), e);
			return new ArrayList<>();
		}
	}

	private SummaryDay getSummaryDay(List<String> content, ImportFileDto importFileDto) throws ParseException {
		SummaryDay summaryDay = new SummaryDay();
		summaryDay.setIp(content.get(0));

		// 分割客户区域和车间,先取传进来的workArea作为客户区域参数,第二列格式：苏州维信_A3
		String area = content.get(1);
		String workArea = importFileDto.getWorkArea() == null ? "" : importFileDto.getWorkArea();
		String workshop = "";
		if (StringUtils.isNotBlank(area) && area.contains("_")) {
			String[] arr = area.split("_");
			if (arr != null && arr.length > 1) {
				workArea = arr[0];
				workshop = arr[1];
			}
		}

		summaryDay.setWorkArea(workArea);
		summaryDay.setWorkshop(workshop);
		summaryDay.setProductType(content.get(2));
		summaryDay.setPartName(content.get(3));
		summaryDay.setTestType(content.get(4));
		summaryDay.setMachineType(content.get(5));
		summaryDay.setMachineId(content.get(6));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
		summaryDay.setDutyDate(simpleDateFormat.parse(content.get(7)));
		summaryDay.setWaitDuaration(Long.parseLong(content.get(8)));
		summaryDay.setStopDuaration(Long.parseLong(content.get(9)));
		summaryDay.setTotalCount(Integer.parseInt(content.get(10)));
		summaryDay.setFirstPassRatio(Float.parseFloat(content.get(11)));
		summaryDay.setFirstPassCount(Integer.parseInt(content.get(13)));
		summaryDay.setRetestPassCount(Integer.parseInt(content.get(14)));

		if (content.size() > 15) {
			if (content.get(15).length() >= 1000) {
				summaryDay.setTop1NgItem(content.get(15).substring(0, 999));
			} else {
				summaryDay.setTop1NgItem(content.get(15));
			}

			if (StringUtils.isNotEmpty(content.get(16))) {
				summaryDay.setTop1NgCount(Integer.parseInt(content.get(16)));
			}
		}
		if (content.size() > 18) {
			if (content.get(18).length() >= 1000) {
				summaryDay.setTop2NgItem(content.get(18).substring(0, 999));
			} else {
				summaryDay.setTop2NgItem(content.get(18));
			}

			if (StringUtils.isNotEmpty(content.get(19))) {
				summaryDay.setTop2NgCount(Integer.parseInt(content.get(19)));
			}
		}

		if (content.size() > 21) {
			if (content.get(21).length() >= 1000) {
				summaryDay.setTop3NgItem(content.get(21).substring(0, 999));
			} else {
				summaryDay.setTop3NgItem(content.get(21));
			}

			if (StringUtils.isNotEmpty(content.get(22))) {
				summaryDay.setTop3NgCount(Integer.parseInt(content.get(22)));
			}
		}

		if (content.size() > 24) {
			summaryDay.setTop1AlarmItem(content.get(24));
			if (StringUtils.isNotEmpty(content.get(25))) {
				summaryDay.setTop1AlarmCount(Integer.parseInt(content.get(25)));
			}
		}
		if (content.size() > 26) {
			summaryDay.setTop2AlarmItem(content.get(26));
			if (StringUtils.isNotEmpty(content.get(27))) {
				summaryDay.setTop2AlarmCount(Integer.parseInt(content.get(27)));
			}
		}
		if (content.size() > 28) {
			summaryDay.setTop3AlarmItem(content.get(28));
			if (StringUtils.isNotEmpty(content.get(29))) {
				summaryDay.setTop3AlarmCount(Integer.parseInt(content.get(29)));
			}
		}

		summaryDay.setCreatetime(new Date());
		return summaryDay;
	}

	private List<SummaryDuty> parseDutyFiles(File file, ImportFileDto importFileDto) {
		boolean update = importFileDto.isUpdate();
		try {
			List<List<String>> contents = CsvUtil.getCsvDataList(file.getAbsolutePath());
			if (contents.size() <= 1) {
				logger.info("{}文件只有一行", file.getAbsolutePath());
				return new ArrayList<>();
			}

			List<SummaryDuty> summaryDuties = new ArrayList<>();
			// 第一行是标题，不进行遍历
			for (int i = 1; i < contents.size(); i++) {
				List<String> content = contents.get(i);
				SummaryDuty summaryDuty = getSummaryDuty(content, importFileDto);

				summaryDuty.setCreatetime(new Date());

				// 查询第一行数据有没有存在，如果存在则当前文件不导入
				if (i == 1 && !update && summaryDutyMapper.countSummaryDuty(summaryDuty) > 0) {
					// logger.info("数据已存在，当前文件{}不会导入", file.getName());
					break;
				}

				summaryDuties.add(summaryDuty);
			}

			if (summaryDuties.isEmpty()) {
				return new ArrayList<>();
			}
			return summaryDuties;
			// mapper.insertBatchSummaryDuty(summaryDuties);

			// logger.info("文件{}数据插入成功", file.getName());
		} catch (Exception e) {
			logger.error("解析文件{}失败", file.getName(), e);
			return new ArrayList<>();
		}
	}

	private SummaryDuty getSummaryDuty(List<String> content, ImportFileDto importFileDto) throws ParseException {
		SummaryDuty summaryDuty = new SummaryDuty();
		summaryDuty.setIp(content.get(0));

		// 分割客户区域和车间,先取传进来的workArea作为客户区域参数,第二列格式：苏州维信_A3
		String area = content.get(1);
		String workArea = importFileDto.getWorkArea() == null ? "" : importFileDto.getWorkArea();
		String workshop = "";
		if (StringUtils.isNotBlank(area) && area.contains("_")) {
			String[] arr = area.split("_");
			if (arr != null && arr.length > 1) {
				workArea = arr[0];
				workshop = arr[1];
			}
		}

		summaryDuty.setWorkArea(workArea);
		summaryDuty.setWorkshop(workshop);
		summaryDuty.setProductType(content.get(2));
		summaryDuty.setPartName(content.get(3));
		summaryDuty.setTestType(content.get(4));
		summaryDuty.setMachineType(content.get(5));
		summaryDuty.setMachineId(content.get(6));

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
		summaryDuty.setDutyDate(simpleDateFormat.parse(content.get(7)));
		summaryDuty.setDuty("白班".equals(content.get(8)) ? 1 : 0);
		summaryDuty.setWaitDuaration(Long.parseLong(content.get(9)));
		summaryDuty.setStopDuaration(Long.parseLong(content.get(10)));
		summaryDuty.setTotalCount(Integer.parseInt(content.get(11)));
		summaryDuty.setFirstPassRatio(Float.parseFloat(content.get(12)));

		summaryDuty.setFirstPassCount(Integer.parseInt(content.get(14)));
		summaryDuty.setRetestPassCount(Integer.parseInt(content.get(15)));

		if (content.size() > 16) {
			if (content.get(16).length() < 1000) {
				summaryDuty.setTop1NgItem(content.get(16));
			} else {
				summaryDuty.setTop1NgItem(content.get(16).substring(0, 999));
			}

			if (StringUtils.isNotEmpty(content.get(17))) {
				summaryDuty.setTop1NgCount(Integer.parseInt(content.get(17)));
			}
		}

		if (content.size() > 19) {
			if (content.get(19).length() < 1000) {
				summaryDuty.setTop2NgItem(content.get(19));
			} else {
				summaryDuty.setTop2NgItem(content.get(19).substring(0, 999));
			}

			if (StringUtils.isNotEmpty(content.get(20))) {
				summaryDuty.setTop2NgCount(Integer.parseInt(content.get(20)));
			}
		}

		if (content.size() > 22) {
			if (content.get(22).length() < 1000) {
				summaryDuty.setTop3NgItem(content.get(22));
			} else {
				summaryDuty.setTop3NgItem(content.get(22).substring(0, 999));
			}

			if (StringUtils.isNotEmpty(content.get(23))) {
				summaryDuty.setTop3NgCount(Integer.parseInt(content.get(23)));
			}
		}

		if (content.size() > 25) {
			summaryDuty.setTop1AlarmItem(content.get(25));
			if (StringUtils.isNotEmpty(content.get(26))) {
				summaryDuty.setTop1AlarmCount(Integer.parseInt(content.get(26)));
			}
		}
		if (content.size() > 27) {
			summaryDuty.setTop2AlarmItem(content.get(27));
			if (StringUtils.isNotEmpty(content.get(28))) {
				summaryDuty.setTop2AlarmCount(Integer.parseInt(content.get(28)));
			}
		}
		if (content.size() > 29) {
			summaryDuty.setTop3AlarmItem(content.get(29));
			if (StringUtils.isNotEmpty(content.get(30))) {
				summaryDuty.setTop3AlarmCount(Integer.parseInt(content.get(30)));
			}
		}
		return summaryDuty;
	}

	private List<SummaryHour> parseHourFiles(File file, ImportFileDto importFileDto) throws ParseException {
		boolean update = importFileDto.isUpdate();
		try {
			List<List<String>> contents = CsvUtil.getCsvDataList(file.getAbsolutePath());
			if (contents.size() <= 1) {
				logger.info("{}文件只有一行", file.getAbsolutePath());
				return new ArrayList<>();
			}

			List<SummaryHour> summaryHours = new ArrayList<>();

			for (int i = 1; i < contents.size(); i++) {
				List<String> content = contents.get(i);
				SummaryHour summaryHour = getSummaryHour(content, importFileDto);
				if (i == 1 && !update && summaryHourMapper.countSummaryHour(summaryHour) > 0) {
					// logger.info("数据已存在，当前文件{}不会导入", file.getName());
					break;
				}

				summaryHours.add(summaryHour);
			}

			if (summaryHours.isEmpty()) {
				return new ArrayList<>();
			}
			return summaryHours;

			// logger.info("文件{}数据插入成功", file.getName());
		} catch (Exception e) {
			logger.error("解析文件{}失败", file.getName(), e);
		}

		return new ArrayList<>();

	}

	private SummaryHour getSummaryHour(List<String> content, ImportFileDto importFileDto) throws ParseException {
		SummaryHour summaryHour = new SummaryHour();
		summaryHour.setIp(content.get(0));

		// 分割客户区域和车间,先取传进来的workArea作为客户区域参数,第二列格式：苏州维信_A3
		String area = content.get(1);
		String workArea = importFileDto.getWorkArea() == null ? "" : importFileDto.getWorkArea();
		String workshop = "";
		if (StringUtils.isNotBlank(area) && area.contains("_")) {
			String[] arr = area.split("_");
			if (arr != null && arr.length > 1) {
				workArea = arr[0];
				workshop = arr[1];
			}
		}

		summaryHour.setWorkArea(workArea);
		summaryHour.setWorkshop(workshop);
		summaryHour.setProductType(content.get(2));
		summaryHour.setPartName(content.get(3));
		summaryHour.setTestType(content.get(4));
		summaryHour.setMachineType(content.get(5));
		summaryHour.setMachineId(content.get(6));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
		summaryHour.setDutyDate(simpleDateFormat.parse(content.get(7)));
		summaryHour.setDuty("白班".equals(content.get(8)) ? 1 : 0);
		summaryHour.setTestDate(simpleDateFormat.parse(content.get(9)));
		summaryHour.setTestHour(Integer.parseInt(content.get(10)));
		summaryHour.setUphTotalCount(Integer.parseInt(content.get(11)));
		summaryHour.setUphPassCount(Integer.parseInt(content.get(12)));
		summaryHour.setWaitDuaration(Long.parseLong(content.get(13)));
		summaryHour.setStopDuaration(Long.parseLong(content.get(14)));

		summaryHour.setTotalCount(Integer.parseInt(content.get(15)));
		summaryHour.setFirstPassRatio(Float.parseFloat(content.get(16)));
		summaryHour.setFirstPassCount(Integer.parseInt(content.get(17)));
		summaryHour.setRetestPassCount(Integer.parseInt(content.get(18)));

		if (content.size() > 20) {
			if (content.get(20).length() < 1000) {
				summaryHour.setTop1NgItem(content.get(20));
			} else {
				summaryHour.setTop1NgItem(content.get(20).substring(0, 999));
			}

			if (StringUtils.isNotEmpty(content.get(21))) {
				summaryHour.setTop1NgCount(Integer.parseInt(content.get(21)));
			}
		}
		if (content.size() > 23) {
			if (content.get(23).length() < 1000) {
				summaryHour.setTop2NgItem(content.get(23));
			} else {
				summaryHour.setTop2NgItem(content.get(23).substring(0, 999));
			}

			if (StringUtils.isNotEmpty(content.get(24))) {
				summaryHour.setTop2NgCount(Integer.parseInt(content.get(24)));
			}
		}
		if (content.size() > 26) {
			if (content.get(26).length() < 1000) {
				summaryHour.setTop3NgItem(content.get(26));
			} else {
				summaryHour.setTop3NgItem(content.get(26).substring(0, 999));
			}

			if (StringUtils.isNotEmpty(content.get(27))) {
				summaryHour.setTop3NgCount(Integer.parseInt(content.get(27)));
			}
		}

//		summaryHour.setTop1AlarmItem("");
//		summaryHour.setTop1AlarmCount(0);
//		summaryHour.setTop2AlarmItem("");
//		summaryHour.setTop2AlarmCount(0);
//		summaryHour.setTop3AlarmItem("");
//		summaryHour.setTop3AlarmCount(0);

		summaryHour.setCreatetime(new Date());
		return summaryHour;
	}

	/**上传zip文件到application.yaml里面的import.path路径里面
	 * 解压之后删除该文件，最后将zip里面的数据插入到数据库表
	 * @param request
	 * @param uploadPath
	 * @return true成功 false失败
	 */
	public boolean uploadZipFile(HttpServletRequest request, String uploadPath) {
		String originName = null;
		File targetFile = null;
		try {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
			Iterator<String> fileNames = mreq.getFileNames();
			MultipartFile file = null;
			while (fileNames.hasNext()) {
				String fileName = fileNames.next();
				file = mreq.getFile(fileName);
			}
			
			if(Objects.isNull(file) || StringUtils.isEmpty(file.getOriginalFilename())) {
				return false;
			}
			originName = file.getOriginalFilename();
			targetFile = new File(uploadPath, originName);
			file.transferTo(targetFile);
		} catch (Exception e) {
			logger.error("上传zip包失败,文件名{},失败原因,{}", originName, e.getMessage());
			return false;
		}

		Map<String, String> treeMap = new TreeMap<>();
		try(ZipFile zip = new ZipFile(targetFile, Charset.forName("gbk"))){
			
			//解压文件到指定目录
			ZipUtil.unZipFile(zip, uploadPath);
			treeMap = ZipUtil.geTreeMap();
		} catch (Exception e) {
			logger.error("读取压缩文件异常,原因{}", e.getMessage());
			return false;
		} finally {
			Path path = Paths.get(targetFile.getAbsolutePath());
			try {
				Files.delete(path);
			} catch (IOException e) {
				logger.error("删除文件失败,原因{}", e.getMessage());
			}
		}
		importAfterUpload(treeMap,uploadPath);
		return true;
	}

	// 导入上传的数据到数据库
	private void importAfterUpload(Map<String, String> treeMap,String uploadPath) {
		if (treeMap.isEmpty()) {
			return;
		}
		//按时间从小到大排序之后放入list 取出最大值和最小值作为插入的起止时间
		ArrayList<String> list = new ArrayList<>(treeMap.values());
		String startDate = numberToDate(list.get(0));
		String endDate = numberToDate(list.get(list.size()-1));
		
		ImportFileDto dto = new ImportFileDto();
		dto.setAuto(true);
		dto.setByDay(true);
		dto.setByDuty(true);
		dto.setByHour(true);
		dto.setFolderPath(uploadPath);
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		importFileByFolderPath(dto);
	}
	
	//讲20201108转化为2020-11-08
	private String numberToDate(String date) {
		StringBuilder sBuilder = new StringBuilder(date);
		sBuilder.insert(4, '-');
		sBuilder.insert(7, '-');
		return sBuilder.toString();
	}

}
