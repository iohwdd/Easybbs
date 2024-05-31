package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/10 16:54
 * @description: 发帖设置
 */
@Data
public class SysSetting4PostDto {
    /**
     * 发帖积分
     */
    private Integer postIntegral;
    /**
     * 一天发帖数量
     */
    private Integer postDayCountThreshold;
    /**
     * 每天上传图片数量
     */
    private Integer dayImageUploadCount;
    /**
     * 附件大小 单位 mb
     */
    private Integer attachmentSize;
}
