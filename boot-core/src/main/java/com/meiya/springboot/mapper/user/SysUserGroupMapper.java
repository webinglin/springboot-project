package com.meiya.springboot.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiya.springboot.bean.SysUserGroup;
import com.meiya.springboot.bean.SysUserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linwb
 * @since 2019/12/12
 */
public interface SysUserGroupMapper extends BaseMapper<SysUserGroup> {

    @Insert("<script>" +
                "insert into sys_user_group (user_id, group_id) values " +
                "<foreach collection='items' index='index' item='item' separator=',' >" +
                    "(#{item.userId}, #{item.groupId})" +
                "</foreach>" +
            "</script>")
    void batchInsert(@Param("items") List<SysUserGroup> userGroupList) throws Exception ;
}
