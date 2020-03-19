package com.meiya.springboot.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiya.springboot.bean.SysUserDept;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linwb
 * @since 2019/12/12
 */
public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {

    @Insert("<script>" +
                "insert into sys_user_dept (user_id, dept_id, position) values " +
                "<foreach collection='items' index='index' item='item' separator=',' >" +
                    "(#{item.userId}, #{item.deptId}, #{item.position})" +
                "</foreach>" +
            "</script>")
    void batchInsert(@Param("items") List<SysUserDept> userDeptList) throws Exception;

}
