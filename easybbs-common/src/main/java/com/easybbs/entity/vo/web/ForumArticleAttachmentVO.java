package com.easybbs.entity.vo.web;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: iohw
 * @date: 2024/5/15 16:22
 * @description:
 */
@Data
public class ForumArticleAttachmentVO implements Serializable {
    private String fileId;
    private Long fileSize;
    private String fileName;
    private Integer downloadCount;
    private Integer fileType;
    private Integer Integral;
}
