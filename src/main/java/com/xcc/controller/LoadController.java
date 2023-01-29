package com.xcc.controller;

import com.xcc.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author xcc
 * @version 1.0
 */

@RestController
@RequestMapping("/common")
public class LoadController {

    @Value("${reggie.basePath}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        //file 是一个临时文件,需要转存到指定位置,否本本次请求完成之后会删除

        //原始文件名字
        String originalFilename = file.getOriginalFilename();

        //文件后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //新文件名 防止覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //转存到服务器本地
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);

    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流,读取该文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流 输出到浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");//设置类型

            int len = 0;
            byte[] bytes = new byte[1024];
            //每次读1024存入数组
            while ((len = fileInputStream.read(bytes)) != -1) {
//                每次从数组中从0读到 len 长度
                outputStream.write(bytes, 0, len);
                //刷新
                outputStream.flush();
            }
            //关闭资源
            fileInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
