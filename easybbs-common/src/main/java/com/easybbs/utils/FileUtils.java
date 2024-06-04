package com.easybbs.utils;

import com.easybbs.config.AppConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.FileUploadTypeEnum;
import com.easybbs.entity.po.FileUploadDto;
import com.easybbs.exception.BusinessException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

/**
 * @author: iohw
 * @date: 2024/5/20 21:38
 * @description:
 */
@Component
public class FileUtils {
    @Autowired
    AppConfig appConfig;
    @Autowired
    ImageUtils imageUtils;
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public FileUploadDto uploadFile2Local(MultipartFile file, String folder, FileUploadTypeEnum uploadTypeEnum) {
        try {
            FileUploadDto uploadDto = new FileUploadDto();
            String originFileName = file.getOriginalFilename();
            String fileSuffix = StringUtils.getFileSuffix(originFileName);
            if (!ArrayUtils.contains(uploadTypeEnum.getSuffixArray(), fileSuffix)) {
                throw new BusinessException("文件类型不正确");
            }
            //将图片以年月份分类，分别放在不同的文件夹
            String month = DateUtil.format(new Date(), "yyyyMM");
            String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
            File targetFileFolder = new File(baseFolder + folder + month + "/");
            if (!targetFileFolder.exists()) {
                targetFileFolder.mkdirs();
            }
            String fileName = StringUtils.getRandomString(Constants.LENGTH_15) + fileSuffix;

            File targetFile = new File(targetFileFolder.getPath() + "/" + fileName);
            String localPath = month + "/" + fileName;

            if (uploadTypeEnum == FileUploadTypeEnum.AVATAR) {
                //头像上传
                targetFileFolder = new File(baseFolder + Constants.FILE_FOLDER_AVATAR_NAME);
                targetFile = new File(targetFileFolder.getPath() + "/" + folder + Constants.AVATAR_SUFFIX);
                localPath = folder + Constants.AVATAR_SUFFIX;
            }
            file.transferTo(targetFile);
            //压缩评论图片
            if (uploadTypeEnum == FileUploadTypeEnum.COMMENT_IMAGE) {
                String thumbnailName = targetFile.getName().replace(".", "_.");
                File thumbnail = new File(targetFile.getParent() + "/" + thumbnailName);
                Boolean thumbnailCreated = imageUtils.createThumbnail(targetFile, 200, 200, thumbnail);
                if (!thumbnailCreated) {
                    org.apache.commons.io.FileUtils.copyFile(targetFile, thumbnail);
                }
            } else if (uploadTypeEnum == FileUploadTypeEnum.AVATAR || uploadTypeEnum == FileUploadTypeEnum.ARTICLE_COVER) {
                imageUtils.createThumbnail(targetFile, 200, 200, targetFile);
            }
            uploadDto.setLocalPath(localPath);
            uploadDto.setOriginFileName(originFileName);
            return uploadDto;
        } catch (BusinessException e) {
            logger.error("文件上传失败", e);
            throw e;
        } catch (Exception e) {
            logger.error("文件上传失败", e);
        }
        return null;
    }
}