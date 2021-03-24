package com.yanmade.plat.publish.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Service
public interface ApplyGroupMapper {
	
	@Select("select a.*,GROUP_CONCAT(c.realname) as staffName,"
			+ "GROUP_CONCAT(userName) as staffCode from ag_apply_group a "
			+ "left join ag_group_staff b on a.groupId = b.groupId "
			+ "left join sm_user c on b.staffCode = c.username group by groupId "
			+ "limit #{page},#{limit}")
	List<Map<String, Object>> get(Map<String, Object> map);
	
	@Select("select count(*) from ag_apply_group a left join ag_group_staff b on a.groupId = b.groupId")
	int getCnt();
	
	@Delete("delete a,b from ag_apply_group a left join ag_group_staff b on a.groupId = b.groupId  where  a.groupId = #{groupId}")
	boolean delete(int groupId);
	
	@Update("update ag_apply_group set name = #{name} where groupId = #{groupId}")
	boolean put(Map<String, Object> map);
	
	@Delete("delete from ag_group_staff where groupId = #{groupId}")
	boolean deleteStaff(Object groupId);
	
	@Insert("insert into ag_apply_group(groupId,name) values(#{groupId},#{name})")
	boolean insertGroup(Map<String, Object> map);
	
	@Insert("<script>"
			+ "insert into ag_group_staff values"
			+ "<foreach collection='list' item='item' separator=','>"
			+ "(#{groupId},#{item.staffCode})"
			+ "</foreach>"
			+ "</script>")
	boolean insertStaff(Map<String,Object> map);
	
	

}
