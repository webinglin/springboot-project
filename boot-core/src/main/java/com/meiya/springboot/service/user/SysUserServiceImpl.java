package com.meiya.springboot.service.user;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.meiya.springboot.bean.SysUser;
import com.meiya.springboot.bean.SysUserDept;
import com.meiya.springboot.bean.SysUserGroup;
import com.meiya.springboot.bean.SysUserRole;
import com.meiya.springboot.common.util.StringUtil;
import com.meiya.springboot.constants.BizConstants;
import com.meiya.springboot.constants.FieldConstants;
import com.meiya.springboot.dto.SysUserDTO;
import com.meiya.springboot.mapper.user.SysUserDeptMapper;
import com.meiya.springboot.mapper.user.SysUserGroupMapper;
import com.meiya.springboot.mapper.user.SysUserMapper;
import com.meiya.springboot.mapper.user.SysUserRoleMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户表业务层
 *
 * @author linwb
 * @since 2020-03-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysUserRoleMapper userRoleMapper;
    @Resource
    private SysUserDeptMapper userDeptMapper;
    @Resource
    private SysUserGroupMapper userGroupMapper;

    /**
     * 判断用户对象是否合法
     *
     * @param userDTO 用户对象
     * @throws Exception 非法参数抛出异常
     */
    private void checkUserProperties(SysUserDTO userDTO) throws Exception {
        // 判断是否为空
        if (userDTO == null) {
            throw new IllegalArgumentException("操作的对象为空");
        }
        StringUtil.isBlank(userDTO.getUsername(),"用户名为空");
        StringUtil.isBlank(userDTO.getIdcard(),"身份证为空");
    }

    /**
     * 设置对象的默认值
     *
     * @param userDTO 需要判断设置默认值的对象
     */
    private void setDefaultValues(SysUserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new IllegalArgumentException("对象为空");
        }

        if (userDTO.getStatus() == null) {
            userDTO.setStatus(1);
        }

        Date date = new Date();
        if (userDTO.getCreateTime() == null) {
            userDTO.setCreateTime(date);
        }
        userDTO.setUpdateTime(date);

        if (StringUtils.isBlank(userDTO.getId())) {
            userDTO.setId(StringUtil.getBase64Guid());
        }
    }

    private Map<String, Object> getDelUserIdMap(String userId) throws Exception {
        StringUtil.isBlank(userId,"用户ID为空");
        Map<String, Object> delMap = new HashMap<>();
        delMap.put(FieldConstants.USER_ID, userId);
        return delMap;
    }


    private void validRepeatUserName(SysUserDTO userDTO) throws Exception {
        if (StringUtils.isBlank(userDTO.getUsername())) {
            return;
        }
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put(FieldConstants.STATUS, BizConstants.STATUS_AVAIL);
        selectMap.put(FieldConstants.USER_NAME, userDTO.getUsername());
        List<SysUser> userList = userMapper.selectByMap(selectMap);
        if (CollectionUtils.isNotEmpty(userList)) {
            throw new IllegalArgumentException("用户名已经存在");
        }
    }

    private void validRepeatIdcard(SysUserDTO userDTO) throws Exception {
        if (StringUtils.isBlank(userDTO.getIdcard())) {
            return;
        }
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put(FieldConstants.STATUS, BizConstants.STATUS_AVAIL);
        selectMap.put(FieldConstants.USER_IDCARD, userDTO.getIdcard());
        List<SysUser> userList = userMapper.selectByMap(selectMap);
        if (CollectionUtils.isNotEmpty(userList)) {
            throw new IllegalArgumentException("身份证已经存在");
        }
    }

    private void validRepeatUserCode(SysUserDTO userDTO) throws Exception {
        if (StringUtils.isBlank(userDTO.getUserCode())) {
            return;
        }
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put(FieldConstants.STATUS, BizConstants.STATUS_AVAIL);
        selectMap.put(FieldConstants.USER_CODE, userDTO.getUserCode());
        List<SysUser> userList = userMapper.selectByMap(selectMap);
        if (CollectionUtils.isNotEmpty(userList)) {
            throw new IllegalArgumentException("编号已经存在");
        }
    }

    @Override
    public SysUser selectByID(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public SysUserDTO addUser(SysUserDTO userDTO) throws Exception {
        // 验证参数
        checkUserProperties(userDTO);
        List<SysUserDept> userDeptList = userDTO.getDeptList();
        if (CollectionUtils.isEmpty(userDeptList)) {
            throw new IllegalArgumentException("用户归属单位为空");
        }
        // 判断用户是否已经存在了
        validRepeatUserName(userDTO);
        validRepeatIdcard(userDTO);
        validRepeatUserCode(userDTO);

        // 设置默认值
        setDefaultValues(userDTO);

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDTO, sysUser);

        // 添加用户
        userMapper.insert(sysUser);
        // 添加用户-单位关系
        addUserDept(userDTO);
        // 添加用户-角色关系
        addUserRole(userDTO);
        // 添加用户-用户组关系
        addUserGoup(userDTO);
        return userDTO;
    }

    private void addUserDept(SysUserDTO userDTO) throws Exception {
        String userId = userDTO.getId();
        List<SysUserDept> userDeptList = userDTO.getDeptList();
        if (CollectionUtils.isNotEmpty(userDeptList)) {
            userDeptList.forEach(ud -> ud.setUserId(userId));
            userDeptMapper.batchInsert(userDeptList);
        }
    }

    private void addUserRole(SysUserDTO userDTO) throws Exception {
        String userId = userDTO.getId();
        List<SysUserRole> userRoleList = userDTO.getRoleList();
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            userRoleList.forEach(ur -> ur.setUserId(userId));
            userRoleMapper.batchInsert(userRoleList);
        }
    }

    private void addUserGoup(SysUserDTO userDTO) throws Exception {
        String userId = userDTO.getId();
        List<SysUserGroup> userGroupList = userDTO.getGroupList();
        if (CollectionUtils.isNotEmpty(userGroupList)) {
            userGroupList.forEach(ug -> ug.setUserId(userId));
            userGroupMapper.batchInsert(userGroupList);
        }
    }

    @Override
    public void delUser(SysUserDTO userDTO) throws Exception {
        StringUtil.isBlank(userDTO.getId(),"用户ID为空");
        SysUser u = new SysUser();
        BeanUtils.copyProperties(userDTO, u);
        u.setUpdateTime(new Date());
        u.setStatus(BizConstants.STATUS_DEL);
        if(StringUtils.isNotBlank(userDTO.getUpdatorId())) {
            u.setUpdatorId(userDTO.getUpdatorId());
        }
        userMapper.updateById(u);

        Map<String, Object> delMap = getDelUserIdMap(userDTO.getId());
        userGroupMapper.deleteByMap(delMap);
        userRoleMapper.deleteByMap(delMap);
        userDeptMapper.deleteByMap(delMap);
    }

    @Override
    public void editUser(SysUserDTO userDTO) throws Exception {
        // 验证参数
        checkUserProperties(userDTO);
        StringUtil.isBlank(userDTO.getId(),"用户ID为空");
        SysUser u = userMapper.selectById(userDTO.getId());

        // 判断修改了 用户名/身份证/用户编号 需要判断修改之后会不会跟数据库中已存在的用户重复，如果重复了，就不允许修改
        if (!u.getUsername().equals(userDTO.getUsername())) {
            validRepeatUserName(userDTO);
        }
        if (!u.getIdcard().equals(userDTO.getIdcard())) {
            validRepeatIdcard(userDTO);
        }
        if (StringUtils.isNotBlank(userDTO.getUserCode()) && !u.getUserCode().equals(userDTO.getUserCode())) {
            validRepeatUserCode(userDTO);
        }

        BeanUtils.copyProperties(userDTO, u);
        userMapper.updateById(u);
    }

    @Override
    public void editUserRole(SysUserDTO userDTO) throws Exception {
        userRoleMapper.deleteByMap(getDelUserIdMap(userDTO.getId()));
        // 重新添加用户-角色关系
        addUserRole(userDTO);
    }

    @Override
    public void editUserGroup(SysUserDTO userDTO) throws Exception {
        userGroupMapper.deleteByMap(getDelUserIdMap(userDTO.getId()));
        // 重新添加用户-用户组关系
        addUserGoup(userDTO);
    }

    @Override
    public void editUserDept(SysUserDTO userDTO) throws Exception {
        userDeptMapper.deleteByMap(getDelUserIdMap(userDTO.getId()));
        // 重新添加用户-单位关系
        addUserDept(userDTO);
    }

    @Override
    public void editUserAll(SysUserDTO userDTO) throws Exception {
        editUser(userDTO);
        editUserDept(userDTO);
        editUserRole(userDTO);
        editUserGroup(userDTO);
    }
}
