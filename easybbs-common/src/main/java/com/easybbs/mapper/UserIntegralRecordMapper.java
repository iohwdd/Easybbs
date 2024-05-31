package com.easybbs.mapper;

import com.easybbs.entity.po.UserIntegralRecord;
import com.easybbs.entity.query.UserIntegralRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【user_integral_record(用户积分记录表)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.UserIntegralRecord
 */
//@Mapper
public interface UserIntegralRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserIntegralRecord record);

    int insertSelective(UserIntegralRecord record);

    UserIntegralRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserIntegralRecord record);

    int updateByPrimaryKey(UserIntegralRecord record);

    List<UserIntegralRecord> findByParam(@Param("query") UserIntegralRecordQuery query);
}
