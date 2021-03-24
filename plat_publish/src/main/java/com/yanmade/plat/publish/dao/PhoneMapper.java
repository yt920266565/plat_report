package com.yanmade.plat.publish.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.yanmade.plat.publish.entity.VerMain;

@Component
public interface PhoneMapper {

	@Select("select * from ver_publish")
	public List<HashMap<String, Object>> getPublished();
	
	@Update("update ver_publish set infoPeople = #{userInfo},mails = #{mails} where flowNo = #{flowNo}")
	public boolean updateSale(Map<String,Object> map);
	
	@Select("select * from ver_publish where flowNo = #{flowNo}")
	public VerMain getVerPublishByFlowNo(String flowNo);

}
