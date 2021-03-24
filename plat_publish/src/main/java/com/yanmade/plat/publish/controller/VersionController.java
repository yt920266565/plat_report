package com.yanmade.plat.publish.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yanmade.plat.framework.entity.ApiResponse;
import com.yanmade.plat.framework.enums.ErrMsgEnum;
import com.yanmade.plat.framework.service.UserService;
import com.yanmade.plat.framework.util.ApiResponseUtil;
import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.entity.VerMain;
import com.yanmade.plat.publish.service.FtpService;
import com.yanmade.plat.publish.service.MydoneService;
import com.yanmade.plat.publish.service.VersionService;
import com.yanmade.plat.publish.util.PhoneSendUtil;

@RestController
@RequestMapping("/versions")
public class VersionController {
	private static final Logger log = LoggerFactory.getLogger(VersionController.class);
	private static final String COUNT = "count";
	private static final String DATA = "data";
	private static final String MSG = "msg";

	@Autowired
	VersionService service;

	@Autowired
	MydoneService mydoneService;

	@Autowired
	UserService userService;

	@Autowired
	VerMainMapper mapper;

	@Autowired
	private VerMain vermain;

	@Autowired
	PhoneSendUtil phoneSendUtil;

	// 获取erp_dictionary数据
	@GetMapping("/erpDict")
	public Map<String, Object> getErpDict() {
		List<HashMap<String, Object>> list = service.getErpDic();
		Map<String, Object> map = new HashMap<>();
		map.put(DATA, list);
		return map;
	}

