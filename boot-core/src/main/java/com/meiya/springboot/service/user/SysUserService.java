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
     * 只编辑用户-角色
     */
    void editUserRole(SysUserDTO userDTO) throws Exception;

    /**
     * 只编辑用户-用户组
     */
    void editUserGroup(SysUserDTO userDTO) throws Exception;

    /**
     * 只编辑用户-单位
     */
    void editUserDept(SysUserDTO userDTO) throws Exception;

    /**
     * 更新用户所有信息，包括 用户-角色，用户-用户组，用户-单位
     */
    void editUserAll(SysUserDTO userDTO) throws Exception;


}
