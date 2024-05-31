package com.easybbs.service;

import com.easybbs.entity.query.UserIntegralRecordQuery;
import com.easybbs.entity.vo.PaginationResultVO;

/**
 * @author: iohw
 * @date: 2024/5/23 21:21
 * @description:
 */
public interface UserIntegralRecordService {
    PaginationResultVO findByParam(Integer pageNo, UserIntegralRecordQuery query);
}
