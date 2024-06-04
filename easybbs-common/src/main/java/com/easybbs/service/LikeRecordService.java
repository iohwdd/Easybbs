package com.easybbs.service;

import com.easybbs.entity.enums.OperRecordTypeEnum;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.query.LikeRecordQuery;
import com.easybbs.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/15 19:47
 * @description:
 */
public interface LikeRecordService {
    LikeRecord getLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer type);

    void doLike(String objectId, String userId, String nickName, OperRecordTypeEnum operRecordTypeEnum);

    Integer findCountByParam(LikeRecordQuery recordQuery);

    List<LikeRecord> findListByParam(LikeRecordQuery recordQuery);

}
