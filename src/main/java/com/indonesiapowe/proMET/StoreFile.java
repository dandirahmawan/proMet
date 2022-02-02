package com.indonesiapowe.proMET;

import com.indonesiapowe.proMET.Model.TblLampiranReservasi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class StoreFile {
    private final Path dirUpload = Paths.get("../upload");

    public StoreFile(){
        File file = new File(dirUpload.toUri());
        if(!file.exists()){
            file.mkdir();
        }
    }

    public Map<String, Object> upload(MultipartFile file){
        Long ts =  System.currentTimeMillis();
        String fileNameOri = file.getOriginalFilename();
        String fileSeparator = File.separator;

        int idx = file.getOriginalFilename().lastIndexOf(".");
        int len = fileNameOri.length();
        String fileNameUpload = fileNameOri.substring(0, idx).replace(" ", "_")+"_"+ts;
        String ext = fileNameOri.substring(idx, len);

        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        fileNameUpload = fileNameUpload+ext;
        Path fullPath = dirUpload.toAbsolutePath();

        try {
            Files.copy(file.getInputStream(), this.dirUpload.resolve(fileNameUpload));
            map.put("success", true);
            map.put("fileName", fileNameOri);
            map.put("fileNameUpload", fileNameUpload);
            map.put("path", fullPath+fileSeparator+fileNameUpload);
            return map;
        } catch (Exception e) {
            return map;
        }
    }

    public void delete(String path){
        File file = new File(path);
        if(file.delete()){
            System.out.println("delete file upload : "+path);
        }
    }

    public void download (HttpServletResponse response, String path, String fileName) {
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        File file = new File(path);
        try {
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
