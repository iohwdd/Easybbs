package com.easybbs.entity.vo.web;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/24 14:51
 * @description:
 */
@Data
public class UserMessageCountVO {
    private Integer total;
    private Integer sys;
    private Integer reply;
    private Integer likePost;
    private Integer likeComment;
    private Integer downloadAttachment;
}
