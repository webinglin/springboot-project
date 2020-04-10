package com.meiya.springboot.service.group;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.meiya.springboot.bean.SysGroup;
import com.meiya.springboot.bean.SysGroupRole;
import com.meiya.springboot.bean.SysUserGroup;
import com.meiya.springboot.common.util.StringUtil;
import com.meiya.springboot.constants.BizConstants;
import com.meiya.springboot.constants.FieldConstants;
import com.meiya.springboot.dto.SysGroupDTO;
import com.meiya.springboot.dto.SysRoleDTO;
import com.meiya.springboot.dto.SysUserDTO;
import com.meiya.springboot.mapper.group.SysGroupMapper;
import com.meiya.springboot.mapper.group.SysGroupRoleMapper;
import com.meiya.springboot.mapper.user.SysUserGroupMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户组业务实现类
 * @author linwb
 * @since 2020/3/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysGroupServiceImpl implements SysGroupService {

    @Resource
    private SysGroupMapper sysGroupMapper;
    @Resource
    private SysUserGroupMapper sysUserGroupMapper;
    @Resource
    private SysGroupRoleMapper sysGroupRoleMapper;

    private void checkProperties(SysGroupDTO groupDTO) throws Exception {
        Validate.notNull(groupDTO, "操作的对象为空");
        Validate.notBlank(groupDTO.getUpdatorId(), "编辑者用户ID不允许为空");
    }

    /**
     * 添加用户组基本信息
     *
     * @param groupDTO 用户组基本信息
     */
    @Override
    public SysGroupDTO addGroup(SysGroupDTO groupDTO) throws Exception {
        checkProperties(groupDTO);
        if(StringUtils.isBlank(groupDTO.getId())) {
            groupDTO.setId(StringUtil.getBase64Guid());
        }
        Validate.notBlank(groupDTO.getCreatorId(), "创建用户ID不允许为空");
        Validate.notBlank(groupDTO.getName(), "组名不能为空");
        Date now = new Date();
        groupDTO.setUpdateTime(now);
        groupDTO.setCreateTime(now);
        groupDTO.setStatus(BizConstants.STATUS_AVAIL);

        SysGroup sysGroup = new SysGroup();
        BeanUtils.copyProperties(groupDTO, sysGroup);
        sysGroupMapper.insert(sysGroup);

        return groupDTO;
    }

    /**
     * 删除用户组基本信息（同时会删除该用户组关联的所有角色/用户 关系)
     *
     * @param groupDTO 用户组
     */
    @Override
    public void delGroup(SysGroupDTO groupDTO) throws Exception {
        checkProperties(groupDTO);
        String groupId = groupDTO.getId();
        Validate.notBlank(groupId, "用户组ID不能为空");

        groupDTO.setStatus(BizConstants.STATUS_DEL);
        groupDTO.setUpdateTime(new Date());
        SysGroup sysGroup = new SysGroup();
        BeanUtils.copyProperties(groupDTO, sysGroup);
        sysGroupMapper.updateById(sysGroup);

        // 删除用户组相关的用户关系数据和 角色关系数据
        Map<String,Object> delMap = Collections.singletonMap(FieldConstants.GROUP_ID, groupId);
        sysGroupRoleMapper.deleteByMap(delMap);
        sysUserGroupMapper.deleteByMap(delMap);
    }

    /**
     * 修改用户组基本信息
     *
     * @param groupDTO 用户组
     */
    @Override
    public SysGroupDTO editGroup(SysGroupDTO groupDTO) throws Exception {
        checkProperties(groupDTO);
        Validate.notBlank(groupDTO.getId(), "用户组ID不能为空");
        SysGroup sysGroup = sysGroupMapper.selectById(groupDTO.getId());
        groupDTO.setUpdateTime(new Date());
        BeanUtils.copyProperties(groupDTO, sysGroup);
        sysGroupMapper.updateById(sysGroup);
        return groupDTO;
    }

    /**
     * 将用户列表添加到指定的用户组
     *
     * @param userDTOList 用户列表
     * @param groupDTO    用户组
     */
    @Override
    public void addUsersToGroup(List<SysUserDTO> userDTOList, SysGroupDTO groupDTO) throws Exception {
        Validate.notEmpty(userDTOList, "待分配的用户列表不允许为空");
        Validate.notNull(groupDTO, "用户组不能为空");
        Validate.notBlank(groupDTO.getId(), "用户组ID不能为空");

        List<SysUserGroup> userGroupList = new ArrayList<>(userDTOList.size());
        String groupId = groupDTO.getId();

        userDTOList.forEach(userDTO -> {
            Validate.notBlank(userDTO.getId(), "用户ID不能为空");
            SysUserGroup userGroup = new SysUserGroup();
            userGroup.setUserId(userDTO.getId());
            userGroup.setGroupId(groupId);
            userGroupList.add(userGroup);
        });
        sysUserGroupMapper.batchInsert(userGroupList);
    }

    /**
     * 从用户组中删除用户列表
     *
     * @param userDTOList 需要从用户组中删除的用户列表
     * @param groupDTO    用户组
     */
    @Override
    public void delUsersFromGroup(List<SysUserDTO> userDTOList, SysGroupDTO groupDTO) throws Exception {
        Validate.notEmpty(userDTOList, "待分配的用户列表不允许为空");
        Validate.notNull(groupDTO, "用户组不能为空");
        Validate.notBlank(groupDTO.getId(), "用户组ID不能为空");
        Set<String> userIdSet =userDTOList.stream().map(SysUserDTO::getId).collect(Collectors.toSet());
        String groupId = groupDTO.getId();

        UpdateWrapper<SysUserGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(FieldConstants.GROUP_ID, groupId).in(FieldConstants.USER_ID, userIdSet);
        sysUserGroupMapper.delete(updateWrapper);
    }

    /**
     * 重新分配用户到用户组，原先的该用户组下面的所有用户关联关系清空。
     *
     * @param userDTOList 用户列表
     * @param groupDTO    用户组
     */
    @Override
    public void reAllocateUsersToGroup(List<SysUserDTO> userDTOList, SysGroupDTO groupDTO) throws Exception {
        Validate.notEmpty(userDTOList, "待分配的用户列表不允许为空");
        Validate.notNull(groupDTO, "用户组不能为空");
        Validate.notBlank(groupDTO.getId(), "用户组ID不能为空");
        String groupId = groupDTO.getId();
        sysUserGroupMapper.deleteByMap(Collections.singletonMap(FieldConstants.GROUP_ID, groupId));

        List<SysUserGroup> userGroupList = new ArrayList<>(userDTOList.size());
        userDTOList.forEach(userDTO -> {
            SysUserGroup userGroup = new SysUserGroup();
            userGroup.setUserId(userDTO.getId());
            userGroup.setGroupId(groupId);
            userGroupList.add(userGroup);
        });
        sysUserGroupMapper.batchInsert(userGroupList);
    }

    /**
     * 将角色列表添加到指定的用户组
     *
     * @param sysRoleDTOList 需要分配到用户组的角色列表
     * @param groupDTO       用户组
     */
    @Override
    public void addRolesToGroup(List<SysRoleDTO> sysRoleDTOList, SysGroupDTO groupDTO) throws Exception {
        Validate.notEmpty(sysRoleDTOList, "待分配的角色列表不允许为空");
        Validate.notNull(groupDTO, "用户组不能为空");
        Validate.notBlank(groupDTO.getId(), "用户组ID不能为空");
        List<SysGroupRole> groupRoleList = new ArrayList<>(sysRoleDTOList.size());
        String groupId = groupDTO.getId();

        sysRoleDTOList.forEach(roleDTO -> {
            SysGroupRole groupRole = new SysGroupRole();
            groupRole.setRoleId(roleDTO.getId());
            groupRole.setGroupId(groupId);
            groupRoleList.add(groupRole);
        });
        sysGroupRoleMapper.batchInsert(groupRoleList);
    }

    /**
     * 将角色列表从指定的用户组中删除
     *
     * @param sysRoleDTOList 需要删除的角色列表
     * @param groupDTO       用户组
     */
    @Override
    public void delRolesFromGroup(List<SysRoleDTO> sysRoleDTOList, SysGroupDTO groupDTO) throws Exception {
        Validate.notEmpty(sysRoleDTOList, "待分配的角色列表不允许为空");
        Validate.notNull(groupDTO, "用户组不能为空");
        Validate.notBlank(groupDTO.getId(), "用户组ID不能为空");
        Set<String> roleIdSet =sysRoleDTOList.stream().map(SysRoleDTO::getId).collect(Collectors.toSet());
        String groupId = groupDTO.getId();

        UpdateWrapper<SysGroupRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(FieldConstants.GROUP_ID, groupId).in(FieldConstants.ROLE_ID, roleIdSet);
        sysGroupRoleMapper.delete(updateWrapper);
    }

    /**
     * 重新分配角色到用户组，删除原来该用户组关联的角色关系
     *
     * @param sysRoleDTOList 需要分配到用户组的角色列表
     * @param groupDTO       用户组
     */
    @Override
    public void reAllocateRolesToGroup(List<SysRoleDTO> sysRoleDTOList, SysGroupDTO groupDTO) throws Exception {
        Validate.notEmpty(sysRoleDTOList, "待分配的角色列表不允许为空");
        Validate.notNull(groupDTO, "用户组不能为空");
        Validate.notBlank(groupDTO.getId(), "用户组ID不能为空");
        String groupId = groupDTO.getId();
        sysGroupRoleMapper.deleteByMap(Collections.singletonMap(FieldConstants.GROUP_ID, groupId));

        List<SysGroupRole> groupRoleList = new ArrayList<>(sysRoleDTOList.size());
        sysRoleDTOList.forEach(roleDTO -> {
            SysGroupRole groupRole = new SysGroupRole();
            groupRole.setRoleId(roleDTO.getId());
            groupRole.setGroupId(groupId);
            groupRoleList.add(groupRole);
        });
        sysGroupRoleMapper.batchInsert(groupRoleList);
    }
}
