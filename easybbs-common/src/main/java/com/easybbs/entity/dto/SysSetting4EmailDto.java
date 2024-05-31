package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/10 16:52
 * @description: 邮箱设置
 */
@Data
public class SysSetting4EmailDto {
    //邮箱标题
    private String emailTitle;
    //邮箱内容
    private String emailContent;

}
