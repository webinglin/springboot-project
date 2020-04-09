package com.meiya.springboot.service.dept;

import com.meiya.springboot.bean.SysDept;
import com.meiya.springboot.common.util.StringUtil;
import com.meiya.springboot.constants.BizConstants;
import com.meiya.springboot.dto.SysDeptDTO;
import com.meiya.springboot.mapper.dept.SysDeptMapper;
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

    /**
     * 校验单位名称是否重复
     * @param deptDTO   需要校验的单位信息
     */
    private void validRepeatDeptName(SysDeptDTO deptDTO) throws IllegalArgumentException{
        Validate.notBlank(deptDTO.getId(), "单位ID不允许为空");
        Validate.notBlank(deptDTO.getName(), "单位名称不允许为空");

        Map<String,Object> deptNameMap = new HashMap<>();
        deptNameMap.put("STATUS", BizConstants.STATUS_AVAIL);
        deptNameMap.put("DEPT_NAME", deptDTO.getName());
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
        deptCodeMap.put("STATUS", BizConstants.STATUS_AVAIL);
        deptCodeMap.put("DEPT_CODE", deptDTO.getCode());
        if(CollectionUtils.isNotEmpty(sysDeptMapper.selectByMap(deptCodeMap))){
            throw new IllegalArgumentException("单位编码重复");
        }
    }

    @Override
    public SysDeptDTO addSysDept(SysDeptDTO sysDeptDTO) throws Exception {
        Validate.notNull(sysDeptDTO, "单位对象为空");
        if(StringUtils.isBlank(sysDeptDTO.getId())){
            sysDeptDTO.setId(StringUtil.getBase64Guid());
        }
        sysDeptDTO.setCreateTime(new Date());

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




    }

    /**
     * 修改单位信息
     * @param sysDeptDTO 需要修改的单位数据
     */
    @Override
    public SysDeptDTO editSysDep(SysDeptDTO sysDeptDTO) throws Exception {
        Validate.notNull(sysDeptDTO, "单位对象为空");

        // 查询 判断


//        validRepeatDeptName(sysDeptDTO);
//        validRepeatDeptCode(sysDeptDTO);

        return null;
    }
}
