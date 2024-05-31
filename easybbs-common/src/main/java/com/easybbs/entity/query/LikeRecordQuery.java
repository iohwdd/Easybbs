package com.easybbs.entity.query;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/23 19:22
 * @description:
 */
@Data
public class LikeRecordQuery {
    private String authorUserId;
    private String userId;
    private Integer opType;

}
