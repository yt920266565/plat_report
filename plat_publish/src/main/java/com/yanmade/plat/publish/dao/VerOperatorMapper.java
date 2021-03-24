package com.yanmade.plat.publish.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.yanmade.plat.publish.entity.VerOperation;
import com.yanmade.plat.publish.entity.VerTest;

@Component
public interface VerOperatorMapper {

	@Insert("insert into ver_operation (flowno, mflowno, name, result, comment, comment1, comment2, comment3, optime, operator)"
			+ "values (#{flowNo}, #{mFlowNo}, #{name}, #{result}, #{comment}, #{comment1}, #{comment2}, #{comment3}, #{optime}, #{operator})")
	public boolean insert(VerOperation ver);

	@Insert("insert into ver_test (flowno, mainflowno, tester, result, comment, startTime, endTime, fbuga,fbugb,fbugc,fbugd,reportPath, optime, operator)"
			+ "values (#{flowNo}, #{mainFlowNo}, #{tester}, #{result}, #{comment}, #{startTime}, #{endTime}, #{fbuga},#{fbugb},#{fbugc},#{fbugd},"
			+ "#{reportPath},#{optime}, #{operator})")
	public boolean insertTest(VerTest vt);

	@Select("select type,code,name from erp_dictionary order by type,sort")
	public List<HashMap<String, Object>> getErpDic();

}
