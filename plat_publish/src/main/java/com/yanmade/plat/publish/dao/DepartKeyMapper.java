package com.yanmade.plat.publish.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface DepartKeyMapper {

	@Insert("<script> insert into depart_keywords(departId,keyword) values"
			+ " <foreach collection='list' item='item' separator=','>" + " (#{departId},#{item})"
			+ " </foreach> </script>")
	void insert(Map<String, Object> map);

	@Delete("delete from depart_keywords where departId = #{departId}")
	void delete(int departId);

	@Select("select * from sm_user where username = #{username}")
	Map<String, Object> getUserInfoByName(String username);

	@Select("select pid from sm_department where id = #{departId}")
	int getPidById(int departId);

	@Select("select departId,GROUP_CONCAT(DISTINCT(keyword)) as keywords,name from depart_keywords a"
			+ " join sm_department b on a.departId = b.id group by departId ")
	List<Map<String, Object>> getDeptKeywords();
	
	@Select("select count(DISTINCT(departId))  from depart_keywords ")
	int getDeptKeywordsCnt();
	
	@Select("<script> select DISTINCT(keyword) from depart_keywords"
			+" <where>"
			+" <if test = ' departId != \"\" and departId != null'>departId = #{departId} </if>"
			+" </where>"
			+ "</script>")
	List<String> getKeyWordsByDeptId(@Param("departId")String departId);
}
