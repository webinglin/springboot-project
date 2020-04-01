package com.meiya.springboot.service.group;

import com.meiya.springboot.dto.SysGroupDTO;
import com.meiya.springboot.dto.SysRoleDTO;
import com.meiya.springboot.dto.SysUserDTO;

import java.util.List;

/**
 * @author linwb
 * @since 2020/3/18
 */
public interface SysGroupService {

    /**
     * 添加用户组基本信息
     * @param groupDTO  用户组基本信息
     */
    SysGroupDTO addGroup(SysGroupDTO groupDTO) throws Exception;

    /**
     * 删除用户组基本信息（同时会删除该用户组关联的所有角色/用户 关系)
     * @param groupDTO  用户组
     */
    void delGroup(SysGroupDTO groupDTO) throws Exception;

    /**
     * 修改用户组基本信息
     * @param groupDTO  用户组
     */
    SysGroupDTO editGroup(SysGroupDTO groupDTO) throws Exception;

    /**
     * 将用户列表添加到指定的用户组
     * @param userDTOList   用户列表
     * @param groupDTO      用户组
     */
    void addUsersToGroup(List<SysUserDTO> userDTOList, SysGroupDTO groupDTO) throws Exception;

    /**
     * 从用户组中删除用户列表
     * @param userDTOList   需要从用户组中删除的用户列表
     * @param groupDTO      用户组
     */
    void delUsersFromGroup(List<SysUserDTO> userDTOList, SysGroupDTO groupDTO) throws Exception;

    /**
     * 重新分配用户到用户组，原先的该用户组下面的所有用户关联关系清空。
     * @param userDTOList   用户列表
     * @param groupDTO      用户组
     */
    void reAllocateUsersToGroup(List<SysUserDTO> userDTOList, SysGroupDTO groupDTO) throws Exception;

    /**
     * 将角色列表添加到指定的用户组
     * @param sysRoleDTOList    需要分配到用户组的角色列表
     * @param groupDTO          用户组
     */
    void addRolesToGroup(List<SysRoleDTO> sysRoleDTOList, SysGroupDTO groupDTO) throws Exception;

    /**
     * 将角色列表从指定的用户组中删除
     * @param sysRoleDTOList    需要删除的角色列表
     * @param groupDTO          用户组
     */
    void delRolesFromGroup(List<SysRoleDTO> sysRoleDTOList, SysGroupDTO groupDTO) throws Exception;

    /**
     * 重新分配角色到用户组，删除原来该用户组关联的角色关系
     * @param sysRoleDTOList    需要分配到用户组的角色列表
     * @param groupDTO          用户组
     */
    void reAllocateRolesToGroup(List<SysRoleDTO> sysRoleDTOList, SysGroupDTO groupDTO) throws Exception;


}
