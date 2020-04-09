package com.meiya.springboot.service.user;

import com.meiya.springboot.bean.SysUser;
import com.meiya.springboot.dto.SysUserDTO;

/**
 * @author linwb
 * @since 2019/12/12
 */
public interface SysUserService {

    /**
     * 按照ID查询用户数据
     * @param id    用户ID
     * @return      返回用户信息
     */
    SysUser selectByID(String id) ;

    /**
     * 添加用户
     */
    SysUserDTO addUser(SysUserDTO userDTO) throws Exception;

    /**
     * 删除用户
     */
    void delUser(SysUserDTO userDTO) throws Exception;

    /**
     * 只编辑用户
     */
    void editUser(SysUserDTO userDTO) throws Exception;

    /**
     * 重新关联用户-角色关系
     * 只编辑用户-角色
     */
    void reAllocateRoleToUser(SysUserDTO userDTO) throws Exception;

    /**
     * 重新关联用户-用户组的关系
     * 只编辑用户-用户组
     */
    void reAllocateGroupToUser(SysUserDTO userDTO) throws Exception;

    /**
     * 重新关联用户-单位的数据
     * 只编辑用户-单位
     */
    void reAllocateDeptToUser(SysUserDTO userDTO) throws Exception;

    /**
     * 重新关联用户相关的所有数据（角色/用户组/单位）
     * 更新用户所有信息，包括 用户-角色，用户-用户组，用户-单位
     */
    void reAllocateAll(SysUserDTO userDTO) throws Exception;


}
