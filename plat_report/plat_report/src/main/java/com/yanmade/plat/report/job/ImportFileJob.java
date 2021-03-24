package com.yanmade.plat.report.job;

import com.yanmade.plat.report.entity.ImportFileDto;
import com.yanmade.plat.report.service.ImportFileLogService;
import com.yanmade.plat.report.service.ImportFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ImportFileJob {
    private static final Logger logger = LoggerFactory.getLogger(ImportFileJob.class);

    @Autowired
    private ImportFileService service;

    @Autowired
    private ImportFileLogService importFileLogService;

    @Value("${import.path}")
    private String path;

    @Scheduled(cron = "0 30 7 * * ?")
    public void importFileJob(){
        logger.info("导入任务开始，当前时间：{}", LocalDateTime.now());
        if (path == null || path.isEmpty()){
            logger.info("定时导入路径为空，导入任务结束");
            return;
        }

        String startDate = LocalDate.now().toString();
        String endDate = LocalDate.now().plusDays(-1).toString();
        ImportFileDto dto = new ImportFileDto();
        dto.setFolderPath(path);
        dto.setUpdate(false);
        dto.setAuto(true);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);

        service.importFileByFolderPath(dto);

        logger.info("数据导入任务结束, 当前时间：{}", LocalDateTime.now());
    }
}
