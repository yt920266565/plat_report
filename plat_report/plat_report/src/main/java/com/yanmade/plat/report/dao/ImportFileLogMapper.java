package com.yanmade.plat.report.dao;

import com.yanmade.plat.report.entity.ImportFileLog;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 0103379
 */
public interface ImportFileLogMapper {

    /**
     * 插入导入日志
     * @param importFileLog
     * @return
     */
    boolean insertLog(ImportFileLog importFileLog);

    /**
     * 查询最近几条日志
     * @param count
     * @return
     */
    List<ImportFileLog> selectLog(int count);
    
    @Select("select * from import_file_log order by createtime desc limit #{page},#{limit}")
    List<ImportFileLog> getAllLog(@RequestParam Map<String, Object> map);
    
    @Select("select count(*) from import_file_log")
    int getAllLogCnt();

}
