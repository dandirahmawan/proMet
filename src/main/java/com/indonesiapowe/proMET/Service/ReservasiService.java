package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.*;
import com.indonesiapowe.proMET.Model.Integrasi.RoleUser;
import com.indonesiapowe.proMET.Model.Integrasi.SummaryFasilitas;
import com.indonesiapowe.proMET.Model.ModelView.TblReservasiView;
import com.indonesiapowe.proMET.Repository.*;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservasiService {
    @Autowired
    TblReservasiRepository trr;

    @Autowired
    TblReservasiViewRepository trvr;

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

    @Autowired
    ViewTblUsersService vtus;

    @Autowired
    TblMasterUnitRepository tmur;

    String roleIdAdminPromet = "6DEB5FAC-7D49-4A75-A774-2433412A6DBF";

    @Value("${role.id.superadmin}")
    String roleSuperAdmin;

    @Value("${role.id.adminfasilitas}")
    String roleAdminFasilitas;

    @Value("${role.id.admingaf}")
    String roleAdminGaf;

    public Object getByEmail(String email){
        if(email != null){
            return trvr.findByCreatedByOrderByCreatedDateDesc(email);
        }
        return trvr.findAllByOrderByCreatedDateDesc();
    }

    public Object getPersetujuan(String email){
        if(email != null){
            ViewTblUsers vtu = vtus.getByEmail(email);
            String roleId = vtu.getRoleId();
            roleId = (roleId == null) ? "" : roleId;

            String unitId = vtu.getUnitId();
            String site = vtu.getSitegroup();
            if(site.equals("IP")){
                Optional<TblMasterUnit> masterUnit = tmur.findByNamaUnitIgnoreCase("kantor pusat");
                unitId = masterUnit.map(TblMasterUnit::getId).orElse("");
            }

            if(roleId.equals(roleAdminFasilitas)){
                return trvr.findByIdUnit(unitId);
            }else if(roleId.equals(roleSuperAdmin) || roleId.equals(roleIdAdminPromet) || roleId.equals(roleAdminGaf)){
                return trvr.findAllByOrderByCreatedDateDesc();
            }else{
                return trvr.findByCreatedByOrderByCreatedDateDesc(email);
            }
        }
        return trvr.findByCreatedByOrderByCreatedDateDesc(email);
    }

    public Object getAll(){
        return trvr.findAll();
    }

    public Object getApprovedData(){
        return trvr.findByStatus("APPR");
    }

    public Object post(TblReservasi tblReservasi, Boolean isApparove){
        /*init variabel untuk delete data file lama ketika edit reservasi*/
        String path = "";
        String idFileExisting = "";
        String idFileInput = "";
        String id = tblReservasi.getId();

        ViewTblUsers vtu = vtus.getByEmail(tblReservasi.getCreatedBy());
        String roleId = vtu.getRoleId();

        Integer jumlahExternal = tblReservasi.getJumlahExternal();
        jumlahExternal = (jumlahExternal == null) ? 0 : jumlahExternal;

        /*set null data konsumsi jika jumlah external 0*/
        if(jumlahExternal == 0){
            tblReservasi.setKonsumsi(null);
        }

        Map<String, Object> map = new HashMap<>();
        Boolean isLampiranInput = true;

        Date tglAcara = tblReservasi.getJadwalMulai();
        Date tglSelesai = tblReservasi.getJadwalSelesai();
        Date now = new Date();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime nt = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
        LocalDateTime d1 = LocalDateTime.ofInstant(tglAcara.toInstant(), ZoneId.systemDefault());
        LocalDateTime d2 = LocalDateTime.ofInstant(tglSelesai.toInstant(), ZoneId.systemDefault());

        String startDateRapat = dtf.format(d1);
        String finishDateRapat = dtf.format(d2);

        LocalTime maxTime = LocalTime.parse("21:00:00"); /*set waktu selesai maksimal*/
        LocalTime minTime = LocalTime.parse("07:00:00"); /*set waktu mulai maksimal*/
        LocalTime finishTime = LocalTime.parse(finishDateRapat);
        LocalTime startTime = LocalTime.parse(startDateRapat);

        /*validasi jam mulai dan jam selesai*/
        Long itvFinish = finishTime.until(maxTime, ChronoUnit.SECONDS);
        Long itvStart = minTime.until(startTime, ChronoUnit.SECONDS);

        /*keperluan validasi 30 menit sebelum pemesanan konsumsi*/
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dayDate = formatter.format(tglAcara); /*tgl acara*/
        String dayNow = formatter.format(now); /*tgl hari ini*/

        /*validasi yang berlaku untuk flow bukan approval*/
        if(!isApparove){
            if(itvFinish.intValue() < 0 || itvStart.intValue() < 0){
                map.put("code", -1);
                map.put("message", "Pemesanan hanya dapat dimulai pukul 07:00 - 21:00");
                return map;
            }

            /*get interval or gap day*/
            Long interval = tglAcara.getTime() - now.getTime();
            Long gapDay = interval / (60 * 60 * 24 * 1000);

            /*jika edit maka tidak perlu validasi ini*/
            if(id == null){
                if(!roleId.equals(roleIdAdminPromet) && !roleId.equals(roleSuperAdmin) && !roleId.equals(roleAdminGaf)){
                    if(gapDay < 1){
                        map.put("code", -1);
                        map.put("message", "Pemesanan dapat dilakukan mulai 1 hari sebelum waktu rapat");
                        return map;
                    }
                }
            }

//          System.out.println("gap day : "+gapDay);
            /*validasi maksimum pemesanan ruangan 3 hari sebelum meet*/
            if(gapDay > 3){
                map.put("code", -1);
                map.put("message", "Pemesanan dapat dilakukan maksimal 3 hari sebelum rapat");
                return map;
            }

            /*validasi pemesanan makanan minimal 30 menit sebelum rapat*/
            Long itvMakanan = d1.until(nt, ChronoUnit.SECONDS);
            Long mm = new Long(30 * 60);
            int sizeKonsumsi = (tblReservasi.getKonsumsi() != null) ? tblReservasi.getKonsumsi().size() : 0;

            /*jika pesanan hari ini validasi 30 menit sebelum rapat untuk konsumsi*/
            if(gapDay == 0 && dayDate == dayNow){
                if(itvMakanan.intValue() < mm.intValue() && sizeKonsumsi > 0){
                    map.put("code", -1);
                    map.put("message", "Pemesanan konsumsi minimal 30 menit sebelum rapat");
                    return map;
                }
            }
        }


        /*validasi durasi pemesanan ruangan maksimal 3 hari*/
        Long intervalReservasi = tglSelesai.getTime() - tglAcara.getTime();
        Long gapDayReservasi = intervalReservasi / (60 * 60 * 24 * 1000);
        if(gapDayReservasi > 2){
            map.put("code", -1);
            map.put("message", "Maksimal pemesanan rapat 3 hari");
            return map;
        }

        if(gapDayReservasi < 0){
            map.put("code", -1);
            map.put("message", "Tanggal akhir rapat kurang dari tanggal mulai");
            return map;
        }

        /*validasi fasilitas*/
        Map<String, Object> rtn = this.validasiFasilitas(tblReservasi);
        Boolean isFasValid = (Boolean) rtn.get("isValid");
        if(!isFasValid){
            Object msg = rtn.get("message");
            map.put("code", -1);
            map.put("message", msg);
            return map;
        }

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

        Boolean isLayoutValid = this.validationLayoutRuangan(tblReservasi);
        if(isLayoutValid){
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
                tblReservasi.setCreatedDate(now);
            }

            /*set APPROVE status*/
            if(isApparove){
                tblReservasi.setStatus("APPR");
            }

            trr.save(tblReservasi);
            layoutRepo.deleteByIdReservasiIsNull(); /*ini untuk keperluan edit data*/
            lampiranRepo.deleteByIdReservasiIsNull(); /*ini untuk keperluan edit data*/

            /*delete data file lama, ketika user merubah data lampiran pada reservasi*/
            if(!idFileExisting.equals(idFileInput) && id != null){
                /*delete file upload lampiran lama*/
                tur.deleteById(idFileExisting);
                sf.delete(path);
            }

            if(!isApparove){
                map.put("code", 1);
                map.put("message", "Reservasi ruangan berhasil");
            }else {
                map.put("code", 1);
                map.put("message", "Approve reservasi berhasil");
            }
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

        Map<String, Object> map = new HashMap<>();
        Boolean isFasValid = true;

        /*set parameter list fasilitas*/
        List<String> fasilitasParam = new ArrayList<>();
        for(TblDetailReservasiFasilitas itemFasilitas : tblReservasi.getFasilitas()){
            /*validasi jumlah fasilitas tidak boleh kosong*/
            Integer jumlahReservasi = itemFasilitas.getJumlah();
            if(jumlahReservasi == null || jumlahReservasi < 1){
                map.put("isValid", false);
                map.put("message", "Jumlah Fasilitas tidak boleh kosong");
                return map;
            }

            String idFasilitas = itemFasilitas.getDataFasilitas().getId();
            fasilitasParam.add(idFasilitas);
        }

        /*param ketersediaan data fasilitas*/
        String id = tblReservasi.getId();
        List<SummaryFasilitas> summaryFasilitas = vdfr.findSummaryFasilitas(start, finish, fasilitasParam);
        List<String> msgFasilitas = new ArrayList<>();

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
                    remains = ready - itemReservFasilitas.getJumlah();
                }
            }

            if(remains < 0){
                msgFasilitas.add("Fasilitas "+item.getName()+" tidak tersedia.");
                isFasValid = false;
            }
        }

        map.put("isValid", isFasValid);
        map.put("message", msgFasilitas);
        return map;
    }

