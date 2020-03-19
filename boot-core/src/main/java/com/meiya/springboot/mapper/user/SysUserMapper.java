package com.meiya.springboot.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiya.springboot.bean.SysUser;
import org.apache.ibatis.annotations.Delete;

/**
 * 用户Mapper对象
 * @author linwb
 * @since 2019/12/12
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    void insertUserGroup(String userId, String groupId);

    void insertUserRole(String userId, String roleId);

    void insertUserDept(String userId, String deptId);

    @Delete("DELETE FROM SYS_USER_GROUP WHERE USER_ID = #{userId}")
    void delUserGroups(String userId);

    @Delete("DELETE FROM SYS_USER_ROLE WHERE USER_ID = #{userId}")
    void delUserRoles(String userId);

    @Delete("DELETE FROM SYS_USER_DEPT WHERE USER_ID = #{userId}")
    void delUserDepts(String userId);
}
