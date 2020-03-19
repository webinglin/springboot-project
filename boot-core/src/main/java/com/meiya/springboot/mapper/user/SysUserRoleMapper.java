package com.meiya.springboot.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiya.springboot.bean.SysUserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linwb
 * @since 2019/12/12
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Insert("<script>" +
                "insert into sys_user_role (user_id, role_id) values " +
                "<foreach collection='items' index='index' item='item' separator=',' >" +
                    "(#{item.userId}, #{item.roleId})" +
                "</foreach>" +
            "</script>")
    void batchInsert(@Param("items") List<SysUserRole> userRoleList) throws Exception ;

}
