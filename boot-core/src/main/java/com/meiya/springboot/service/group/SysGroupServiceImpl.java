package com.meiya.springboot.service.group;

import com.meiya.springboot.mapper.group.SysGroupRoleMapper;
import com.meiya.springboot.mapper.user.SysUserGroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author linwb
 * @since 2020/3/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysGroupServiceImpl implements SysGroupService {

    @Resource
    private SysUserGroupMapper sysUserGroupMapper;
    @Resource
    private SysGroupRoleMapper sysGroupRoleMapper;




}
