package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/10 16:49
 * @description: 审核设置
 */
@Data
public class SysSetting4AuditDto {
    /**
     * 帖子是否需要审核
     */
    private Boolean postAudit;
    /**
     * 评论是否需要审核
     */
    private Boolean commentAudit;

}
