package com.easybbs.utils;

import com.easybbs.config.AppConfig;
import com.easybbs.entity.constants.Constants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    @Autowired
    AppConfig appConfig;

    public Boolean createThumbnail(File file, int thumbnailWidth, int thumbnailHeight, File targetFile) {
        try {
            // 使用NIO和内存映射文件读取源文件图片
            BufferedImage src = readImageWithNIO(file);
            if (src == null) {
                return false;
            }

            int sourceH = src.getHeight();
            int sourceW = src.getWidth();

            if (sourceW <= thumbnailWidth) {
                return false;
            }

            // 计算缩略图的高度
            int height = thumbnailWidth * sourceH / sourceW;
            if (height > thumbnailHeight) {
                height = thumbnailHeight;
                thumbnailWidth = height * sourceW / sourceH;
            }

            // 创建缩放后的图像
            BufferedImage dst = new BufferedImage(thumbnailWidth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = dst.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(src, 0, 0, thumbnailWidth, height, null);
            g.dispose();

            // 如果高度仍然大于期望的高度，则裁剪
            if (height > thumbnailHeight) {
                dst = dst.getSubimage(0, 0, thumbnailWidth, thumbnailHeight);
            }

            // 保存缩略图并设置压缩质量
            saveCompressedImage(dst, targetFile, 0.75f); // 调整压缩质量参数（0.0f - 1.0f）

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private BufferedImage readImageWithNIO(File file) throws IOException {
        Path path = Paths.get(file.toURI());
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel fileChannel = fis.getChannel()) {
            long size = fileChannel.size();
            ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);

            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            return ImageIO.read(new ByteArrayInputStream(bytes));
        }
    }

    private void saveCompressedImage(BufferedImage image, File file, float quality) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }

            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();
        }
    }

    public String resetImageHtml(String html) {
        String month = DateUtil.format(new Date(), "yyyyMM");
        List<String> imageList = getImageList(html);
        for (String img : imageList) {
            resetImage(img, month);
        }
        return month;
    }

    public String resetImage(String imagePath, String month) {
        if (StringUtils.isEmpty(imagePath) || !imagePath.contains(Constants.FILE_FOLDER_TEMP_2)) {
            return imagePath;
        }
        imagePath = imagePath.replace(Constants.READ_IMAGE_PATH, "");
        if (StringUtils.isEmpty(month)) {
            month = DateUtil.format(new Date(), "yyyyMM");
        }
        String imageFileName = month + "/" + imagePath.substring(imagePath.lastIndexOf("/") + 1);
        File targetFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + imageFileName);
        try {
            File tempFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imagePath);
            FileUtils.copyFile(tempFile, targetFile);
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("复制图片失败", e);
            return imagePath;
        }
        return imageFileName;
    }

    public List<String> getImageList(String html) {
        List<String> imageList = new ArrayList<>();
        String regEx_img = "<img\\s+[^>]*src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(html);
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                String imageUrl = m.group(1);
                imageList.add(imageUrl);
            }
        }
        return imageList;
    }
}