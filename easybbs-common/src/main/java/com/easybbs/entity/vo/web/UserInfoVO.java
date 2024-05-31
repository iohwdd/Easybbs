package com.easybbs.entity.vo.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: iohw
 * @date: 2024/5/23 19:14
 * @description:
 */
@Data
public class UserInfoVO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 0:女 1:男
     */
    private Integer sex;

    /**
     * 个人描述
     */
    private String personDescription;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date joinTime;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;


    /**
     * 当前积分
     */
    private Integer currentIntegral;

    private Integer postCount;
    private Integer likeCount;
}
