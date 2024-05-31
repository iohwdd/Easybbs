package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/10 16:50
 * @description: 评论设置
 */
@Data
public class SysSetting4CommentDto {
    /**
     * 评论积分
     */
    private Integer commentIntegral;
    /**
     * 评论数量阈值
     */
    private Integer commentDayCountThreshold;
    /**
     * 评论是否打开
     */
    private Boolean commentOpen;
}
