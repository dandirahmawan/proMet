package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.*;
import com.indonesiapowe.proMET.Model.Integrasi.SummaryFasilitas;
import com.indonesiapowe.proMET.Repository.*;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservasiService {
    @Autowired
    TblReservasiRepository trr;

    @Autowired
    TblDetailMasterRuanganRepository tdmrr;

    @Autowired
    TblDetailReservasiLayoutRepository layoutRepo;

    @Autowired
    TblLampiranReservasiRepository lampiranRepo;

    @Autowired
    TblLampiranService tls;

    @Autowired
    TblUploadService tus;

    @Autowired
    ViewTblUploadRepository vtur;

    @Autowired
    ViewTblUploadService vus;

    @Autowired
    TblMasterFasilitasRepository tmfr;

    @Autowired
    VIewDetailFasilitasRepository vdfr;

    @Autowired
    StoreFile sf;

    @Autowired
    TblUploadRepository tur;

    @Autowired
    TblDetailReservasiFasilitasRepository tdrfr;

    public Object getByEmail(String email){
        return trr.findByCreatedByOrderByCreatedDateDesc(email);
    }

    public Object post(TblReservasi tblReservasi){
        /*init variabel untuk delete data file lama ketika edit reservasi*/
        String path = "";
        String idFileExisting = "";
        String idFileInput = "";

        Map<String, Object> map = new HashMap<>();
        Boolean isLampiranInput = true;
        /*set lampiran null ketika user tidak melampirkan file reservasi*/
        try{
            int l = tblReservasi.getLampiran().size();
            if(l > 0){
                String idFile = tblReservasi.getLampiran().get(0).getFile().getId();
                if(idFile == null) {
                    tblReservasi.setLampiran(null);
                    isLampiranInput = false;
                }
            }
        }catch (Exception e){
            /*file lampiran tidak tersedia*/
            tblReservasi.setLampiran(null);
            isLampiranInput = false;
        }

//        System.out.println("is lampiran : "+isLampiranInput);

        Boolean isLayoutValid = this.validationLayoutRuangan(tblReservasi);
        if(isLayoutValid){
            String id = tblReservasi.getId();
            if(id != null){
                Optional<TblReservasi> reservasi = trr.findById(id);
                String idExists = reservasi.map(TblReservasi::getId).orElse("");
                if(idExists.equals("")){
                    map.put("code", -1);
                    map.put("message", "Data reservasi tidak ditemukan");
                    return map;
                }

                /*data lampiran lama*/
                List<TblLampiranReservasi> lampiran = reservasi.get().getLampiran();
                for(TblLampiranReservasi item : lampiran){
                    TblUpload dataUpload = item.getFile();
                    path = dataUpload.getPath();
                    idFileExisting = dataUpload.getId();
                    idFileInput = (isLampiranInput) ? tblReservasi.getLampiran().get(0).getFile().getId() : "";
                }
            }else{
                /*set created date*/
                Date now = new Date();
                tblReservasi.setCreatedDate(now);
            }

            Map<String, Object> rtn = this.validasiFasilitas(tblReservasi);
            Boolean isFasValid = (Boolean) rtn.get("isValid");
            if(!isFasValid){
                Object msg = rtn.get("message");
                map.put("code", -1);
                map.put("message", msg);
                return map;
            }

            trr.save(tblReservasi);
            layoutRepo.deleteByIdReservasiIsNull(); /*ini untuk keperluan edit data*/
            lampiranRepo.deleteByIdReservasiIsNull(); /*ini untuk keperluan edit data*/

            /*delete data file lama, ketika user merubah data lampiran pada reservasi*/
            if(!idFileExisting.equals(idFileInput) && id != null){
                /*delete file upload lampiran lama*/
                System.out.println("delete data lampiran lama");
                tur.deleteById(idFileExisting);
                sf.delete(path);
            }

//            List<ViewTblUpload> listUpload = vtur.findByIdLampiranIsNullAndGedungUploadIsNullAndFileVendorIsNull();
//            for(int i = 0;i<listUpload.size();i++){
//                System.out.println(listUpload.get(i).getId());
//            }
//            vus.deleteSomeLampiran(listUpload); /*delete unmapped data upload*/

            map.put("code", 1);
            map.put("message", "Reservasi ruangan berhasil");
        }else{
            List<String> msg = new ArrayList<>();
            map.put("code", -1);
            map.put("message", msg);
        }
        return map;
    }

    public Map<String, Object> validasiFasilitas(TblReservasi tblReservasi){
        /*validasi data fasilitas*/
        Date start = tblReservasi.getJadwalMulai();
        Date finish = tblReservasi.getJadwalSelesai();

        /*set parameter list fasilitas*/
        List<String> fasilitasParam = new ArrayList<>();
        for(TblDetailReservasiFasilitas itemFasilitas : tblReservasi.getFasilitas()){
            String id = itemFasilitas.getId();
//            if(id == null){
                String idFasilitas = itemFasilitas.getDataFasilitas().getId();
                fasilitasParam.add(idFasilitas);
//            }
        }

        /*param ketersediaan data fasilitas*/
        String id = tblReservasi.getId();
        List<SummaryFasilitas> summaryFasilitas = vdfr.findSummaryFasilitas(start, finish, fasilitasParam);
        List<String> msgFasilitas = new ArrayList<>();
        Boolean isFasValid = true;

        /*fasilitas yang sudah di set sebelumnya*/
        List<TblDetailReservasiFasilitas> reservasiFasilitas = new ArrayList<>();
        if(id != null){
            reservasiFasilitas = tdrfr.findByIdReservasiAndIdReservasiIsNotNull(id);
        }

        for(SummaryFasilitas item : summaryFasilitas){
            Integer terpakai = Integer.parseInt(item.getJumlah().toString());
            Integer jumlahMaster = item.getJumlahMaster();
            Integer ready = jumlahMaster - terpakai;

//            System.out.println(item.getName());
//            System.out.println(item.getJumlahMaster());
//            System.out.println(item.getJumlah());
//            System.out.println(ready);

            for(int x = 0;x<reservasiFasilitas.size();x++){
                TblMasterFasilitas fasExists = reservasiFasilitas.get(x).getDataFasilitas();
                String idExists = fasExists.getId();
                String nameFasExists = fasExists.getNama();
                Integer jumlahFasExists = reservasiFasilitas.get(x).getJumlah();
                /*reset jumlah fasilitas terpakai dengan mengurangi
                jumlah fasilitas dari data yang lama atau akan di edit*/
                if(idExists.equals(item.getIdFasilitas())){
                    terpakai = terpakai - jumlahFasExists;
                    ready = jumlahMaster - terpakai;
//                    System.out.println("ready : "+ready);
//                    System.out.println("id : "+idExists);
//                    System.out.println("id input : "+item.getIdFasilitas());
//                    System.out.println("nama : "+nameFasExists);
//                    System.out.println("jumlah : "+jumlahFasExists);
//                    System.out.println("----------------");
                }
            }

            /*hitung data sisa master fasilitas
            jika sisa -1 atau lebih berarti input data fasilitas gagal*/
            Integer remains = 0;
            for(TblDetailReservasiFasilitas itemReservFasilitas : tblReservasi.getFasilitas()){
                String idFasInput = itemReservFasilitas.getDataFasilitas().getId();
                String idFas = item.getIdFasilitas();
                if(idFasInput.equals(idFas)){
                    System.out.println("fasilitas : "+item.getName());
                    System.out.println("raady : "+ready);
                    System.out.println("reserved : "+itemReservFasilitas.getJumlah());
                    System.out.println("");
                    remains = ready - itemReservFasilitas.getJumlah();
                }
            }

            if(remains < 0){
                msgFasilitas.add("Fasilitas "+item.getName()+" tidak tersedia.");
                isFasValid = false;
            }
//            System.out.println("=====================================");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("isValid", isFasValid);
        map.put("message", msgFasilitas);
        return map;
    }

    public Object approve(TblReservasi reservasi){
        String id = reservasi.getId();
        System.out.println("reservasis id : "+id);
        Optional<TblReservasi> reserv = trr.findById(id);
        String idExs = reserv.map(TblReservasi::getId).orElse("");
        Map<String, Object> map = new HashMap<>();

        if(idExs.equals("")){
            map.put("code", 201);
            map.put("message", "Data reservasi tidak ditemukan");
        }else {
            Date now = new Date();
            reserv.get().setModifyDate(now);
            reserv.get().setStatus("APPR");
            trr.save(reserv.get());
            map.put("code", 201);
            map.put("message", "Approve reservasi berhasil");
        }

        return map;
    }

    public Object reject(TblReservasi reservasi){
        String id = reservasi.getId();
        Optional<TblReservasi> reserv = trr.findById(id);
        String idExs = reserv.map(TblReservasi::getId).orElse("");
        Map<String, Object> map = new HashMap<>();

        if(idExs.equals("")){
            map.put("code", 201);
            map.put("message", "Data reservasi tidak ditemukan");
        }else {
            Date now = new Date();
            reserv.get().setModifyDate(now);
            reserv.get().setStatus("REJECT");
            trr.save(reserv.get());
            map.put("code", 201);
            map.put("message", "Reject reservasi berhasil");
        }

        return map;
    }

    public Object deleteReservasi(TblReservasi reservasi){
        Optional<TblReservasi> reserv = trr.findById(reservasi.getId());
        String id = reserv.map(TblReservasi::getId).orElse("");
        Map<String, Object> map = new HashMap<>();

        if(id.equals("")){
            map.put("code", 201);
            map.put("message", "Data reservasi tidak ditemukan");
        }else{
            /*get data upload*/
            trr.delete(reserv.get());

            /*delete data lampiran*/
            List<TblLampiranReservasi> lampiran = reserv.get().getLampiran();
            for(int i = 0;i<lampiran.size();i++) {
                TblUpload upload = lampiran.get(i).getFile();
                tus.deleteUpload(upload); /*delete data and file upload*/
            }

            map.put("code", 200);
            map.put("message", "Hapus data reservasi berhasil");
        }

        return map;
    }

    public Object getById(String id){
        Optional<TblReservasi> data = trr.findById(id);
        String idExists = data.map(TblReservasi::getId).orElse("");

        Map<String, Object> map = new HashMap<>();
        if(!idExists.equals("")){
            map.put("message", "fetch data successfully");
            map.put("data", data.get());
            return map;
        }else {
            map.put("message", "fetch data successfully");
            map.put("data", null);
            return map;
        }
    }

    public Boolean validationLayoutRuangan(TblReservasi tblReservasi){
        List<TblDetailReservasiLayout> layout = tblReservasi.getLayouts();
        System.out.println("size of layout : "+layout.size());

        for(TblDetailReservasiLayout item : layout){
            String idLayout = item.getLayout().getId();
            String idRuangan = item.getRuangan().getId();

            Optional<TblDetailMasterRuangan> ruanganLayout = tdmrr.findById(idLayout);
            String idRuanganLyt = ruanganLayout.map(TblDetailMasterRuangan::getIdRuangan).orElse("");
            if(!idRuanganLyt.equals(idRuangan)){
                return false;
            }
        }
        return true;
    }
}
