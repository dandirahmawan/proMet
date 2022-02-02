package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.ViewTblUpload;
import com.indonesiapowe.proMET.Repository.TblUploadRepository;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewTblUploadService {

    @Autowired
    StoreFile sf;

    @Autowired
    TblUploadRepository tur;

    public void deleteSomeLampiran(List<ViewTblUpload> viewTblUploadList){
        for(int i = 0;i<viewTblUploadList.size();i++){
            String path = viewTblUploadList.get(i).getPath();
            String id = viewTblUploadList.get(i).getId();
            sf.delete(path);
            try{
                tur.deleteById(id);
            }catch (Exception e){
                /*data upload masih ada relasi dengan data lain*/
            }
        }
    }

}
