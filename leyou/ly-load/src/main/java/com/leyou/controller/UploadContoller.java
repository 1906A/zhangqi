package com.leyou.controller;


import com.leyou.service.UploadServcie;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("upload")
public class UploadContoller {

    @Autowired
    public UploadServcie uploadServcie;


    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        //得到图片
        String url = uploadServcie.uploadImages(file);
        //判断图片是否为空
        if (StringUtils.isBlank(url)) {

            //图片为空，报400
            return ResponseEntity.badRequest().build();
        }
        //body 返回读取的图片内容
        return ResponseEntity.status(HttpStatus.CREATED).body(url);

    }


}
