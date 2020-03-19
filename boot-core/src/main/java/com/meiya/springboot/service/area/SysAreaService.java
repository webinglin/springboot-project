package com.meiya.springboot.service.area;

import com.meiya.springboot.bean.SysArea;
import com.meiya.springboot.dto.SysAreaDTO;

/**
 * @author linwb
 * @since 2020/3/18
 */
public interface SysAreaService {
    /**
     * 根据ID查询区域
     * @param id    区域ID
     */
    SysArea selectById(String id) ;

    SysAreaDTO addArea(SysAreaDTO areaDTO) throws Exception;

    void editArea(SysAreaDTO areaDTO) throws Exception;

    void delArea(SysAreaDTO areaDTO) throws Exception;

}
