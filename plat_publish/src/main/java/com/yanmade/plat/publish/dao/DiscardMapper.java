package com.yanmade.plat.publish.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import com.yanmade.plat.publish.entity.VerMain;

@Service
public interface DiscardMapper {

	@Select("<script> select vd.*,vt.* from ver_discard vd " + "left join ver_test vt on vd.flowNo = vt.mainFLowNo"
			+ "<where>" + "<if test='roleId == null or roleId == \"\"'>and deptId in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"deptList\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}" + "</foreach> " + "</if>"
			+ "<if test='flowNo != null and flowNo != \"\"'>and vd.flowNo = #{flowNo}</if> "
			+ "<if test='deptId != null and deptId != \"\" '>and deptId = #{deptId}</if> "
			+ "<if test='client != null and client != \"\"'>and client = #{client}</if> "
			+ "<if test='product != null and product != \"\"'>and product = #{product}</if> "
			+ "<if test='subPartNo != null and subPartNo != \"\"'>and subPartNo like concat('%',#{subPartNo},'%')</if> "
			+ "<if test='testType != null and testType != \"\"'>and testType = #{testType}</if> "
			+ "<if test='verType != null and verType != \"\"'>and verType = #{verType}</if> "
			+ "<if test='verId != null and verId != \"\"'>and verId like concat('%',#{verId},'%')</if> "
			+ "<if test='verNo != null and verNo != \"\"'>and verNo like concat('%',#{verNo},'%')</if> "
			+ "<if test='verlevel != null and verlevel != \"\"'>and verlevel = #{verlevel}</if> "
			+ "<if test='pkgType != null and pkgType != \"\"'>and pkgType = #{pkgType}</if> "
			+ "<if test='keyword != null and keyword != \"\"'>and concat(projName,verInfo) like concat('%',#{keyword},'%')</if> "
			+ "<if test='keywords != null and keywords != \"\"'>and concat(projName,verInfo) like concat('%',#{keywords},'%')</if> "
			+ "<if test='sTime != null and sTime != \"\"'>and publishTime between #{sTime} and #{eTime}</if> </where> "
			+ " order by publishTime DESC limit #{page},#{limit}</script>")
	List<Map<String, Object>> get(Map<String, Object> map);

	@Select("<script> select count(*) from ver_discard vd " + "left join ver_test vt on vd.flowNo = vt.mainFLowNo"
			+ "<where>" + "<if test='roleId == null or roleId == \"\"'>and deptId in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"deptList\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}" + "</foreach> " + "</if>"
			+ "<if test='flowNo != null and flowNo != \"\"'>and vd.flowNo = #{flowNo}</if> "
			+ "<if test='deptId != null and deptId != \"\" '>and deptId = #{deptId}</if> "
			+ "<if test='client != null and client != \"\"'>and client = #{client}</if> "
			+ "<if test='product != null and product != \"\"'>and product = #{product}</if> "
			+ "<if test='subPartNo != null and subPartNo != \"\"'>and subPartNo like concat('%',#{subPartNo},'%')</if> "
			+ "<if test='testType != null and testType != \"\"'>and testType = #{testType}</if> "
			+ "<if test='verType != null and verType != \"\"'>and verType = #{verType}</if> "
			+ "<if test='verId != null and verId != \"\"'>and verId like concat('%',#{verId},'%')</if> "
			+ "<if test='verNo != null and verNo != \"\"'>and verNo like concat('%',#{verNo},'%')</if> "
			+ "<if test='verlevel != null and verlevel != \"\"'>and verlevel = #{verlevel}</if> "
			+ "<if test='pkgType != null and pkgType != \"\"'>and pkgType = #{pkgType}</if> "
			+ "<if test='keyword != null and keyword != \"\"'>and concat(projName,verInfo) like concat('%',#{keyword},'%')</if> "
			+ "<if test='keywords != null and keywords != \"\"'>and concat(projName,verInfo) like concat('%',#{keywords},'%')</if> "
			+ "<if test='sTime != null and sTime != \"\"'>and publishTime between #{sTime} and #{eTime}</if> </where> </script>")
	int getCnt(Map<String, Object> map);

	@Select("select concat(b.realname,'-',b.username) as operator,comment from ver_operation a "
			+ "join sm_user b on a.operator=b.id where a.mFlowNo = #{flowNo}  and name = 'Discard' order by opTime desc limit 1 ")
	public Map<String, Object> getDiscardOpt(String flowNo);
	
	@Insert("insert into ver_discard (flowno, projname, product, subpartno, testtype,buildInfo,tempInfo, "
			+ "vertype, deptid, client, verlevel, pkgtype, verid, verno, cfgmanager, testmanager, verinfo,verPath, "
			+ "status, currenter, createtime, applicant,applytime,builder,buildtime,testtime,testresult,tester,publisher,publishTime,infoPeople)"
			+ "values (#{flowNo}, #{projName}, #{product}, #{subPartNo}, #{testType},#{buildInfo}, #{tempInfo}, "
			+ "#{verType}, #{deptId}, #{client}, #{verlevel}, #{pkgType}, "
			+ "#{verId}, #{verNo}, #{cfgManager}, #{testManager}, #{verInfo},#{verPath}, "
			+ "#{status}, #{applicant}, #{createTime}, #{applicant},#{applyTime},#{builder},#{buildTime},#{testTime},#{testResult},#{tester},#{publisher},#{publishTime},#{infoPeople})")
	public void post(VerMain ver);
	
	@Delete("delete from ver_discard where flowNo = #{flowNo}")
	public boolean delete(String flowNo);
	
	@Delete("delete from ver_publish where flowNo = #{flowNO}")
	boolean deletePublisByFlowNo(String flowNo);
	
	@Select("select * from ver_publish where flowNo = #{flowNo}")
	VerMain getPublishByFlowNo(String flowNo);
	
	@Select("select * from ver_discard where flowNo = #{flowNo}")
	VerMain getDiscardByFlowNo(String flowNo);

}
