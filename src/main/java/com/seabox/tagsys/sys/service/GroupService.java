package com.seabox.tagsys.sys.service;

import java.util.List;
import java.util.Map;

import com.seabox.tagsys.sys.entity.UserGroupModel;

public interface GroupService {

	/**
	 * Description 分页查询用户组
	 * @param paramMap
	 * @return String
	 * throws
	 */
	String findGroupListByPage(Map<String, Object> paramMap);
	
	/**
	 * Description 得到所有用户组
	 * @param paramMap
	 * @return List<Group>
	 * throws
	 */
	List<UserGroupModel> findGroupList(Map<String, Object> paramMap);
	
	/**
	 * Description 保存用户组信息
	 * @param paramMap void
	 * throws
	 */
	void saveOneGroup(Map<String, Object> paramMap);
	
	/**
	 * Description 根据组ID查询组
	 * @param paramMap
	 * @return Group
	 * throws
	 */
	UserGroupModel findGroupById(Map<String, Object> paramMap);
	
	/**
	 * Description 修改用户组信息
	 * @param paramMap void
	 * throws
	 */
	void modifyGroupInfo(Map<String, Object> paramMap);
	
	/**
	 * Description 修改用户组激活标志
	 * @param paramMap void
	 * throws
	 */
	void modifyGroupActiveInd(Map<String, Object> paramMap);
	
	/**
	 * Description 根据ID删除用户组
	 * @param paramMap void
	 * throws
	 */
	void deleteGroupById(Map<String, Object> paramMap);
	
	/**
	 * Description 修改活动管理员
	 * @param paramMap void
	 * throws
	 */
	void modifyGroupAdmin(Map<String, Object> paramMap);
	
	/**
	 * Description 根据组和原管理员ID修改管理员
	 * @param paramMap void
	 * throws
	 */
	void modifyGroupAdminByGroupAndAdmin(Map<String, Object> paramMap);
	
	UserGroupModel findGroupByOrgAndDep(String group_org, String group_dep);
	
	UserGroupModel findGroupByOrgAndDepAndOtherGroupId(int group_id, String group_org, String group_dep);
	
}
