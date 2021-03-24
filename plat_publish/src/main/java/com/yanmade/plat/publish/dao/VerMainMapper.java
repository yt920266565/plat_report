package com.yanmade.plat.publish.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.yanmade.plat.publish.entity.VerMain;

@Component
public interface VerMainMapper {
	
	@Select("select id from sm_user where username=#{username}")
	public int getIdByName(String username);
	
	@Select("select username,realname from sm_user where id = #{id} ")
	public Map<String, Object> getUserById(int id);
	
	@Insert("insert into ver_main (flowno, projname, product, subpartno, testtype,buildInfo,tempInfo, "
			+ "vertype, deptid, client, verlevel, pkgtype, verid, verno, cfgmanager, testmanager, verinfo, "
			+ "status, currenter, createtime,applyTime,applicant,builder,publisher,infoPeople,mails) "
			+ "values (#{flowNo}, #{projName}, #{product}, #{subPartNo}, #{testType},#{buildInfo}, #{tempInfo}, "
			+ "#{verType}, #{deptId}, #{client}, #{verlevel}, #{pkgType}, "
			+ "#{verId}, #{verNo}, #{cfgManager}, #{testManager}, #{verInfo}, "
			+ "#{status}, #{currenter}, #{createTime},#{applyTime},#{applicant},#{builder},#{publisher},#{infoPeople},#{mails})")
	public boolean insert(VerMain ver);

	@Update("update ver_main set status=#{status}, currenter=#{currenter},verPath=#{verPath},buildtime=#{buildTime},"
			+ " testtime=#{testTime},infoPeople=#{infoPeople},mails=#{mails} where flowNo = #{flowNo}")
	public boolean update(VerMain ver);

	@Delete("delete from ver_main where flowNo = #{flowNo}")
	public boolean delete(String flowNo);

	@Select("select status,flowNo,deptId,client,product,pkgType,subPartNo,testType,verId,verNo,verlevel,verType,projName,"
			+ "testManager,cfgManager,verInfo,buildInfo,tempInfo,verPath,testTime,testResult,tester,applicant,applyTime, "
			+ "builder,buildtime,publishtime, publisher from ver_main where flowNo = #{flowNo}")
	public VerMain getVerMain(String flowNo);

	@Select("select count(flowNo) from ver_main where verId = #{verId} and verNo = #{verNo}")
	public int verIsRept(VerMain ver);

	@Insert("insert into ver_over (infoPeople,flowno, projname, product, subpartno, testtype,buildInfo,tempInfo, "
			+ "vertype, deptid, client, verlevel, pkgtype, verid, verno, cfgmanager, testmanager, verinfo,verPath, "
			+ "status, currenter, createtime, applicant,applytime,builder,buildtime,testtime,testresult,tester,publisher,publishTime)"
			+ "values (#{infoPeople},#{flowNo}, #{projName}, #{product}, #{subPartNo}, #{testType},#{buildInfo}, #{tempInfo}, "
			+ "#{verType}, #{deptId}, #{client}, #{verlevel}, #{pkgType}, "
			+ "#{verId}, #{verNo}, #{cfgManager}, #{testManager}, #{verInfo},#{verPath}, "
			+ "#{status}, #{currenter}, #{createTime}, #{applicant},#{applyTime},#{builder},#{buildTime},#{testTime},#{testResult},#{tester},#{publisher},#{publishTime})")
	public boolean insertOver(VerMain ver);

	@Insert("insert into ver_publish (flowno, projname, product, subpartno, testtype,buildInfo,tempInfo,vertype, deptid, client, "
			+ " verlevel, pkgtype, verid, verno, cfgmanager, testmanager, verinfo,verPath,status, currenter, createtime, "
			+ " applicant,applytime,builder,buildtime,testtime,testresult,tester,publisher,publishTime,infoPeople,mails)"
			+ "values (#{flowNo}, #{projName}, #{product}, #{subPartNo}, #{testType},#{buildInfo}, #{tempInfo}, "
			+ "#{verType}, #{deptId}, #{client}, #{verlevel}, #{pkgType},#{verId}, #{verNo}, #{cfgManager},"
			+ "#{testManager}, #{verInfo},#{verPath},#{status}, #{applicant}, #{createTime},#{applicant},#{applyTime}, "
			+ "#{builder},#{buildTime},#{testTime},#{testResult},#{tester},#{publisher},#{publishTime},#{infoPeople},#{mails})")
	public boolean insertPub(VerMain ver);

