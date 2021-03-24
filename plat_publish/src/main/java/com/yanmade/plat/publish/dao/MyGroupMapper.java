package com.yanmade.plat.publish.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Service
public interface MyGroupMapper {
	
	@Insert("<script>"
			+ "insert into ug_group_staff values"
			+ "<foreach collection='list' item='item' separator=','>"
			+ "(#{groupId},#{item.staffCode})"
			+ "</foreach>"
			+ "</script>")
	boolean insert(Map<String,Object> map);
	
	@Insert("insert into ug_user_group(userId,groupId,groupName) values(#{userId},#{groupId},#{groupName})")
	boolean insertUser(Map<String,Object> map);
	
	@Select("<script> "
			+ "select count(*) from ug_user_group <where> "
			+ "and userId=#{userId} and groupName =#{groupName}"
			+ "<if test='groupId != null and groupId != \"\"'> and groupId != #{groupId}</if>"
			+ "</where> </script>")
	int checkGrpName(Map<String, Object> map);
	
	@Select("select a.*,GROUP_CONCAT(c.realname) as staffName,GROUP_CONCAT(staffCode) as staffCode from ug_user_group a "
			+ "left join ug_group_staff b on a.groupId = b.groupId left join sm_user c on b.staffCode = c.username " 
			+ "where userId = #{userId} group by userId,groupName limit #{page},#{limit}")
	List<HashMap<String, Object>> getUserGroup(Map<String, Object> map);
	
	@Select("select count(*) from ug_user_group where userId = #{userId}")
	int getUserGrpCnt(Map<String, Object> map);
	
	@Update("update ug_user_group set groupName = #{groupName} where groupId = #{groupId} and userId = #{userId}")
	boolean update(Map<String, Object> map);
	
	@Delete("delete from ug_group_staff where  groupId = #{groupId}")
	boolean deleteUser(Map<String, Object> map);
	
	@Delete("delete from ug_user_group where groupId = #{groupId}")
	boolean delete(Map<String, Object> map);
	
	@Select("select count(*) from ug_group_staff where groupId = #{groupId}")
	int hasGroupUser(Map<String, Object> map);
	
	@Select("select * from ug_user_group where userId = #{userId}")
	List<HashMap<String, Object>> getMyGroup(String userId);
	
	@Select("select * from ug_group_staff where groupId = #{groupId}")
	List<HashMap<String, Object>> getStaff(String groupId);

}
