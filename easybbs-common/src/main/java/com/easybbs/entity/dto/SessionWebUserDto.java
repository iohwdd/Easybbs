package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/14 9:59
 * @description: 登录用户信息
 */
@Data
public class SessionWebUserDto {
    private String nickName;
    private String province;
    private String userId;
    private Boolean isAdmin;
}
