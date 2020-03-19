package com.meiya.springboot.service.privilege;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meiya.springboot.bean.*;
import com.meiya.springboot.common.util.StringUtil;
import com.meiya.springboot.mapper.group.SysGroupRoleMapper;
import com.meiya.springboot.mapper.privilege.SysPrivilegeMapper;
import com.meiya.springboot.mapper.role.SysRolePrivilegeMapper;
import com.meiya.springboot.mapper.user.SysUserGroupMapper;
import com.meiya.springboot.mapper.user.SysUserRoleMapper;
import org.apache.commons.collections.CollectionUtils;
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
public class SysPrivilegeServiceImpl implements SysPrivilegeService {

    @Resource
    private SysPrivilegeMapper sysPrivilegeMapper;
    @Resource
    private SysUserGroupMapper sysUserGroupMapper;
    @Resource
    private SysGroupRoleMapper sysGroupRoleMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRolePrivilegeMapper sysRolePrivilegeMapper;

    @Override
    public List<SysPrivilege> queryPrivilegesByUserId(String userId) throws Exception {
        StringUtil.isBlank(userId,"用户ID为空");

        // 查询用户所有的用户组 拥有的所有权限
        Set<String> roleIdSet = queryUserGroupRoleIdSet(userId);
        if(CollectionUtils.isEmpty(roleIdSet)){
            roleIdSet = new HashSet<>();
        }
        // 查询用户所有的角色
        Map<String,Object> selectMap = Collections.singletonMap("USER_ID", userId);
        // 合并用户组关联的角色 和 用户直接关联的角色
        roleIdSet.addAll(sysUserRoleMapper.selectByMap(selectMap).stream().map(SysUserRole::getRoleId).collect(Collectors.toSet()));

        // 根据角色ID查询所有的权限
        QueryWrapper<SysRolePrivilege> rolePrivilegeQueryWrapper = new QueryWrapper<>();
        rolePrivilegeQueryWrapper.in("ROLE_ID", roleIdSet);
        Set<String> privilegeIdSet = sysRolePrivilegeMapper.selectList(rolePrivilegeQueryWrapper).stream().map(SysRolePrivilege::getPrivilegeId).collect(Collectors.toSet());
        if(CollectionUtils.isEmpty(privilegeIdSet)){
            return new ArrayList<>();
        }
        // 查询所有的权限
        return sysPrivilegeMapper.selectBatchIds(privilegeIdSet);
    }

    @Override
    public Set<String> queryUserGroupRoleIdSet(String userId) throws Exception {
        // 查询用户具备的所有用户组
        Map<String,Object> selectMap = Collections.singletonMap("USER_ID", userId);
        List<SysUserGroup> userGroupList = sysUserGroupMapper.selectByMap(selectMap);
        Set<String> groupIdSet = userGroupList.stream().map(SysUserGroup::getGroupId).collect(Collectors.toSet());
        if(CollectionUtils.isEmpty(groupIdSet)){
            return null;
        }
        // 查询用户组具备的角色
        QueryWrapper<SysGroupRole> wrapper = new QueryWrapper<>();
        wrapper.in("GROUP_ID", groupIdSet);
        List<SysGroupRole> groupRoleList = sysGroupRoleMapper.selectList(wrapper);
        return groupRoleList.stream().map(SysGroupRole::getRoleId).collect(Collectors.toSet());
    }
}