	// 版本申请
	@PostMapping("/apply")
	public ApiResponse<VerMain> insertVersion(@RequestBody VerMain ver) {
		int count = service.verIsRept(ver);
		if (count > 0) {
			return ApiResponseUtil.failure(HttpStatus.ALREADY_REPORTED, ver, ErrMsgEnum.NOT_INSERT);
		}
		boolean result = service.insert(ver);
		if (!result) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_INSERT);
		}

		return ApiResponseUtil.success(ver);
	}

	// 删除我创建的
	@DeleteMapping("/{flowNo}")
	public ApiResponse<Object> delete(@PathVariable String flowNo) {
		boolean result = service.delete(flowNo);
		if (!result) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, null, ErrMsgEnum.NOT_DELETE);
		}

		return ApiResponseUtil.success(null);
	}

	// 版本构建
	@PutMapping("/{flowNo}/build")
	public ApiResponse<Object> updateApply(@RequestBody VerMain ver, @PathVariable String flowNo) {
		ver.setFlowNo(flowNo);
		if (ver.getStatus() == 6) {
			// 临时版本构建完成直接发布
			String saleInfo = ver.getSaleInfo();
			boolean result = service.insertPub(ver, saleInfo);
			if (!result) {
				return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_UPDATE);
			}
			return ApiResponseUtil.success(ver);
		}
		boolean upresult = service.update(ver);

		if (!upresult) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_UPDATE);
		}

		return ApiResponseUtil.success(ver);

	}

	// 版本审批
	@PutMapping("/{flowNo}/approve")
	public ApiResponse<Object> approve(@RequestBody VerMain ver, @PathVariable String flowNo) {
		return updateApply(ver, flowNo);
	}

	// 版本测试
	@PutMapping("/{flowNo}/test")
	public ApiResponse<Object> updateTest(@RequestBody VerMain ver, @PathVariable String flowNo) {
		// return updateApply(ver, flowNo);
		// 测试完成直接发布
		ver.setFlowNo(flowNo);
		int status = ver.getStatus();
		boolean result = true;
		if (status == 0) {
			result = service.insertOver(ver);
		} else {
			String saleInfo = ver.getSaleInfo();
			result = service.insertPub(ver, saleInfo);
		}
		if (!result) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_INSERT);
		}
		return ApiResponseUtil.success(ver);
	}

	// 版本发布
	@PostMapping("/{flowNo}/{sendSale}/publish")
	public ApiResponse<Object> updatePub(@RequestBody VerMain ver, @PathVariable String flowNo,
			@PathVariable String sendSale) {
		ver.setFlowNo(flowNo);
		int status = ver.getStatus();
		boolean result = true;
		if (status == 102) {
			result = service.insertOver(ver);
		} else {
			result = service.insertPub(ver, sendSale);
		}

		if (!result) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_INSERT);
		}
		return ApiResponseUtil.success(ver);

	}

	// 已发布版本查询
	@GetMapping("/published")
	public ApiResponse<List<HashMap<String, Object>>> getPublished(@RequestParam Map<String, Object> input) {
		List<HashMap<String, Object>> list = service.getPublished(input);
		int count = service.getPubCnt(input);
		return ApiResponseUtil.success(list, count);
	}

	// 可维护版本查询
	@GetMapping("/maintenance")
	public ApiResponse<List<HashMap<String, Object>>> getMaintain(@RequestParam Map<String, Object> input) {
		List<HashMap<String, Object>> list = service.getMaintain(input);
		int count = service.getMaintainCnt(input);
		return ApiResponseUtil.success(list, count);
	}

	// 版本维护
	@PostMapping("/{flowNo}/maintenance")
	public ApiResponse<Object> verMaintain(@RequestBody VerMain ver, @PathVariable String flowNo) {
		ver.setFlowNo(flowNo);
		Map<String, Object> map = mydoneService.cheVerId(ver);
		int count = Integer.parseInt(map.get(COUNT).toString());
		String mapflowNo = "";
		if (count != 0) {
			mapflowNo = map.get("flowNo").toString();
		}
		// 检查修改的版本号和软件名称在ver_main表中是否唯一
		if (count == 1 && !flowNo.equals(mapflowNo)) {
			return ApiResponseUtil.failure(HttpStatus.CONFLICT, ver, ErrMsgEnum.FAILURE);
		}
		boolean upresult = service.verMaintain(ver);
		if (!upresult) {
			return ApiResponseUtil.failure(HttpStatus.BAD_REQUEST, ver, ErrMsgEnum.NOT_UPDATE);
		}
		return ApiResponseUtil.success(ver);
	}

	// 列出版本文件
	@GetMapping("/{flowNo}/files")
	public ApiResponse<List<HashMap<String, Object>>> searFile(@PathVariable String flowNo) {
		Map<String, Object> vermap = service.getVerPath(flowNo);
		String verPath = vermap.get("verPath").toString();
		FtpService ftpService = new FtpService();
		List<HashMap<String, Object>> list = ftpService.listPublishFilesByVerDir(verPath);
		return ApiResponseUtil.success(list, list.size());
	}

	// 检查路径下是否有文件
	@GetMapping("/checkPath")
	public ApiResponse<List<HashMap<String, Object>>> checkPath(@RequestParam String verPath) {
		FtpService ftpService = new FtpService();
		List<HashMap<String, Object>> list = ftpService.listPublishFilesByVerDir(verPath);
		return ApiResponseUtil.success(list);
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
		vermain.setFlowNo(flowNo);
		vermain.setName(map.get("name").toString());
		vermain.setResult(Integer.parseInt(map.get("result").toString()));
		vermain.setComment(map.get("comment").toString());
		int currenter = Integer.parseInt(map.get("currenter").toString());
		service.insertOpt(vermain, currenter);
	}

	@GetMapping("/verMain")
	public ApiResponse<List<Map<String, Object>>> getUnpublished() {
		List<Map<String, Object>> list = service.getUnpublished();
		return ApiResponseUtil.success(list);
	}

	@GetMapping("/searchExcel")
	public void searchExcel(@RequestParam(value = "flist") String fstr, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		String[] farray = fstr.split(",");
		List<Object> input = Arrays.asList(farray);
		map.put("flist", input);
		List<HashMap<String, Object>> list = service.getPubExcel(map);
		list = deaList(list);
		list = dealDept(list);
		OutputStream fileOut = null;
		try (Workbook wb = new HSSFWorkbook();) {
			// 导出
			// 重置输出流
			response.reset();
			// 设置导出Excel报表的导出形式
			response.setContentType("application/vnd.ms-excel");
			// 自定义响应文件名
			String fileName = new String(("已发布版本的项目").getBytes("utf-8"), StandardCharsets.ISO_8859_1);
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
			fileOut = response.getOutputStream();
			Sheet sheet = wb.createSheet("sheet1");
			// 设置列宽
			sheet.setColumnWidth(0, 3400);
			sheet.setColumnWidth(1, 3400);
			sheet.setColumnWidth(2, 4400);
			sheet.setColumnWidth(3, 3400);
			sheet.setColumnWidth(4, 3400);
			sheet.setColumnWidth(5, 3400);
			sheet.setColumnWidth(6, 3400);
			sheet.setColumnWidth(7, 3400);
			sheet.setColumnWidth(8, 4000);
			CreationHelper creationHelper = wb.getCreationHelper();
			// 创建行 从 0 开始为第一行

			Row row = sheet.createRow((short) 0);
			row.setHeight((short) 450);// 目的是想把行高设置成25px
			// 创建列 从0 开始为第一列
			// 第一行的数据
			row.createCell(0).setCellValue(creationHelper.createRichTextString("项目名称")

			);
			row.createCell(1).setCellValue(creationHelper.createRichTextString("产品线")

			);
			row.createCell(2).setCellValue(creationHelper.createRichTextString("客户名称")

			);
			row.createCell(3).setCellValue(creationHelper.createRichTextString("软件名称")

			);
			row.createCell(4).setCellValue(creationHelper.createRichTextString("版本号")

			);
			row.createCell(5).setCellValue(creationHelper.createRichTextString("版本范围")

			);
			row.createCell(6).setCellValue(creationHelper.createRichTextString("版本类型")

			);
			row.createCell(7).setCellValue(creationHelper.createRichTextString("料号")

			);
			row.createCell(8).setCellValue(creationHelper.createRichTextString("发布时间")

			);
			int i = 0;
			for (HashMap<String, Object> s : list) {
				Row rowi = sheet.createRow((short) ++i);
				rowi.createCell(0).setCellValue(creationHelper.createRichTextString(s.get("projName").toString()));
				rowi.createCell(1).setCellValue(creationHelper.createRichTextString(s.get("deptId").toString()));
				rowi.createCell(2).setCellValue(creationHelper.createRichTextString(s.get("client").toString()));
				rowi.createCell(3).setCellValue(creationHelper.createRichTextString(s.get("verId").toString()));
				rowi.createCell(4).setCellValue(creationHelper.createRichTextString(s.get("verNo").toString()));
				rowi.createCell(5).setCellValue(creationHelper.createRichTextString(s.get("verlevel").toString()));
				rowi.createCell(6).setCellValue(creationHelper.createRichTextString(s.get("verType").toString()));
				rowi.createCell(7).setCellValue(creationHelper.createRichTextString(s.get("subPartNo").toString()));
				rowi.createCell(8).setCellValue(
						creationHelper.createRichTextString(s.get("publishTime").toString().substring(0, 19)));
			}
			wb.write(fileOut);
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			try {
				fileOut.close();
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}

	}

	@GetMapping(value = "getFlowNo")
	public Map<String, Object> getFlowNo(@RequestParam Map<String, Object> input) {
		Map<String, Object> map = new HashMap<>();
		List<String> list = service.getFlowNo(input);
		map.put(DATA, list);
		map.put(MSG, "success");
		return map;
	}

	private List<HashMap<String, Object>> deaList(List<HashMap<String, Object>> list) {
		String sverLevel = "verlevel";
		String sverType = "verType";
		for (HashMap<String, Object> l : list) {
			int verlevel = Integer.parseInt(l.get(sverLevel).toString());
			int verType = Integer.parseInt(l.get(sverType).toString());

			if (verlevel == 1) {
				l.put(sverLevel, "产品");
			} else if (verlevel == 2) {
				l.put(sverLevel, "上位机");
			} else {
				l.put(sverLevel, "下位机");
			}
			if (verType == 1) {
				l.put(sverType, "正式版本");
			} else {
				l.put(sverType, "临时版本");
			}
		}
		return list;
	}

	private List<HashMap<String, Object>> dealDept(List<HashMap<String, Object>> list) {
		String sdeptId = "deptId";
		for (HashMap<String, Object> l : list) {
			int deptId = Integer.parseInt(l.get(sdeptId).toString());
			if (deptId == 132) {
				l.put(sdeptId, "产品二部");
			} else if (deptId == 134) {
				l.put(sdeptId, "产品一部");
			} else if (deptId == 133) {
				l.put(sdeptId, "产品三部");
			} else if (deptId == 135) {
				l.put(sdeptId, "产品四部");
			} else if (deptId == 162) {
				l.put(sdeptId, "产品五部");
			} else if (deptId == 136) {
				l.put(sdeptId, "派科斯");
			} else if (deptId == 155) {
				l.put(sdeptId, "软件部");
			}
		}
		return list;
	}

	@GetMapping("/getCurrenter")
	public Map<String, Object> getCurrenter() {
		Map<String, Object> map = new HashMap<>();
		map.put(DATA, getCurrenterId());
		map.put(MSG, "success");
		return map;
	}

	// 获取当前登录的用户id
	private int getCurrenterId() {
		String loginString = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return mapper.getIdByName(loginString);
	}

}
