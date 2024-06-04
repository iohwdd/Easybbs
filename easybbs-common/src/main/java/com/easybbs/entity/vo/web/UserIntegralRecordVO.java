package com.easybbs.entity.vo.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: iohw
 * @date: 2024/5/23 21:23
 * @description:
 */
@Data
public class UserIntegralRecordVO {
    /**
     * 记录ID
     */
    private Integer recordId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 操作类型
     */
    private String operTypeName;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
