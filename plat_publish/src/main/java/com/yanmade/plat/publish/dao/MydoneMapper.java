package com.yanmade.plat.publish.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.yanmade.plat.publish.entity.VerMain;

@Component
public interface MydoneMapper {
	@Select("<script> " + "SELECT p.*,t.fbuga,t.fbugb,t.fbugc,t.fbugd,t.reportPath,t.startTime,t.endTime  from ( "
			+ "SELECT vm.*, vop.mFlowNo, vop.name, vop.result, vop.comment, vop.optime, vop.operator FROM ver_operation vop "
			+ "JOIN ver_main vm on vm.flowno = vop.mFlowNo " + "UNION ALL "
			+ "SELECT vp.*, vop.mFlowNo, vop.name, vop.result, vop.comment, vop.optime, vop.operator FROM ver_operation vop "
			+ "JOIN ver_publish vp on vp.flowno = vop.mFlowNo " + "UNION ALL "
			+ "SELECT vo.*, vop.mFlowNo, vop.name, vop.result, vop.comment, vop.optime, vop.operator FROM ver_operation vop "
			+ "JOIN ver_over vo on vo.flowno = vop.mFlowNo " + "UNION ALL "
			+ "SELECT vdd.*, vop.mFlowNo, vop.name, vop.result, vop.comment, vop.optime, vop.operator FROM ver_operation vop "
			+ "JOIN ver_discard vdd on vdd.flowno = vop.mFlowNo) p left join ver_test t on p.flowNo = t.mainFlowNo " 
			+ "<where>"
			+ "<if test='list != null and list.size() != 0 ' > " 
			+ "AND p.operator in (<foreach collection='list' item = 'item' separator = ','>#{item.id}</foreach>)" + "</if> "
			+ "<if test='operator != null and operator != \"\" ' > " + "AND p.operator = #{operator} " + "</if> "
			+ "<if test='mFlowNo != null and mFlowNo != \"\" ' > " + "AND p.mFlowNo = #{mFlowNo} " + "</if> "
			+ "<if test='verId != null and verId !=\"\" '> " + "AND p.verid = #{verId} " + "</if> "
			+ "<if test='verNo != null and verNo !=\"\" '> " + "AND p.verno = #{verNo} " + "</if> "
			+ "<if test='keyWord != null and keyWord !=\"\" '> " + "AND concat(projName,verInfo) like concat('%',#{keyWord},'%') " + "</if> "
			+ "<if test='process != null and process !=\"\" '> " + "AND p.name = #{process} " + "</if> " + "</where> "
			+ "ORDER BY optime DESC " + "LIMIT #{page},#{limit} " + "</script>")
	public List<Map<String, Object>> getMydone(Map<String, Object> input);

	@Select("<script> " + "SELECT count(*) from ( "
			+ "SELECT vm.*, vop.mFlowNo, vop.name, vop.result, vop.comment, vop.optime, vop.operator FROM ver_operation vop "
			+ "JOIN ver_main vm on vm.flowno = vop.mFlowNo " + "UNION ALL "
			+ "SELECT vp.*, vop.mFlowNo, vop.name, vop.result, vop.comment, vop.optime, vop.operator FROM ver_operation vop "
			+ "JOIN ver_publish vp on vp.flowno = vop.mFlowNo " + "UNION ALL "
			+ "SELECT vo.*, vop.mFlowNo, vop.name, vop.result, vop.comment, vop.optime, vop.operator FROM ver_operation vop "
			+ "JOIN ver_over vo on vo.flowno = vop.mFlowNo ) p" + "<where>"
			+ "<if test='list != null and list.size() != 0 ' > " 
			+ "AND p.operator in (<foreach collection='list' item = 'item' separator = ','>#{item.id}</foreach>)" + "</if> "
			+ "<if test='operator != null and operator != \"\" ' > " + "AND p.operator = #{operator} " + "</if> "
			+ "<if test='mFlowNo != null and mFlowNo != \"\" ' > " + "AND p.mFlowNo = #{mFlowNo} " + "</if> "
			+ "<if test='verId != null and verId !=\"\" '> " + "AND p.verid = #{verId} " + "</if> "
			+ "<if test='verNo != null and verNo !=\"\" '> " + "AND p.verno = #{verNo} " + "</if> "
			+ "<if test='keyWord != null and keyWord !=\"\" '> " + "AND concat(projName,verInfo) like concat('%',#{keyWord},'%') " + "</if> "
			+ "<if test='process != null and process !=\"\" '> " + "AND p.name = #{process} " + "</if> " + "</where> "
			+ "</script>")
	public int getMydoneCnt(Map<String, Object> input);

	@Select("select realname from sm_user where id = #{id} ")
	public String getNameById(int id);

	@Select("select name,result,comment,opTime,operator from ver_operation where mFlowNo = #{mFlowNo} order by opTime DESC")
	public List<HashMap<String, Object>> getProcess(String mFlowNo);

	@Select("select id,name,description,pid from sm_department limit #{page},#{limit}")
	public List<HashMap<String, Object>> getDeptments(Map<String, Object> input);

	@Select("select count(*) from sm_department")
	public int getDeptCnt();

	@Select("select username,realname,email,phone,deptName from sm_user limit #{page},#{limit}")
	public List<HashMap<String, Object>> getUsers(Map<String, Object> input);

	@Select("select count(*) from sm_user")
	public int getUsersCnt();

	@Update("update ver_main set projName=#{projName}, product=#{product}, subPartNo=#{subPartNo},infoPeople=#{infoPeople},"
			+ "testtype=#{testType}, deptid=#{deptId}, client=#{client}, verlevel=#{verlevel},pkgtype=#{pkgType},"
			+ " verid=#{verId}, verno=#{verNo},verinfo=#{verInfo},buildInfo=#{buildInfo},tempInfo=#{tempInfo}"
			+ " where flowNo = #{flowNo}")
	public boolean update(VerMain ver);

	@Select("Select flowNo,count(flowNo) as count from ver_main  where verId = #{verId} and verNo = #{verNo}")
	public Map<String, Object> cheVerId(VerMain ver);
	
	@Select("select id,username,realname from sm_user where username in (select DISTINCT(staffCode) from ag_group_staff where groupId "
			+ "in (select groupId from ag_group_staff where staffCode = #{staffCode}))")
	List<HashMap<String, Object>> getApplyGrpAll(String staffCode);

}