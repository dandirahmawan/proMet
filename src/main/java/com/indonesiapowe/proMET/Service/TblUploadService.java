package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.TblUpload;
import com.indonesiapowe.proMET.Repository.TblUploadRepository;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TblUploadService {
    @Autowired
    TblUploadRepository tur;

    @Autowired
    StoreFile sf;

    public TblUpload getUploadById(String id){
        return tur.findById(id).get();
    }

    public Object deleteUpload(TblUpload tblUpload){
        System.out.println("delete data and file upload");
        String path = tblUpload.getPath();
        try{
            tur.delete(tblUpload); /*delete data*/
            sf.delete(path); /*delete file*/
        }catch (Exception e){
            /*data upload masih ada relasi dengan data lain*/
        }

        return null;
    }
}
