package com.meiya.springboot.service.dept;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meiya.springboot.bean.SysDept;
import com.meiya.springboot.common.util.StringUtil;
import com.meiya.springboot.constants.BizConstants;
import com.meiya.springboot.constants.FieldConstants;
import com.meiya.springboot.dto.SysDeptDTO;
import com.meiya.springboot.mapper.dept.SysDeptMapper;
import com.meiya.springboot.mapper.user.SysUserDeptMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linwb
 * @since 2020/3/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDeptServiceImpl implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserDeptMapper sysUserDeptMapper;

    /**
     * 校验单位名称是否重复
     * @param deptDTO   需要校验的单位信息
     */
    private void validRepeatDeptName(SysDeptDTO deptDTO) throws IllegalArgumentException{
        Validate.notBlank(deptDTO.getId(), "单位ID不允许为空");
        Validate.notBlank(deptDTO.getName(), "单位名称不允许为空");

        Map<String,Object> deptNameMap = new HashMap<>();
        deptNameMap.put(FieldConstants.STATUS, BizConstants.STATUS_AVAIL);
        deptNameMap.put(FieldConstants.NAME, deptDTO.getName());
        if(CollectionUtils.isNotEmpty(sysDeptMapper.selectByMap(deptNameMap))){
            throw new IllegalArgumentException("单位名称重复");
        }
    }

    /**
     * 验证单位编码是否重复
     * @param deptDTO   待验证的单位对象
     */
    private void validRepeatDeptCode(SysDeptDTO deptDTO) throws IllegalArgumentException{
        Validate.notBlank(deptDTO.getId(), "单位ID不允许为空");
        Validate.notBlank(deptDTO.getCode(), "单位编码不允许为空");

        Map<String,Object> deptCodeMap = new HashMap<>();
        deptCodeMap.put(FieldConstants.STATUS, BizConstants.STATUS_AVAIL);
        deptCodeMap.put(FieldConstants.CODE, deptDTO.getCode());
        if(CollectionUtils.isNotEmpty(sysDeptMapper.selectByMap(deptCodeMap))){
            throw new IllegalArgumentException("单位编码重复");
        }
    }

    @Override
    public SysDeptDTO addSysDept(SysDeptDTO sysDeptDTO) throws Exception {
        Validate.notNull(sysDeptDTO, "单位对象为空");
        Validate.notBlank(sysDeptDTO.getCreatorId(), "创建用户不允许为空");
        Validate.notBlank(sysDeptDTO.getParentId(), "上级单位ID不允许为空");

        if(StringUtils.isBlank(sysDeptDTO.getId())){
            sysDeptDTO.setId(StringUtil.getBase64Guid());
        }
        sysDeptDTO.setCreateTime(new Date());
        sysDeptDTO.setUpdateTime(sysDeptDTO.getCreateTime());
        sysDeptDTO.setUpdatorId(sysDeptDTO.getCreatorId());
        sysDeptDTO.setStatus(BizConstants.STATUS_AVAIL);

        validRepeatDeptName(sysDeptDTO);
        validRepeatDeptCode(sysDeptDTO);

        SysDept dept = new SysDept();
        BeanUtils.copyProperties(sysDeptDTO, dept);
        sysDeptMapper.insert(sysDeptDTO);
        return sysDeptDTO;
    }

    /**
     * 删除单位 如果存在下级单位/或者单位下存在用户，不允许删除
     * @param sysDeptDTO 删除的单位数据
     */
    @Override
    public void delSysDept(SysDeptDTO sysDeptDTO) throws Exception {
        Validate.notNull(sysDeptDTO, "单位对象为空");
        String deptId = sysDeptDTO.getId();
        Validate.notBlank(deptId, "单位ID不允许为空");

        // 单位是否存在 非删除状态的单位，如果都不存在，不需要删除了
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FieldConstants.ID,deptId).ne(FieldConstants.STATUS,BizConstants.STATUS_DEL);
        if(CollectionUtils.isEmpty(sysDeptMapper.selectList(queryWrapper))){
            throw new IllegalArgumentException("不存在需要删除的单位(非删除状态的单位)");
        }

        // 根据单位ID查询有效用户
        Map<String,Object> userDeptMap = new HashMap<>();
        userDeptMap.put(FieldConstants.DEPT_ID, deptId);
        if(CollectionUtils.isNotEmpty(sysUserDeptMapper.selectByMap(userDeptMap))){
            throw new Exception("单位下面还存在用户，不允许删除");
        }

        // 根据单位ID查询下级单位
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FieldConstants.PARENT_ID, deptId).eq(FieldConstants.STATUS,BizConstants.STATUS_AVAIL);
        if(CollectionUtils.isNotEmpty(sysDeptMapper.selectList(queryWrapper))){
            throw new Exception("单位还存在下级单位，不允许直接删除");
        }

        SysDept sysDept = new SysDept();
        sysDept.setStatus(BizConstants.STATUS_DEL);
        sysDept.setUpdateTime(new Date());
        sysDept.setId(deptId);
        sysDeptMapper.updateById(sysDept);
    }

    /**
     * 修改单位信息
     * @param sysDeptDTO 需要修改的单位数据
     */
    @Override
    public SysDeptDTO editSysDep(SysDeptDTO sysDeptDTO) throws Exception {
        Validate.notNull(sysDeptDTO, "单位对象为空");
        Validate.notBlank(sysDeptDTO.getParentId(), "上级单位ID不允许为空");
        Validate.notBlank(sysDeptDTO.getId(), "单位ID不允许为空");
        Validate.notBlank(sysDeptDTO.getName(), "单位名称不允许为空");
        Validate.notBlank(sysDeptDTO.getCode(), "单位编码不允许为空");

        SysDept sysDept = sysDeptMapper.selectById(sysDeptDTO.getId());
        if(!sysDeptDTO.getName().equals(sysDept.getName())) {
            validRepeatDeptName(sysDeptDTO);
        }
        if(!sysDeptDTO.getCode().equals(sysDept.getCode())) {
            validRepeatDeptCode(sysDeptDTO);
        }

        sysDeptDTO.setUpdateTime(new Date());
        BeanUtils.copyProperties(sysDeptDTO, sysDept);

        sysDeptMapper.updateById(sysDept);
        return sysDeptDTO;
    }
}
