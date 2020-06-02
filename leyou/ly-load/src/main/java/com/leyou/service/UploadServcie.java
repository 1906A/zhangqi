package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadServcie {


    /*将符合格式的图片类型存储进数组*/
    private static final List<String> content_types = Arrays.asList("image/png", "image/jpeg", "image/gif",
             "image/TIFF" , "image/tiff" , "image/PNG" , "image/GIF", "image/JPEG"

    );

    /*设置文件上传失败的打印日志*/
    private static final Logger logger = LoggerFactory.getLogger(UploadServcie.class);


    /*FastFDS客户端*/
    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImages(MultipartFile file) {

        //原始文件
        String originalFilename = file.getOriginalFilename();

        originalFilename.split(".");

        //1文件类型

        String contentType = file.getContentType();
        if (!content_types.contains(contentType)) {
            logger.info("文件上传失败:文件类型不合法!" + originalFilename);
            return null;
        }


        try {
            //2校验文件内容

            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                logger.info("文件上传失败:文件类型不合法! 空" + originalFilename);
                return null;

            }

            //3 本地保存服务器

            //file.transferTo(Paths.get(String.valueOf(new File("C:\\images\\" + originalFilename))));

            //保存到服务器
             /*1.获取文件后缀*/
            String lastname = StringUtils.substringAfterLast(originalFilename, ".");

            //        服务器对象                                    文件             文件大小       后缀名      原数据
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), lastname, null);

            //4 返回Url
            //本地服务器
            //return "http://image.leyou.com/" + originalFilename;

            //远程Linux服务器
            return "http://image.leyou.com/" + storePath.getFullPath();

        } catch (IOException e) {
            logger.info("服务器异常!" + originalFilename + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
