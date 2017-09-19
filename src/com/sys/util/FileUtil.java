package com.sys.util;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * description:
 * Created on 2017/9/19 12:59
 **/
public class FileUtil {

    public static Boolean upload(MultipartFile file,String fileType) throws Exception {
        if(fileType.equals("silk")){
            // 如果文件不为空，写入上传路径
            if(!file.isEmpty()){
                // 上传文件路径 F:\idea\weixin\target\weixin-1.0-SNAPSHOT\speech
                //String dirPath = request.getServletContext().getRealPath("/speech/");
                String dirPath= PropertyUtil.getProperty("filePath.properties","silk.path");
                // 得到上传时的文件名
                String filename = file.getOriginalFilename();
                // 判断父目录的路径是否存在，如果不存在就创建一个
                File filepath = new File(dirPath,filename);
                if (!filepath.getParentFile().exists()) {
                    filepath.getParentFile().mkdirs();
                }
                // 将上传文件保存到一个目标文件当中
                file.transferTo(new File(dirPath+File.separator+ filename));
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }
    @RequestMapping(value="/download")
    public static ResponseEntity<byte[]> download(String filename)throws Exception{
        // 下载文件路径
        String path = PropertyUtil.getProperty("filePath.properties","song.path");
        File file = new File(path+File.separator+ filename);
        HttpHeaders headers = new HttpHeaders();
        // 下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
        // 通知浏览器以attachment（下载方式）打开图片
        headers.setContentDispositionFormData("attachment", downloadFielName);
        // application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 201 HttpStatus.CREATED
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
