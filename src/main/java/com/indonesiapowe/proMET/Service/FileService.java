package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.TblLampiranReservasi;
import com.indonesiapowe.proMET.Model.TblUpload;
import com.indonesiapowe.proMET.Repository.TblLampiranReservasiRepository;
import com.indonesiapowe.proMET.Repository.TblUploadRepository;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    StoreFile sf;

    @Autowired
    TblLampiranReservasiRepository lampiran;

    @Autowired
    TblUploadRepository uploadRepo;

    public Object upload(MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        map = sf.upload(file);
        map.put("success", true);
        String fileName = map.get("fileName").toString();
        String fileNameUpload = map.get("fileNameUpload").toString();
        String path = map.get("path").toString();

        Date now = new Date();
        TblUpload data = new TblUpload();
        data.setFileName(fileName);
        data.setPath(path);
        data.setUploadDate(now);
        uploadRepo.save(data);
        return data;
    }

    public void download(HttpServletResponse response, String fileId){
        String path = "";
        String file = "";

        Optional<TblUpload> upload = uploadRepo.findById(fileId);
        String id = upload.map(TblUpload::getId).orElse("");
        if(!id.equals("")){
            path = upload.get().getPath();
            file = upload.get().getFileName();
        }

        sf.download(response, path, file);
    }
}
