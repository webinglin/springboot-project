import com.meiya.springboot.SpringWebApplication;
import com.meiya.springboot.bean.*;
import com.meiya.springboot.dto.SysUserDTO;
import com.meiya.springboot.service.privilege.SysPrivilegeService;
import com.meiya.springboot.service.user.SysUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 单位测试类
 * @author linwb
 * @since 2019/12/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringWebApplication.class)
public class JunitSpringWebApplicationTest {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPrivilegeService sysPrivilegeService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testMariadb() throws Exception{
        SysUser user = sysUserService.selectByID("1111111111111111111111");
        System.out.println(user.getUsername());
    }

    @Test
    public void testAddUser() throws Exception {
        SysUserDTO userDTO = new SysUserDTO();
        userDTO.setUsername("username");
        userDTO.setIdcard("idcard..");
        userDTO.setUserCode("pcode");

        userDTO.setRealName("realname");
        Date now = new Date();
        userDTO.setCreateTime(now);
        userDTO.setUpdateTime(now);
        userDTO.setCreatorId("a");
        userDTO.setUpdatorId("b");
        userDTO.setEmail("222");
        userDTO.setHeadImage("uri");
        userDTO.setHomeBgUrl("bgurl");
        userDTO.setLastLoginIp("ip");
        userDTO.setLastLoginTime(new Date());
        userDTO.setLoginCnt(222);
        userDTO.setOnlineTime(99);
        userDTO.setOrderNum(9);
        userDTO.setPassword("pwd");
        userDTO.setRemark("remark");
        userDTO.setSrcDataId("srcdataid");
        userDTO.setTheme("light");
        userDTO.setWechat("wechat");
        userDTO.setStatus(1);
        userDTO.setTelephone("189999");
        userDTO.setShortNum("shortnum");

        List<SysUserDept> userDeptList = new ArrayList<>();
        SysUserDept ud1 = new SysUserDept();
        ud1.setPosition("position1");
        ud1.setUserId("u1");
        ud1.setDeptId("d1");
        userDeptList.add(ud1);
        SysUserDept ud2 = new SysUserDept();
        ud2.setPosition("position2");
        ud2.setUserId("u2");
        ud2.setDeptId("d2");
        userDeptList.add(ud2);
        userDTO.setDeptList(userDeptList);

        List<SysUserGroup> userGroupList = new ArrayList<>();
        SysUserGroup g1 = new SysUserGroup();
        g1.setGroupId("g1");
        g1.setUserId("u1");
        SysUserGroup g2 = new SysUserGroup();
        g2.setGroupId("g2");
        g2.setUserId("u2");
        userGroupList.add(g1);
        userGroupList.add(g2);
        userDTO.setGroupList(userGroupList);

        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole r1 = new SysUserRole();
        r1.setUserId("u1");
        r1.setRoleId("r1");
        SysUserRole r2 = new SysUserRole();
        r2.setUserId("u2");
        r2.setRoleId("r2");
        userRoleList.add(r1);
        userRoleList.add(r2);
        userDTO.setRoleList(userRoleList);

        sysUserService.addUser(userDTO);
    }

    @Test
    public void testEditUser() throws Exception {
        SysUserDTO userDTO = new SysUserDTO();
        userDTO.setId("rOEPTqe1Q1N-parzb63MBw");

        userDTO.setUsername("uuuuu");
        userDTO.setIdcard("4444");
        userDTO.setUserCode("ccccc");

        userDTO.setRealName("realname");
        Date now = new Date();
        userDTO.setCreateTime(now);
        userDTO.setUpdateTime(now);
        userDTO.setCreatorId("a");
        userDTO.setUpdatorId("b");
        userDTO.setEmail("222");
        userDTO.setHeadImage("uri");
        userDTO.setHomeBgUrl("bgurl");
        userDTO.setLastLoginIp("ip");
        userDTO.setLastLoginTime(new Date());
        userDTO.setLoginCnt(222);
        userDTO.setOnlineTime(99);
        userDTO.setOrderNum(9);
        userDTO.setPassword("pwd");
        userDTO.setRemark("remark");
        userDTO.setSrcDataId("srcdataid");
        userDTO.setTheme("light");
        userDTO.setWechat("wechat");
        userDTO.setStatus(1);
        userDTO.setTelephone("189999");
        userDTO.setShortNum("shortnum");

        List<SysUserDept> userDeptList = new ArrayList<>();
        SysUserDept ud1 = new SysUserDept();
        ud1.setPosition("position1");
        ud1.setUserId("u1");
        ud1.setDeptId("deptid....");
        userDeptList.add(ud1);
        SysUserDept ud2 = new SysUserDept();
        ud2.setPosition("position2");
        ud2.setUserId("u2");
        ud2.setDeptId("d");
        userDeptList.add(ud2);
        userDTO.setDeptList(userDeptList);

        List<SysUserGroup> userGroupList = new ArrayList<>();
        SysUserGroup g1 = new SysUserGroup();
        g1.setGroupId("gggggg11111111");
        g1.setUserId("u111111");
        SysUserGroup g2 = new SysUserGroup();
        g2.setGroupId("group");
        g2.setUserId("u33333332");
        userGroupList.add(g1);
        userGroupList.add(g2);
        userDTO.setGroupList(userGroupList);

        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole r1 = new SysUserRole();
        r1.setUserId("u44444441");
        r1.setRoleId("r44444444441");
        SysUserRole r2 = new SysUserRole();
        r2.setUserId("u444442");
        r2.setRoleId("editr=R===");
        userRoleList.add(r1);
        userRoleList.add(r2);
        userDTO.setRoleList(userRoleList);

        sysUserService.editUserAll(userDTO);
    }

    @Test
    public void testDelUser() throws Exception {
        SysUserDTO userDTO = new SysUserDTO();
        userDTO.setId("1");

        userDTO.setUpdatorId("memememmemememe");
        sysUserService.delUser(userDTO);
    }


    @Test
    public void testQueryPrivilegesByUserId() throws Exception {
        List<SysPrivilege>  privilegeList = sysPrivilegeService.queryPrivilegesByUserId("rOEPTqe1Q1N-parzb63MBw");
        privilegeList.forEach(pri -> System.out.println(pri.getCode()));
    }


    @Test
    public void testRedis() throws Exception {
        stringRedisTemplate.opsForValue().set("TEST_REDIS", "TESTREIDS");
        Assert.assertEquals("TESTREIDS", stringRedisTemplate.opsForValue().get("TEST_REDIS"));
    }


    // 区域有哪些单位
    // 单位有哪些用户
    // 单位属于什么区域
    // 用户具备什么角色
    // 用户具备什么权限
    // 用户具备什么用户组
    // 用户具备什么单位
    // 角色有那些权限
    // 角色有那些用户组
    // 用户组有哪些角色
    // 用户组有哪些用户
    // 添加/修改/删除 用户
    // 添加/修改/删除 用户组
    // 添加/修改/删除 角色
    // 添加/修改/删除 权限
    // 添加/修改/删除 区域
}