	@Select("<script>"
			+ "select vp.*,vt.fbuga,vt.fbugb,vt.fbugc,vt.fbugd,vt.reportPath,vt.startTime,vt.endTime from ver_publish vp left join ver_test vt on vp.flowNo = vt.mainFlowNo"
			+ "<where>" + "<if test='roleId == null or roleId == \"\"'>and deptId in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"deplist\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}" + "</foreach> " + "</if>"
			+ "<if test='flowNo != null and flowNo != \"\"'>and vp.flowNo = #{flowNo}</if> "
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
			+ "<if test='sTime != null and sTime != \"\"'>and publishTime between #{sTime} and #{eTime}</if> "
			+ "</where>" + "ORDER BY publishTime DESC LIMIT #{page},#{limit}" + "</script>")
	public List<HashMap<String, Object>> getPublished(Map<String, Object> input);

	@Select("<script>"
			+ "select status,flowNo,deptId,client,product,pkgType,subPartNo,testType,verId,verNo,verlevel,verType,projName, "
			+ "testManager,cfgManager,verInfo,buildInfo,tempInfo,verPath,testTime,testResult,tester,createTime,applicant,applyTime, "
			+ "builder,buildTime,testTime,publisher, datediff(now(), createtime) as diff from ver_main "
			+ "where datediff(now(), createtime) >= 7 "
			+ "<if test='deptList != null and deptList.size()> 0'> and deptId in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"deptList\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}" + "</foreach> " + "</if>" + "ORDER BY diff DESC </script>")
	public List<Map<String, Object>> getUnpublished(@Param("deptList") List<String> deptList);

	@Select("<script>" + "select count(*) from ver_publish" + "<where>"
			+ "<if test='roleId == null or roleId == \"\"'>and deptId in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"deplist\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}" + "</foreach> " + "</if>"
			+ "<if test='deptId != null and deptId != \"\"'>and deptId = #{deptId}</if>"
			+ "<if test='client != null and client != \"\"'>and client = #{client}</if>"
			+ "<if test='product != null and product != \"\"'>and product = #{product}</if>"
			+ "<if test='subPartNo != null and subPartNo != \"\"'>and subPartNo = #{subPartNo}</if>"
			+ "<if test='testType != null and testType != \"\"'>and testType = #{testType}</if>"
			+ "<if test='verType != null and verType != \"\"'>and verType = #{verType}</if>"
			+ "<if test='verId != null and verId != \"\"'>and verId = #{verId}</if>"
			+ "<if test='verNo != null and verNo != \"\"'>and verNo = #{verNo}</if>"
			+ "<if test='verlevel != null and verlevel != \"\"'>and verlevel = #{verlevel}</if>"
			+ "<if test='pkgType != null and pkgType != \"\"'>and pkgType = #{pkgType}</if>"
			+ "<if test='keyword != null and keyword != \"\"'>and concat(projName,verInfo) like concat('%',#{keyword},'%')</if> "
			+ "<if test='keywords != null and keywords != \"\"'>and concat(projName,verInfo) like concat('%',#{keywords},'%')</if> "
			+ "<if test='sTime != null and sTime != \"\"'>and publishTime between #{sTime} and #{eTime}</if>"
			+ "</where>" + "</script>")
	public int getPubCnt(Map<String, Object> input);

	@Select("<script>"
			+ "select distinct(vp.flowNo),vp.status,vp.deptId,vp.client,vp.product,vp.pkgType,vp.subPartNo,vp.testType,vp.verId,vp.verNo,"
			+ "vp.tester,vp.testResult,vp.createTime,vp.applicant,vp.applyTime,vp.builder,vp.buildTime,vp.testTime,vp.publisher,vp.mails, "
			+ "(select concat(realname,'-',username) from sm_user smu where  smu.id = vp.cfgManager) as cfgName,"
			+ "(select concat(realname,'-',username)  from sm_user smu where  smu.id = vp.testManager) as testName,"
			+ "vp.verlevel,vp.verType,vp.projName,vp.testManager,vp.cfgManager,vp.verInfo,vp.buildInfo,vp.tempInfo,vp.verPath,vo.result,vp.infoPeople "
			+ "from ver_main vp left join ver_operation vo on vp.flowNo = vo.mFlowNo " + "<where> "
			+ "<if test='roleId == null or roleId == \"\"'>and deptId in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"deplist\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}" + "</foreach> " + "</if>" + "<if test='1==1'>and vp.status != 1</if> "
			+ "<if test='flowNo != null and flowNo != \"\"'>and vp.flowNo = #{flowNo}</if> "
			+ "<if test='verId != null and verId != \"\"'>and vp.verId = #{verId}</if> "
			+ "<if test='verNo != null and verNo != \"\"'>and vp.verNo = #{verNo}</if> "
			+ "<if test='sTime != null and sTime != \"\"'>and vo.opTime between #{sTime} and #{eTime}</if> "
			+ "</where> " + "ORDER BY vp.flowNo DESC,vo.result DESC " + "LIMIT #{page},#{limit} " + "</script>")
	public List<HashMap<String, Object>> getMaintain(Map<String, Object> input);

	@Select("<script>" + "select count(distinct(vp.flowNo)) "
			+ "from ver_main vp left join ver_operation vo on vp.flowNo = vo.mFlowNo " + "<where> "
			+ "<if test='roleId == null or roleId == \"\"'>and deptId in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"deplist\" open=\"(\" separator=\",\" close=\")\">"
			+ "#{item}" + "</foreach> " + "</if>" + "<if test='1==1'>and vp.status != 1</if> "
			+ "<if test='flowNo != null and flowNo != \"\"'>and vp.flowNo = #{flowNo}</if> "
			+ "<if test='verId != null and verId != \"\"'>and vp.verId = #{verId}</if> "
			+ "<if test='verNo != null and verNo != \"\"'>and vp.verNo = #{verNo}</if> "
			+ "<if test='sTime != null and sTime != \"\"'>and vo.opTime between #{sTime} and #{eTime}</if> "
			+ "</where> " + "</script>")
	public int getMaintainCnt(Map<String, Object> input);

	@Update("update ver_main set projName=#{projName}, product=#{product}, subPartNo=#{subPartNo},"
			+ "testtype=#{testType}, deptid=#{deptId}, client=#{client}, verlevel=#{verlevel},"
			+ "pkgtype=#{pkgType}, verid=#{verId}, verno=#{verNo}, cfgmanager=#{cfgManager}, testmanager=#{testManager},"
			+ "verinfo=#{verInfo},buildInfo=#{buildInfo},tempInfo=#{tempInfo},verPath=#{verPath},publisher=#{publisher},"
			+ "testresult=#{testResult},tester=#{tester},currenter=#{currenter},infoPeople=#{infoPeople},mails=#{mails} where flowNo = #{flowNo}")
	public Boolean verMaintain(VerMain ver);

	@Update("update ver_main set status = #{status},currenter = #{currenter} where flowNo = #{flowNo}")
	public Boolean rollBack(VerMain ver);

	@Select("select verPath from ver_main  where flowNo = #{flowNo} "
			+ "UNION ALL select verPath from ver_publish  where flowNo = #{flowNo}")
	public Map<String, Object> getVerPath(@PathVariable(value = "flowNo") String flowNo);

	@Select("<script>" + "select flowNo from ver_publish " + "<where> "
			+ "<if test='deptId != null and deptId != \"\" '>and deptId = #{deptId}</if> "
			+ "<if test='client != null and client != \"\"'>and client = #{client}</if> "
			+ "<if test='product != null and product != \"\"'>and product = #{product}</if> "
			+ "<if test='subPartNo != null and subPartNo != \"\"'>and subPartNo = #{subPartNo}</if> "
			+ "<if test='testType != null and testType != \"\"'>and testType = #{testType}</if> "
			+ "<if test='verType != null and verType != \"\"'>and verType = #{verType}</if> "
			+ "<if test='verId != null and verId != \"\"'>and verId = #{verId}</if> "
			+ "<if test='verNo != null and verNo != \"\"'>and verNo = #{verNo}</if> "
			+ "<if test='verlevel != null and verlevel != \"\"'>and verlevel = #{verlevel}</if> "
			+ "<if test='pkgType != null and pkgType != \"\"'>and pkgType = #{pkgType}</if> "
			+ "<if test='sTime != null and sTime != \"\"'>and publishTime between #{sTime} and #{eTime}</if> "
			+ "</where>" + "</script>")
	public List<String> getFlowNo(Map<String, Object> map);

	@Select("<script> " + "select * from ver_publish where flowNo in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"flist\"  open=\"(\" separator=\",\" close=\")\"> "
			+ "#{item} " + "</foreach> " + "</script> ")
	public List<HashMap<String, Object>> getPubExcel(Map<String, Object> map);

	@Select("select count(*) from ver_main where flowNo = #{flowNo}")
	public int checkPub(String flowNo);

	@Select("select currenter from ver_main where flowNo = #{flowNo}")
	public int getCurrenter(String flowNo);
	
}
