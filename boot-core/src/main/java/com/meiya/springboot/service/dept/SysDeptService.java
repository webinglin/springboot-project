package com.meiya.springboot.service.dept;

import com.meiya.springboot.dto.SysDeptDTO;

/**
 * 单位基础逻辑定义
 * @author linwb
 * @since 2020/3/18
 */
public interface SysDeptService {

    /**
     * 添加单位信息
     * @param sysDeptDTO    待添加单位
     */
    SysDeptDTO addSysDept(SysDeptDTO sysDeptDTO) throws Exception;

    /**
     * 删除单位 如果存在下级单位/或者单位下存在用户，不允许删除
     * @param sysDeptDTO    删除的单位数据
     */
    void delSysDept(SysDeptDTO sysDeptDTO) throws Exception;

    /**
     * 修改单位信息
     * @param sysDeptDTO    需要修改的单位数据
     */
    SysDeptDTO editSysDep(SysDeptDTO sysDeptDTO) throws Exception;




}
