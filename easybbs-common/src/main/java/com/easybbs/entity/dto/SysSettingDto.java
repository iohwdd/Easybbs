package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2024/5/10 16:58
 * @description:
 */
@Data
public class SysSettingDto {
    private SysSetting4AuditDto auditSetting;
    private SysSetting4CommentDto commentSetting;
    private SysSetting4EmailDto emailSetting;
    private SysSetting4LikeDto likeSetting;
    private SysSetting4PostDto postSetting;
    private SysSetting4RegisterDto registerSetting;
}
