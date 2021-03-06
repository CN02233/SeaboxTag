package com.seabox.tagsys.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.sys.entity.UserGroupModel;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.sys.entity.User;
import com.seabox.tagsys.sys.service.GroupService;
import com.seabox.tagsys.sys.service.UserService;

@Service("groupService")
public class GroupServiceImpl extends BaseService implements GroupService {
	
	private Map<String,String> realOrderColum = new HashMap<String,String>();

	@Autowired
	private UserService userService;

	@Override
	public String findGroupListByPage(Map<String, Object> paramMap) {
		String order_id = (String) paramMap.get("order_id");
		String orderId = realOrderColum(order_id);
		paramMap.put("orderId", orderId);
		PageBean<UserGroupModel> pageBean = relationDbTemplate.selectListForPage("group.findGroupListByPage",
				"group.findTotalNum", paramMap);
		List<UserGroupModel> groupList = pageBean.getList();
		List<User> userList = userService.findAllUser(paramMap);
		for (UserGroupModel group : groupList) {
			if (group != null) {
				try {
					for (User user : userList) {
						if (user != null) {
							try {
								if(group.getGroup_id() == user.getUser_group_id()){
									group.getUserList().add(user);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		List<UserGroupModel> newGroupList = new ArrayList<UserGroupModel> ();
		for (UserGroupModel group : groupList) {
			if (group != null) {
				try {
					if(group.getUserList().size() == 1){
						group.setTwoUserNames(group.getUserList().get(0).getUser_real_nm());
						group.setAllUserNames(group.getUserList().get(0).getUser_real_nm());
					} else if(group.getUserList().size() == 2){
						group.setTwoUserNames(group.getUserList().get(0).getUser_real_nm() + "，" + group.getUserList().get(1).getUser_real_nm());
						group.setAllUserNames(group.getUserList().get(0).getUser_real_nm() + "，" + group.getUserList().get(1).getUser_real_nm());
					} else if(group.getUserList().size() > 2){
						group.setTwoUserNames(group.getUserList().get(0).getUser_real_nm() + "，" + group.getUserList().get(1).getUser_real_nm());
						StringBuffer sb = new StringBuffer();
						List<User> groupUserList = group.getUserList();
						for (User user : groupUserList) {
							if(groupUserList.indexOf(user) == (groupUserList.size() - 1)){
								sb.append(user.getUser_real_nm());
							} else {
								sb.append(user.getUser_real_nm() + "，");
							}
						}
						group.setAllUserNames(sb.toString());
					}
					newGroupList.add(group);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		pageBean.setList(newGroupList);
		return objectToJson(pageBean);
	}

	@Override
	public List<UserGroupModel> findGroupList(Map<String, Object> paramMap) {
		List<UserGroupModel> resultList = relationDbTemplate.selectList("group.findGroupList", paramMap);
		return resultList;
	}
	
	public static void main(String[] args) {
		List<UserGroupModel> groupList = new ArrayList<UserGroupModel> ();
		UserGroupModel group = new UserGroupModel();
		group.setGroup_id(1);
		System.out.println(groupList.get(0).getTwoUserNames());
	}

	@Override
	public void saveOneGroup(Map<String, Object> paramMap) {
		relationDbTemplate.insert("group.saveOneGroup", paramMap);
	}

	@Override
	public UserGroupModel findGroupById(Map<String, Object> paramMap) {
		UserGroupModel group = relationDbTemplate.selectOne("group.findGroupById", paramMap);
		return group;
	}

	@Override
	public void modifyGroupInfo(Map<String, Object> paramMap) {
		relationDbTemplate.update("group.modifyGroupInfo", paramMap);
	}

	@Override
	public void modifyGroupActiveInd(Map<String, Object> paramMap) {
		relationDbTemplate.update("group.modifyGroupActiveInd", paramMap);
	}

	@Override
	public void deleteGroupById(Map<String, Object> paramMap) {
		relationDbTemplate.update("group.deleteGroupById", paramMap);
	}

	@Override
	public void modifyGroupAdmin(Map<String, Object> paramMap) {
		relationDbTemplate.update("group.modifyGroupAdmin", paramMap);
	}

	@Override
	public void modifyGroupAdminByGroupAndAdmin(Map<String, Object> paramMap) {
		relationDbTemplate.update("group.modifyGroupAdminByGroupAndAdmin", paramMap);
	}
	
	private String realOrderColum(String orderId) {
		if (realOrderColum.size() < 1) {
			realOrderColum.put("group_id", "g.group_id");
			realOrderColum.put("group_nm", "g.group_org");
			realOrderColum.put("camp_admin_name", "camp_admin_name");
			realOrderColum.put("create_name", "create_name");
			realOrderColum.put("create_ts", "g.create_ts");
			realOrderColum.put("update_name", "update_name");
			realOrderColum.put("update_ts", "g.update_ts");
		}
		return realOrderColum.get(orderId);
	}

	@Override
	public UserGroupModel findGroupByOrgAndDep(String group_org, String group_dep) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("group_org", group_org);
		paramMap.put("group_dep", group_dep);
		UserGroupModel userGroupModel = relationDbTemplate.selectOne("group.findGroupByOrgAndDep", paramMap);
		return userGroupModel;
	}

	@Override
	public UserGroupModel findGroupByOrgAndDepAndOtherGroupId(int group_id, String group_org, String group_dep) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("group_id", group_id);
		paramMap.put("group_org", group_org);
		paramMap.put("group_dep", group_dep);
		UserGroupModel userGroupModel = relationDbTemplate.selectOne("group.findGroupByOrgAndDepAndOtherGroupId", paramMap);
		return userGroupModel;
	}

}
