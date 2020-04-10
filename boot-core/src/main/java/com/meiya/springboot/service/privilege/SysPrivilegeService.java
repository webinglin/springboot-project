package com.meiya.springboot.service.privilege;

import com.meiya.springboot.bean.SysPrivilege;

import java.util.List;
import java.util.Set;

/**
 * 权限逻辑定义
 * @author linwb
 * @since 2020/3/18
 */
public interface SysPrivilegeService {

    /**
     * 根据用户ID查询用户的所有权限（用户组关联的权限+用户角色关联的权限）
     * @param userId    用户ID
     */
    List<SysPrivilege> queryPrivilegesByUserId(String userId) throws Exception;

    /**
     * 查询用户所有用户组对应的角色ID列表
     * @param userId    用户ID
     */
    Set<String> queryUserGroupRoleIdSet(String userId) throws Exception;



    // TODO CRUD

}
