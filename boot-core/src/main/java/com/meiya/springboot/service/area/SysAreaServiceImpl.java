package com.meiya.springboot.service.area;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.meiya.springboot.bean.SysArea;
import com.meiya.springboot.bean.SysDept;
import com.meiya.springboot.common.util.StringUtil;
import com.meiya.springboot.constants.BizConstants;
import com.meiya.springboot.constants.FieldConstants;
import com.meiya.springboot.dto.SysAreaDTO;
import com.meiya.springboot.mapper.area.SysAreaMapper;
import com.meiya.springboot.mapper.dept.SysDeptMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linwb
 * @since 2020/3/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAreaServiceImpl implements SysAreaService {
    @Resource
    private SysAreaMapper sysAreaMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;

    private void checkUserProperties(SysAreaDTO areaDTO) throws Exception {
        if(areaDTO==null){
            throw new IllegalArgumentException("参数为空");
        }
        StringUtil.isBlank(areaDTO.getCode(), "区域编码不能为空");
    }

    private void validRepeatAreaCode(SysAreaDTO areaDTO) throws Exception {
        if (StringUtils.isBlank(areaDTO.getCode())) {
            return;
        }
        Map<String,Object> selectMap = new HashMap<>();
        selectMap.put(FieldConstants.STATUS, BizConstants.STATUS_AVAIL);
        selectMap.put(FieldConstants.CODE, areaDTO.getCode());
        List<SysArea> areaList = sysAreaMapper.selectByMap(selectMap);
        if(CollectionUtils.isNotEmpty(areaList)) {
            throw new Exception("修改的编码已经存在，不允许修改");
        }
    }

    @Override
    public SysArea selectById(String id) {
        return sysAreaMapper.selectById(id);
    }

    @Override
    public SysAreaDTO addArea(SysAreaDTO areaDTO) throws Exception {
        checkUserProperties(areaDTO);
        if(StringUtils.isBlank(areaDTO.getId())) {
            areaDTO.setId(StringUtil.getBase64Guid());
        }
        validRepeatAreaCode(areaDTO);
        StringUtil.isBlank(areaDTO.getCreatorId(), "创建者不能为空");
        StringUtil.isBlank(areaDTO.getName(), "区域名称不能为空");

        Date now = new Date();
        areaDTO.setCreateTime(now);
        areaDTO.setUpdateTime(now);
        areaDTO.setUpdatorId(areaDTO.getUpdatorId());
        areaDTO.setStatus(BizConstants.STATUS_AVAIL);
        SysArea sysArea = new SysArea();
        BeanUtils.copyProperties(areaDTO,sysArea);
        sysAreaMapper.insert(sysArea);
        return areaDTO;
    }

    @Override
    public void editArea(SysAreaDTO areaDTO) throws Exception {
        checkUserProperties(areaDTO);
        StringUtil.isBlank(areaDTO.getId(), "编辑区域ID不能为空");
        StringUtil.isBlank(areaDTO.getUpdatorId(), "变更者不能为空");

        SysArea sysArea = sysAreaMapper.selectById(areaDTO.getId());
        // 判断一下修改后的编码如果跟原有的编码不一样，但又再数据库中存在，那不允许修改
        if(!sysArea.getCode().equals(areaDTO.getCode())) {
            validRepeatAreaCode(areaDTO);
        }

        BeanUtils.copyProperties(areaDTO, sysArea);
        sysArea.setUpdateTime(new Date());
        sysAreaMapper.updateById(sysArea);
    }

    @Override
    public void delArea(SysAreaDTO areaDTO) throws Exception {
        if(areaDTO==null){
            throw new IllegalArgumentException("要删除的区域不能为空");
        }
        StringUtil.isBlank(areaDTO.getId(), "删除的区域ID不能为空");
        // 如果关联有效的单位了，不能直接删除
        Map<String,Object> selectMap = new HashMap<>();
        selectMap.put(FieldConstants.AREA_ID, areaDTO.getId());
        selectMap.put(FieldConstants.STATUS, BizConstants.STATUS_AVAIL);
        List<SysDept> deptList = sysDeptMapper.selectByMap(selectMap);
        if(CollectionUtils.isNotEmpty(deptList)){
            throw new Exception("区域已经关联了正在使用的单位，不允许删除");
        }
        UpdateWrapper<SysArea> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(FieldConstants.STATUS, BizConstants.STATUS_DEL);
        updateWrapper.set(FieldConstants.UPDATE_TIME, new Date());
        SysArea sysArea = new SysArea();
        sysArea.setId(areaDTO.getId());
        sysAreaMapper.update(sysArea, updateWrapper);
    }
}
