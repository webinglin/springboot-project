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

    /**
     * 添加区域
     * @param areaDTO   待添加的区域对象
     */
    SysAreaDTO addArea(SysAreaDTO areaDTO) throws Exception;

    /**
     * 编辑区域对象
     * @param areaDTO   代编辑的区域对象
     */
    void editArea(SysAreaDTO areaDTO) throws Exception;

    /**
     * 删除区域对象
     * @param areaDTO   删除条件（默认按照ID删除）
     */
    void delArea(SysAreaDTO areaDTO) throws Exception;

}
