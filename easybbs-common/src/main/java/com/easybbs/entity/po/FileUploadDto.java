package com.easybbs.entity.po;

/**
 * @author: iohw
 * @date: 2024/5/21 10:18
 * @description:
 */
public class FileUploadDto {
    private String localPath;
    private String originFileName;

    public FileUploadDto() {
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }
}
