package com.easybbs.entity.enums;

import com.easybbs.entity.constants.Constants;

/**
 * @author: iohw
 * @date: 2024/5/21 10:19
 * @description: 上传文件类型
 */
public enum FileUploadTypeEnum {
    ARTICLE_COVER("文章封面", Constants.IMAGE_SUFFIX),
    ARTICLE_ATTACHMENT("文章附件", new String[]{".zip", ".rar", ".ZIP", ".RAR"}),
    COMMENT_IMAGE("评论图片", Constants.IMAGE_SUFFIX),
    AVATAR("个人头像", Constants.IMAGE_SUFFIX);
    private String desc;
    private String[] suffixArray;

    FileUploadTypeEnum(String desc, String[] suffixArray) {
        this.desc = desc;
        this.suffixArray = suffixArray;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String[] getSuffixArray() {
        return suffixArray;
    }

    public void setSuffixArray(String[] suffixArray) {
        this.suffixArray = suffixArray;
    }
}
