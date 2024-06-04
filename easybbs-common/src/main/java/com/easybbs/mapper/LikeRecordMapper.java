package com.easybbs.mapper;


import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.query.LikeRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【like_record(点赞记录)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.LikeRecord
 */
@Mapper
public interface LikeRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(LikeRecord record);

    int insertSelective(LikeRecord record);

    LikeRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LikeRecord record);

    int updateByPrimaryKey(LikeRecord record);

    LikeRecord selectByObjectIdAndUserIdAndOpType(@Param("objectId") String objectId, @Param("userId") String userId, @Param("opType") Integer opType);

    void deleteByObjectIdAndUserIdAndOpType(@Param("objectId") String objId, @Param("userId") String userId, @Param("opType") Integer type);

    Integer findCountByParam(@Param("query") LikeRecordQuery query);

    List<LikeRecord> findListByParam(@Param("query") LikeRecordQuery query);
}
