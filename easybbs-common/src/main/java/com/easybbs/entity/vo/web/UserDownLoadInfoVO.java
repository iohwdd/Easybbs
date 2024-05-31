package com.easybbs.entity.vo.web;

/**
 * @author: iohw
 * @date: 2024/5/16 19:41
 * @description:
 */
public class UserDownLoadInfoVO {
    private Integer userIntegral;
    private Boolean haveDownload;

    public UserDownLoadInfoVO() {
    }

    public Integer getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Integer userIntegral) {
        this.userIntegral = userIntegral;
    }

    public Boolean getHaveDownload() {
        return haveDownload;
    }

    public void setHaveDownload(Boolean haveDownload) {
        this.haveDownload = haveDownload;
    }
}
