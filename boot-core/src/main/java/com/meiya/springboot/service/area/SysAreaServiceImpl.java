package com.meiya.springboot.service.area;

import com.meiya.springboot.bean.SysArea;
import com.meiya.springboot.dto.SysAreaDTO;
import com.meiya.springboot.mapper.area.SysAreaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author linwb
 * @since 2020/3/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAreaServiceImpl implements SysAreaService {
    @Resource
    private SysAreaMapper sysAreaMapper;

    @Override
    public SysArea selectById(String id) {
        return sysAreaMapper.selectById(id);
    }

    @Override
    public SysAreaDTO addArea(SysAreaDTO areaDTO) throws Exception {
        return null;
    }

    @Override
    public void editArea(SysAreaDTO areaDTO) throws Exception {

    }

    @Override
    public void delArea(SysAreaDTO areaDTO) throws Exception {

    }
}
