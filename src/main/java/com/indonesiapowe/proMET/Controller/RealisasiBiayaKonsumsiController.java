package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Model.TblRealisasiBiayaKonsumsi;
import com.indonesiapowe.proMET.Service.RealisasiBiayaKonsumsiService;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/realisasi_biaya_konsumsi")
public class RealisasiBiayaKonsumsiController {

    @Autowired
    RealisasiBiayaKonsumsiService rbks;

    @Autowired
    StoreFile sf;

    @PostMapping()
    public Object post(@RequestBody TblRealisasiBiayaKonsumsi tblRealisasiBiayaKonsumsi){
        return rbks.post(tblRealisasiBiayaKonsumsi);
    }

    @GetMapping()
    public Object getAll(@RequestParam(name = "username", required = false) String username){
        return rbks.getAll(username);
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable(name = "id") String id){
        return rbks.getDataById(id);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable(name = "id") String id){
        return rbks.delete(id);
    }

    @GetMapping("/export")
    public Object export(@RequestParam("type") String type, HttpServletResponse response){
        rbks.export(response, type);
        Map<String, Object> map = new HashMap<>();
        map.put("path_url", "/realisasi_biaya_konsumsi/excel");
        return map;
    }

    @GetMapping("/excel")
    public void export(HttpServletResponse response){
        String fileName = "realisasi-biaya-konsumsi.xls";
        String path = "./report/Sprint Planning #1 - Stream 2.pdf";
        File file = new File(path);

        if(file.exists()){
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            String fileNameDownload = "Realiasi Biaya Konsumsi.pdf";

            try {
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileNameDownload + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                System.out.println(inputStream);
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            System.out.println("file not exists");
        }
    }
}
