package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/10 19:34
 * @description: 系统子模块设置 -> 这里为反射设计
 */
public enum SysSettingCodeEnum {
    AUDIT("audit","com.easybbs.entity.dto.SysSetting4AuditDto","auditSetting","审核设置"),
    COMMENT("comment","com.easybbs.entity.dto.SysSetting4CommentDto","commentSetting","评论设置"),
    EMAIL("email","com.easybbs.entity.dto.SysSetting4EmailDto","emailSetting","邮件设置"),
    LIKE("like","com.easybbs.entity.dto.SysSetting4LikeDto","likeSetting","点赞设置"),
    POST("post","com.easybbs.entity.dto.SysSetting4PostDto","postSetting","发帖设置"),
    REGISTER("register","com.easybbs.entity.dto.SysSetting4RegisterDto","registerSetting","注册设置");
    private String code;
    private String classz;
    private String propName;
    private String desc;

    SysSettingCodeEnum(String code, String classz, String propName, String desc) {
        this.code = code;
        this.classz = classz;
        this.propName = propName;
        this.desc = desc;
    }
    public static SysSettingCodeEnum getByCode(String code) {
        for(SysSettingCodeEnum item : SysSettingCodeEnum.values()) {
            if(item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }

    public String getClassz() {
        return classz;
    }

    public String getPropName() {
        return propName;
    }

    public String getDesc() {
        return desc;
    }
}
