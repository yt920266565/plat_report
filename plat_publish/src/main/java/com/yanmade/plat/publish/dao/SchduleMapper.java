package com.yanmade.plat.publish.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


@Component
public interface SchduleMapper {
    
    @Select("select verType,status,currenter,applicant,applyTime,buildTime,projName"
    		+ ",(select realname  from sm_user where id = applicant) as appName"
    		+ ",(select username  from sm_user where id = applicant) as appCode"
    		+ ",(select realname  from sm_user where id = currenter) as curName"
    		+ ",(select username  from sm_user where id = currenter) as curCode"
    		+ " from ver_main")
    List<HashMap<String, Object>> getAllVerMain();
    
    @Select("select * from ym_newoa.t_hrm_configworkday where workDay = #{now} and workType = 0")
    Map<String, Object> isFreeDay(LocalDate now);
}
