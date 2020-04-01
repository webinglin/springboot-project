package com.meiya.springboot.mapper.group;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiya.springboot.bean.SysGroupRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户组-角色Mapper对象
 * @author linwb
 * @since 2019/12/12
 */
public interface SysGroupRoleMapper extends BaseMapper<SysGroupRole> {

    @Insert("<script>" +
            "insert into sys_group_role (role_id, group_id) values " +
            "<foreach collection='items' index='index' item='item' separator=',' >" +
            "(#{item.roleId}, #{item.groupId})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("items") List<SysGroupRole> groupRoleList)throws Exception ;

}
