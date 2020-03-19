package com.meiya.springboot.dto;

import com.meiya.springboot.bean.SysUser;
import com.meiya.springboot.bean.SysUserDept;
import com.meiya.springboot.bean.SysUserGroup;
import com.meiya.springboot.bean.SysUserRole;

import java.util.List;

/**
 * @author linwb
 * @since 2020/3/18
 */
public class SysUserDTO extends SysUser {
    private List<SysUserDept> deptList;
    private List<SysUserGroup> groupList;
    private List<SysUserRole> roleList;

    public List<SysUserDept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<SysUserDept> deptList) {
        this.deptList = deptList;
    }

    public List<SysUserGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<SysUserGroup> groupList) {
        this.groupList = groupList;
    }

    public List<SysUserRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysUserRole> roleList) {
        this.roleList = roleList;
    }
}
