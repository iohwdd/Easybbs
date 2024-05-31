package com.easybbs.entity.query;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/24 14:55
 * @description:
 */
@Data
public class UserMessageQuery {
    private String receivedUserId;
    private Integer messageType;
    /**
     * 1：未读 2：已读
     */
    private Integer status;
}