//    public Object approve(TblReservasi reservasi){
//        String id = reservasi.getId();
//        Optional<TblReservasi> reserv = trr.findById(id);
//        String idExs = reserv.map(TblReservasi::getId).orElse("");
//        Map<String, Object> map = new HashMap<>();
//
//        if(idExs.equals("")){
//            map.put("code", 201);
//            map.put("message", "Data reservasi tidak ditemukan");
//        }else {
//
//            /*validasi fasilitas*/
//            Map<String, Object> rtn = this.validasiFasilitas(reservasi);
//            Boolean isFasValid = (Boolean) rtn.get("isValid");
//            if(!isFasValid){
//                Object msg = rtn.get("message");
//                map.put("code", 201);
//                map.put("message", msg);
//                return map;
//            }
//
//            /*validasi durasi pemesanan ruangan maksimal 3 hari*/
//            Date tglAcara = reservasi.getJadwalMulai();
//            Date tglSelesai = reservasi.getJadwalSelesai();
//            Long intervalReservasi = tglSelesai.getTime() - tglAcara.getTime();
//            Long gapDayReservasi = intervalReservasi / (60 * 60 * 24 * 1000);
//
//            if(gapDayReservasi > 2){
//                map.put("code", 201);
//                map.put("message", "Maksimal pemesanan rapat 3 hari");
//                return map;
//            }
//
//            if(gapDayReservasi < 0){
//                map.put("code", 201);
//                map.put("message", "Tanggal akhir rapat kurang dari tanggal mulai");
//                return map;
//            }
//
//            /*layout validation*/
//            Boolean isLayoutValid = this.validationLayoutRuangan(reservasi);
//            if(isLayoutValid){
//
//                /*data lampiran lama*/
//                List<TblLampiranReservasi> lampiran = reservasi.get().getLampiran();
//                for(TblLampiranReservasi item : lampiran){
//                    TblUpload dataUpload = item.getFile();
//                    path = dataUpload.getPath();
//                    idFileExisting = dataUpload.getId();
//                    idFileInput = (isLampiranInput) ? tblReservasi.getLampiran().get(0).getFile().getId() : "";
//                }
//
//                /*update data reservasi*/
//                Date now = new Date();
//                reservasi.setModifyDate(now);
//                reservasi.setStatus("APPR");
//                trr.save(reservasi);
//                map.put("code", 200);
//                map.put("message", "Approve reservasi berhasil");
//            }else{
//                map.put("code", 201);
//                map.put("message", "Layout reservasi tidak valid");
//            }
//        }
//
//        return map;
//    }

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
        Optional<TblReservasiView> data = trvr.findById(id);
        String idExists = data.map(TblReservasiView::getId).orElse("");

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
//        System.out.println("size of layout : "+layout.size());

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
