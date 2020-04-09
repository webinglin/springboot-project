package com.meiya.springboot.mapper.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiya.springboot.bean.SysGroupRole;
import com.meiya.springboot.bean.SysRolePrivilege;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色-权限Mapper对象
 * @author linwb
 * @since 2019/12/12
 */
public interface SysRolePrivilegeMapper extends BaseMapper<SysRolePrivilege> {


    @Insert("<script>" +
            "insert into sys_role_privilege (role_id, privilege_id) values " +
            "<foreach collection='items' index='index' item='item' separator=',' >" +
            "(#{item.roleId}, #{item.privilegeId})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("items") List<SysRolePrivilege> rolePrivilegeList) throws Exception ;
}
