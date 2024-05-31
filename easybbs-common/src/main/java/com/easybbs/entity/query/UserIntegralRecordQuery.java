package com.easybbs.entity.query;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/23 21:26
 * @description:
 */
@Data
public class UserIntegralRecordQuery {
    private String userId;
    private String createTimeStart;
    private String createTimeEnd;
}
