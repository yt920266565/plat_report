package com.yanmade.plat.publish.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.yanmade.plat.publish.entity.VerMain;


@Component
public interface MydoMapper {
	@Select("<script> select status,flowNo,deptId,client,product,pkgType,subPartNo,testType,verId,verNo,verlevel,verType,projName, "
			+ "testManager,cfgManager,verInfo,buildInfo,tempInfo,verPath,testTime,testResult,tester,createTime,applicant,applyTime, "
			+ "builder,buildTime,testTime,publisher,infoPeople,currenter,mails,"
			+ "(select concat(realname,'-',username) from sm_user where id = currenter) as currName "
			+ "from ver_main where currenter = #{currenter} "
			+ "<if test='list != null and list.size() > 0'> or ( status in (3,7) and concat(verlevel,deptId) in (<foreach collection='list' item='item' separator=','> "
			+ "#{item}</foreach>) )</if>"
			+ " ORDER BY flowNo DESC LIMIT #{page},#{limit} </script>")
	public List<Map<String, Object>> getMydo(Map<String, Object> input);

	@Select("<script> select count(*) from ver_main where currenter = #{currenter} "
			+ "<if test='list != null and list.size()>0 '>"
			+ " or (status in (3,7) and  concat(verlevel,deptId) in ( <foreach collection='list' item='item' separator=','>#{item}</foreach>) )"
			+ "</if> "
			+ "</script>")
	public int getMydoCnt(Map<String, Object> input);

	@Update("update ver_main set projName=#{projName}, product=#{product}, subPartNo=#{subPartNo},"
			+ "testtype=#{testType}, vertype=#{verType}, deptid=#{deptId}, client=#{client}, verlevel=#{verlevel},"
			+ "pkgtype=#{pkgType}, verid=#{verId}, verno=#{verNo}, cfgmanager=#{cfgManager}, testmanager=#{testManager},"
			+ "verinfo=#{verInfo}, buildInfo=#{buildInfo},tempInfo=#{tempInfo},status=#{status}, currenter=#{currenter}, "
			+ "applytime=#{applyTime}, buildtime=#{buildTime}, builder=#{builder}, testtime=#{testTime}, testresult=#{testResult},"
			+ "tester=#{tester}	where flowNo = #{flowNo}")
	public boolean updateMydo(VerMain ver);

	@Select("select id,username,realname from sm_user")
	public List<HashMap<String, Object>> getUsers();

	@Select("select su.id,su.username,su.realname,sur.roleid from sm_user su inner join sm_user_role sur on su.id = sur.userid")
	public List<HashMap<String, Object>> getUsersRoles();

	@Select("select su.id,su.username,su.realname,group_concat(sur.roleid) as roleid from sm_user su inner join sm_user_role sur "
			+ "on su.id = sur.userid where sur.roleid in (2,3,4,5,9) group by su.id")
	public List<HashMap<String, Object>> getDistinctUR();
	
	@Select("select su.id,su.username,su.realname,group_concat(sur.roleid) as roleid from sm_user su inner join sm_user_role sur "
			+ "on su.id = sur.userid  group by su.id")
	public List<HashMap<String, Object>> getAllDistinctUR();
	
	@Select("<script> "
			+ "select concat(realname,'(',username,')') as userInfo from sm_user where username in "
			+ "(<foreach collection='array' item='item' separator=','>#{item}</foreach>)"
			+ "</script>")
	public List<String> getUserInfoByCode(String[] codeList);
	
	@Select("select distinct(concat(b.levelId,c.departmentId)) as level " + 
			"from sm_user_role a  " + 
			"join sm_role_level b on a.roleid = b.roleid  " + 
			"join sm_role_department c on a.roleid = c.roleid where userid = #{userid}")
	List<Integer> isTestManager(int userid);
	
	@Update("update ver_main set status = 7 where flowNo = #{flowNo}")
	void changeTestStatus(String flowNo);
	
}