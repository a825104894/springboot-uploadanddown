package com.terry.springbootuploadanddown.controller;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RestController
public class HelloController {
    @PostMapping("/handleUpload")
    public String handleUpload(HttpServletRequest request,@RequestParam("files") MultipartFile[] files){
        try {
        String serverFilePath=((MultipartHttpServletRequest)request).getSession().getServletContext().getRealPath("/upload/");
        File serverPath=new File(serverFilePath);
        if(!serverPath.exists()){
            serverPath.mkdir();
        }
        for (int i = 0; i < files.length; i++) {
            MultipartFile file=files[i];
            if(file.getSize()>0) {
                String uploadFileName = file.getOriginalFilename();
                //这用sb优化，因为懒所以。。。
                String fileName = UUID.randomUUID() + "_" + uploadFileName;
                File uploadFile=new File(serverFilePath+fileName);
                file.transferTo(uploadFile);
                //应该控制台打印记录，日志记录
                System.out.println(uploadFile.getCanonicalPath());
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
            return"上传失败";
        }
        return "上传成功";
    }



    @RequestMapping("handleDown")
    public ResponseEntity<byte[]> handleDownload(HttpServletRequest request) throws IOException {
        Resource resource=new ClassPathResource("4000配置.png");
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode("4000配置.png","utf-8"));
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(org.apache.commons.io.FileUtils.readFileToByteArray(resource.getFile()),httpHeaders,HttpStatus.CREATED);
    }
}
