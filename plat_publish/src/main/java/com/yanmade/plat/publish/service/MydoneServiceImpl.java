package com.yanmade.plat.publish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yanmade.plat.publish.dao.MydoneMapper;
import com.yanmade.plat.publish.dao.VerMainMapper;
import com.yanmade.plat.publish.entity.VerMain;

@Service
public class MydoneServiceImpl implements MydoneService {

	private static final String OPERATOR = "operator";
	private static final String LIMIT = "limit";
	private static final String PAGE = "page";

	@Autowired
	MydoneMapper mapper;

	@Autowired
	VerMainMapper vmapper;

	@Override
	public List<Map<String, Object>> getMydone(Map<String, Object> input) {
		if (input.get(LIMIT) != null) {
			int page = Integer.parseInt(input.get(PAGE).toString());
			int limit = Integer.parseInt(input.get(LIMIT).toString());
			if (page < 1) {
				input.put(PAGE, (page * limit));
			} else {
				input.put(PAGE, (page - 1) * limit);
			}
			input.put(LIMIT, limit);
		}
		addParam(input);
		List<Map<String, Object>> list = mapper.getMydone(input);
		listAddInitPeople(list);
		return list;
	}

	@Override
	public int getMydoneCnt(Map<String, Object> input) {
		return mapper.getMydoneCnt(input);
	}
	
	/**添加initPeople，用作已通知人员的初始化
	 * @param list
	 */
	private void listAddInitPeople(List<Map<String, Object>> list) {
		String infoKey = "infoPeople";
		for (Map<String, Object> entries:list) {
			
			if(Objects.isNull(entries.get(infoKey))) {
				continue;
			}
			String initPeople = entries.get(infoKey).toString();
			initPeople = initPeople.replaceAll("[\u4e00-\u9fa5]+\\(", "");
			initPeople = initPeople.replaceAll("\\)", "");
			entries.put("initPeople", initPeople);
		}
	}
	
	//根据参数判断是查询本人我处理的还是，我所在版本申请分组里面所有的我处理的
	private Map<String,Object> addParam(Map<String, Object> map){
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get(OPERATOR) == null) {
			// 根据工号查出所在申请组所有成员的id
			List<HashMap<String, Object>> list = mapper.getApplyGrpAll(username);
			if(list.isEmpty()) {
				map.put(OPERATOR, vmapper.getIdByName(username));
			}else {
				map.put("list", list);
			}
		} else {
			int id = vmapper.getIdByName(map.get(OPERATOR).toString());
			map.put(OPERATOR, id);
		}
		return map;
	}

	@Override
	public Map<String, Object> getNameById(Map<String, Object> input) {
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, Object> m : input.entrySet()) {
			int id = Integer.parseInt(m.getValue().toString());
			String key = m.getKey();
			if (key.equals("cfgManager")) {
				String cfgManager = mapper.getNameById(id);
				map.put("cfgManager", cfgManager);
			} else if (key.equals("testManager")) {
				String testManager = mapper.getNameById(id);
				map.put("testManager", testManager);
			} else if (key.equals("tester")) {
				String tester = mapper.getNameById(id);
				map.put("tester", tester);
			} else if (key.equals("publisher")) {
				String publisher = mapper.getNameById(id);
				map.put("publisher", publisher);
			} else if (key.equals(OPERATOR)) {
				String operator = mapper.getNameById(id);
				map.put(OPERATOR, operator);
			}
		}
		return map;
	}

	@Override
	public List<HashMap<String, Object>> getProcess(String mFlowNo) {
		return mapper.getProcess(mFlowNo);
	}

	@Override
	public List<HashMap<String, Object>> getDeptments(Map<String, Object> input) {
		if (input.get(LIMIT) != null) {
			int page = Integer.parseInt(input.get(PAGE).toString());
			int limit = Integer.parseInt(input.get(LIMIT).toString());
			if (page < 1) {
				input.put(PAGE, (page * limit));
			} else {
				input.put(PAGE, (page - 1) * limit);
			}
			input.put(LIMIT, limit);
		}
		return mapper.getDeptments(input);
	}

	@Override
	public int getDeptCnt() {
		return mapper.getDeptCnt();
	}

	@Override
	public List<HashMap<String, Object>> getUsers(Map<String, Object> input) {
		if (input.get(LIMIT) != null) {
			int page = Integer.parseInt(input.get(PAGE).toString());
			int limit = Integer.parseInt(input.get(LIMIT).toString());
			if (page < 1) {
				input.put(PAGE, (page * limit));
			} else {
				input.put(PAGE, (page - 1) * limit);
			}
			input.put(LIMIT, limit);
		}
		return mapper.getUsers(input);
	}

	@Override
	public int getUsersCnt() {
		return mapper.getUsersCnt();
	}

	@Override
	public boolean update(VerMain ver) {
		return mapper.update(ver);
	}

	@Override
	public Map<String, Object> cheVerId(VerMain ver) {
		return mapper.cheVerId(ver);
	}

	@Override
	public List<HashMap<String, Object>> getApplyGrpAll(String staffCode) {
		return mapper.getApplyGrpAll(staffCode);
	}

}