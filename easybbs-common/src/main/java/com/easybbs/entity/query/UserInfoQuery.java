package com.easybbs.entity.query;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/27 19:22
 * @description:
 */
@Data
public class UserInfoQuery {
    private String userId;
    private String email;
    private String password;
    private Integer pageNo;
    private Integer pageSize;
    private String nickNameFuzzy;
    private Integer sex;
    private Integer status;
}
