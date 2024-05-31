package com.easybbs.mapper;


import com.easybbs.entity.po.SysSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【sys_setting(系统设置信息)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.SysSetting
 */
//@Mapper
public interface SysSettingMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysSetting record);

    int insertSelective(SysSetting record);

    SysSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysSetting record);

    int updateByPrimaryKey(@Param("code") String code, @Param("jsonContent") String jsonContent);

    List<SysSetting> selectList();
}
