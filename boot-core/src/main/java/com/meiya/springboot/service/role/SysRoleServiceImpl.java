package com.meiya.springboot.service.role;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.meiya.springboot.bean.SysRole;
import com.meiya.springboot.bean.SysRolePrivilege;
import com.meiya.springboot.common.util.StringUtil;
import com.meiya.springboot.constants.BizConstants;
import com.meiya.springboot.dto.SysPrivilegeDTO;
import com.meiya.springboot.dto.SysRoleDTO;
import com.meiya.springboot.mapper.group.SysGroupRoleMapper;
import com.meiya.springboot.mapper.role.SysRoleMapper;
import com.meiya.springboot.mapper.role.SysRolePrivilegeMapper;
import com.meiya.springboot.mapper.user.SysUserRoleMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author linwb
 * @since 2020/3/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRolePrivilegeMapper sysRolePrivilegeMapper;
    @Resource
    private SysGroupRoleMapper sysGroupRoleMapper;

    /**
     * 添加角色
     *
     * @param sysRoleDTO 添加角色信息
     * @return 返回添加的角色信息
     */
    @Override
    public SysRoleDTO addSysRole(SysRoleDTO sysRoleDTO) throws Exception {
        Validate.notNull(sysRoleDTO, "待添加的对象不能为空");
        if(StringUtils.isBlank(sysRoleDTO.getId())) {
            sysRoleDTO.setId(StringUtil.getBase64Guid());
        }

        Validate.notBlank(sysRoleDTO.getRoleName(), "角色名称不能为空");
        Validate.notBlank(sysRoleDTO.getRoleCode(), "角色编码不能为空");

        sysRoleDTO.setCreateTime(new Date());
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDTO,sysRole);
        sysRoleMapper.insert(sysRole);
        return sysRoleDTO;
    }

    /**
     * 根据角色ID删除角色
     *
     * @param roleId 角色ID
     */
    @Override
    public void delSysRoleById(String roleId) throws Exception {
        Validate.notBlank(roleId, "角色ID不能为空");
        SysRole sysRole = new SysRole();
        sysRole.setId(roleId);
        sysRole.setUpdateTime(new Date());
        sysRoleMapper.updateById(sysRole);

        // 删除角色相关的 用户组-角色关系 角色-权限关系 用户-角色关系
        Map<String,Object> delMap = Collections.singletonMap("ROLE_ID", roleId);
        sysUserRoleMapper.deleteByMap(delMap);
        sysRolePrivilegeMapper.deleteByMap(delMap);
        sysGroupRoleMapper.deleteByMap(delMap);
    }

    /**
     * 修改角色信息
     *
     * @param sysRoleDTO 需要修改的角色信息
     * @return 返回修改之后的角色
     */
    @Override
    public SysRoleDTO editSysRole(SysRoleDTO sysRoleDTO) throws Exception {
        Validate.notNull(sysRoleDTO, "待添加的对象不能为空");
        Validate.notBlank(sysRoleDTO.getId(), "角色ID不能为空");
        Validate.notBlank(sysRoleDTO.getRoleName(), "角色名称不能为空");
        Validate.notBlank(sysRoleDTO.getRoleCode(), "角色编码不能为空");

        // 根据编码查询，且ID不等于当前的角色ID 不允许存在重复的编码
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ROLE_CODE",sysRoleDTO.getRoleCode()).ne("ID", sysRoleDTO.getId()).eq("STATUS", BizConstants.STATUS_AVAIL);
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(sysRoleList)){
            throw new IllegalArgumentException("角色编码不允许重复");
        }

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDTO, sysRole);
        sysRole.setUpdateTime(new Date());
        sysRoleMapper.updateById(sysRole);
        return sysRoleDTO;
    }

    /**
     * 将权限分配给角色
     *
     * @param privilegeDTOList 权限数据
     * @param sysRoleDTO       待分配权限的角色信息
     */
    @Override
    public void addPrivilegeToRole(List<SysPrivilegeDTO> privilegeDTOList, SysRoleDTO sysRoleDTO) throws Exception {
        Validate.notEmpty(privilegeDTOList, "待分配的权限列表不允许为空");
        Validate.notNull(sysRoleDTO, "角色不能为空");
        Validate.notBlank(sysRoleDTO.getId(), "角色ID不能为空");

        List<SysRolePrivilege> rolePrivilegeList = new ArrayList<>(privilegeDTOList.size());
        String roleId = sysRoleDTO.getId();

        privilegeDTOList.forEach(privilegeDTO -> {
            Validate.notBlank(privilegeDTO.getId(), "权限ID不能为空");
            SysRolePrivilege sysRolePrivilege = new SysRolePrivilege();
            sysRolePrivilege.setRoleId(roleId);
            sysRolePrivilege.setPrivilegeId(privilegeDTO.getId());
            rolePrivilegeList.add(sysRolePrivilege);
        });
        sysRolePrivilegeMapper.batchInsert(rolePrivilegeList);
    }

    /**
     * 删除角色关联的权限数据
     *
     * @param privilegeDTOList 权限数据
     * @param sysRoleDTO       角色
     */
    @Override
    public void delPrivilegeFromRole(List<SysPrivilegeDTO> privilegeDTOList, SysRoleDTO sysRoleDTO) throws Exception {
        Validate.notEmpty(privilegeDTOList, "待分配的权限列表不允许为空");
        Validate.notNull(sysRoleDTO, "角色不能为空");
        Validate.notBlank(sysRoleDTO.getId(), "角色ID不能为空");

        Set<String> privilegeIdSet =privilegeDTOList.stream().map(SysPrivilegeDTO::getId).collect(Collectors.toSet());
        String roleId = sysRoleDTO.getId();

        UpdateWrapper<SysRolePrivilege> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("ROLE_ID", roleId).in("PRIVILEGE_ID", privilegeIdSet);
        sysRolePrivilegeMapper.delete(updateWrapper);
    }

    /**
     * 重新分配角色-权限的关系数据
     *
     * @param privilegeDTOList 需要分配的权限数据
     * @param sysRoleDTO       角色信息
     */
    @Override
    public void reAllocatePrivilegeToRole(List<SysPrivilegeDTO> privilegeDTOList, SysRoleDTO sysRoleDTO) throws Exception {
        Validate.notEmpty(privilegeDTOList, "待分配的权限列表不允许为空");
        Validate.notNull(sysRoleDTO, "角色不能为空");
        Validate.notBlank(sysRoleDTO.getId(), "角色ID不能为空");

        String roleId = sysRoleDTO.getId();
        sysRolePrivilegeMapper.deleteByMap(Collections.singletonMap("ROLE_ID", roleId));

        List<SysRolePrivilege> rolePrivilegeList = new ArrayList<>(privilegeDTOList.size());
        privilegeDTOList.forEach(privilegeDTO -> {
            SysRolePrivilege rolePrivilege = new SysRolePrivilege();
            rolePrivilege.setPrivilegeId(privilegeDTO.getId());
            rolePrivilege.setRoleId(roleId);
            rolePrivilegeList.add(rolePrivilege);
        });
        sysRolePrivilegeMapper.batchInsert(rolePrivilegeList);
    }

}
