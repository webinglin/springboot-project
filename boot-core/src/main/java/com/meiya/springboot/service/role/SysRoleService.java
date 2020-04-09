package com.meiya.springboot.service.role;

import com.meiya.springboot.dto.SysPrivilegeDTO;
import com.meiya.springboot.dto.SysRoleDTO;

import java.util.List;

/**
 * 角色操作业务方法定义
 * @author linwb
 * @since 2020/3/18
 */
public interface SysRoleService {
    /**
     * 添加角色
     * @param sysRoleDTO    添加角色信息
     * @return              返回添加的角色信息
     */
    SysRoleDTO addSysRole(SysRoleDTO sysRoleDTO) throws Exception;

    /**
     * 根据角色ID删除角色
     * @param roleId    角色ID
     */
    void delSysRoleById(String roleId) throws Exception;

    /**
     * 修改角色信息
     * @param sysRoleDTO    需要修改的角色信息
     * @return      返回修改之后的角色
     */
    SysRoleDTO editSysRole(SysRoleDTO sysRoleDTO) throws Exception;

    /**
     * 将权限分配给角色
     * @param privilegeDTOList  权限数据
     * @param sysRoleDTO    待分配权限的角色信息
     */
    void addPrivilegeToRole(List<SysPrivilegeDTO> privilegeDTOList, SysRoleDTO sysRoleDTO) throws Exception;

    /**
     * 删除角色关联的权限数据
     * @param privilegeDTOList  权限数据
     * @param sysRoleDTO        角色
     */
    void delPrivilegeFromRole(List<SysPrivilegeDTO> privilegeDTOList, SysRoleDTO sysRoleDTO) throws Exception;

    /**
     * 重新分配角色-权限的关系数据
     * @param privilegeDTOList  需要分配的权限数据
     * @param sysRoleDTO        角色信息
     */
    void reAllocatePrivilegeToRole(List<SysPrivilegeDTO> privilegeDTOList, SysRoleDTO sysRoleDTO) throws Exception;


}
