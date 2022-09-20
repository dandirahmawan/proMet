package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Service.FileService;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fs;

    @PostMapping("/upload")
    public Object upload(MultipartFile file){
        System.out.println(file.getOriginalFilename());
        return fs.upload(file);
    }

    @GetMapping("/download/{idLampiran}")
    public void download(@PathVariable String idLampiran, HttpServletResponse response){
        fs.download(response, idLampiran);
    }

    @GetMapping("/qr/{id}")
    public void qrImgae(HttpServletResponse response, @PathVariable String id){
        fs.downloadQr(response, id);
    }
}
