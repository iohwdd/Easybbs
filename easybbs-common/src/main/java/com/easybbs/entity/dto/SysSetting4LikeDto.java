package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/10 16:53
 * @description: 点赞设置
 */
@Data
public class SysSetting4LikeDto {
    /**
     * 点赞数量阈值
     */
    private Integer likeDayCountThreshold;
}
