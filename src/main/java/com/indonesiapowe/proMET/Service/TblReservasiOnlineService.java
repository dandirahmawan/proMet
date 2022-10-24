package com.indonesiapowe.proMET.Service;

import com.google.zxing.EncodeHintType;
import com.indonesiapowe.proMET.Model.ModelView.ViewTblReservasiOnline;
import com.indonesiapowe.proMET.Model.TblHadirReservasiOnline;
import com.indonesiapowe.proMET.Model.TblReservasiOnline;
import com.indonesiapowe.proMET.Model.TblReservasiOnlineDetail;
import com.indonesiapowe.proMET.Repository.TblHadirReservasiOnlineRepository;
import com.indonesiapowe.proMET.Repository.TblReservasiOnlineDetailRepository;
import com.indonesiapowe.proMET.Repository.TblReservasiOnlineRepository;
import com.indonesiapowe.proMET.Repository.ViewTblReservasiOnlineRepository;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TblReservasiOnlineService {

    @Autowired
    TblReservasiOnlineRepository repo;

    @Autowired
    TblReservasiOnlineDetailRepository repoDetail;

    @Autowired
    ViewTblReservasiOnlineRepository repoList;

    @Autowired
    TblHadirReservasiOnlineRepository attendanceRepo;

    @Autowired
    QrCodeServices qs;

    public Object post(HttpServletRequest request, TblReservasiOnline tblReservasiOnline){
        repo.save(tblReservasiOnline);
        String nid = tblReservasiOnline.getId();
        Map<String, Object> map = new HashMap<>();

        /*generate qr code*/
        Optional<ViewTblReservasiOnline> dataView = repoList.findById(nid);
        String ids = dataView.map(ViewTblReservasiOnline::getId).orElse("");
        if(!ids.equals("")) {
            String data = dataView.get().getGeneratedLink();
            this.generateQR(data, nid);
        }

        if(nid != null){
            map.put("code", 1);
            map.put("message", "Reservasi rapat online berhasil");
            map.put("data", this.getById(request, nid));
        }else{
            map.put("code", -1);
            map.put("message", "Reservasi rapat online gagal");
            map.put("data", null);
        }
        return map;
    }

    public void autoGenerateQr(){
        List<ViewTblReservasiOnline> data = repoList.findAll();
        for(int i = 0;i<data.size();i++){
            String link = data.get(i).getGeneratedLink();
            String id = data.get(i).getId();
            this.generateQR(link, id);
        }
    }

    public void generateQR(String data, String idReservasi) {
        try {
            //path where we want to get QR Code
            File dir = new File("../QR_IMAGE");
            if(!dir.exists()) dir.mkdir();

            String path = "../QR_IMAGE/qr_reservasi_"+idReservasi+".png";
            //Encoding charset to be used
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            //generates QR code with Low level(L) error correction capability
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //invoking the user-defined method that creates the QR code
            qs.generateQRcode(data, path, charset, hashMap, 500, 500);//increase or decrease height and width accodingly
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object getAll(HttpServletRequest request){
        List<ViewTblReservasiOnline> data = repoList.findAll();
        return  data;
    }

    public Object getById(HttpServletRequest request, String id){
        Optional<TblReservasiOnlineDetail> dataOpt = repoDetail.findById(id);
        return dataOpt;
    }

    public Object postAttendance(HttpServletRequest request, String reservasiId, TblHadirReservasiOnline body){
        body.setReservasiId(reservasiId);
        attendanceRepo.save(body);

        String nid = body.getId();
        Map<String, Object> map = new HashMap<>();
        if(nid != null){
            map.put("code", 1);
            map.put("message", "Data attendance berhasil disimpan");
            map.put("data", this.getById(request, nid));
        }else{
            map.put("code", -1);
            map.put("message", "Data attendance gagal disimpan");
            map.put("data", this.getById(request, nid));
        }
        return map;
    }
}
