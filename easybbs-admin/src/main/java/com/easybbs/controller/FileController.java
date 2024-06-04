package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.config.WebConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: iohw
 * @date: 2024/5/20 15:02
 * @description: 文件上传
 */
@RestController
@RequestMapping("/file")
public class FileController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    WebConfig webConfig;

    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder, @PathVariable("imageName") String imageName) {
        readImage(response, imageFolder, imageName);
    }

    @RequestMapping("/getAvatar/{userId}")
    public void getAvatar(HttpServletResponse response, @PathVariable("userId") String userId) {
        String avatarFolderName = Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR_NAME;
        String avatarPath = webConfig.getProjectFolder() + avatarFolderName + userId + Constants.AVATAR_SUFFIX;
        File avatarFolder = new File(avatarFolderName);
        if (!avatarFolder.exists()) {
            avatarFolder.mkdirs();
        }
        File file = new File(avatarPath);
        String imageName = userId + Constants.AVATAR_SUFFIX;
        if (!file.exists()) {
            imageName = Constants.AVATAR_DEFAULT;
        }
        readImage(response, Constants.FILE_FOLDER_AVATAR_NAME, imageName);
    }

    private void readImage(HttpServletResponse response, String imageFolder, String imageName) {
        ServletOutputStream sos = null;
        FileInputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            if (StringUtils.isEmpty(imageFolder) || StringUtils.isEmpty(imageName)) {
                return;
            }
            String imageSuffix = StringUtils.getFileSuffix(imageName);
            String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + imageFolder + "/" + imageName;
            if (Constants.FILE_FOLDER_TEMP_2.equals(imageFolder) || imageFolder.contains(Constants.FILE_FOLDER_AVATAR_NAME)) {
                filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + "/" + imageName;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            imageSuffix = imageSuffix.replace(".", "");
            if (!Constants.FILE_FOLDER_AVATAR_NAME.equals(imageFolder)) {
                response.setHeader("Cache-Control", "max-age=2592000");//非头像图片设置缓存
            }
            response.setContentType("image/" + imageSuffix);//设置图片响应格式
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            sos.write(baos.toByteArray());
        } catch (Exception e) {
            logger.error("读取图片异常", e);
            throw new BusinessException("读取图片异常");
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
