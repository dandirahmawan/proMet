package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.*;
import com.indonesiapowe.proMET.Model.ModelView.TblMasterRuanganView;
import com.indonesiapowe.proMET.Repository.*;
import com.indonesiapowe.proMET.StoreFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MasterService {

    @Autowired
    TblMasterUnitRepository unitRepo;

    @Autowired
    TblMasterRuanganRepository ruanganRepo;

    @Autowired
    TblDetailMasterRuanganRepository layoutRepository;

    @Autowired
    TblMasterFasilitasRepository fasilitasRepo;

    @Autowired
    TblMasterKonsumsiRepository konsumsiRepo;

    @Autowired
    TblMasterGedungRepository gedungRepo;

    @Autowired
    TblMasterRuanganViewRepository ruanganRepoView;

    @Autowired
    ViewTblMasterGedungRepository vtmgr;

    @Autowired
    ViewTblMasterUnitRepository vtmur;

    @Autowired
    TblMasterVendorRepository tmvr;

    @Autowired
    TblUploadRepository tur;

    @Autowired
    StoreFile sf;

    @Autowired
    TblDetailReservasiLayoutRepository tdrlr;

    @Autowired
    TblDetailReservasiFasilitasRepository tdrfr;

    @Autowired
    TblRealisasiBiayaKonsumsiRepository realisasiBiayaKonsumsiRepository;

    @Autowired
    ViewTblUsersService vtus;

    @Value("${role.id.superadmin}")
    String roleIdSuperadmin;

    @Value("${role.id.adminpromet}")
    String roleAdminPromet;

    @Value("${role.id.admingaf}")
    String roleAdminGaf;

    public Object getUnit(String id){
        if(id == null) {
            return vtmur.findAll();
        }else{
            return vtmur.findById(id);
        }
    }

    public Object getRuangan(String idUnit, String id, String kapasitas, String username) {
        if(username != null){
            ViewTblUsers vtu = vtus.getByEmail(username);
            String roleId = vtu.getRoleId();
            if(!roleId.equals(roleIdSuperadmin) && !roleId.equals(roleAdminPromet) && !roleId.equals(roleAdminGaf)){
                idUnit = (vtu.getUnitId() == null) ? "" : vtu.getUnitId();
            }
        }

        if ((idUnit == null || idUnit.equals("")) && id == null) {
            return ruanganRepoView.findAll();
        }else if(id != null) {
            return ruanganRepoView.findById(id);
        }else{
            List<TblMasterRuanganView> ruangans = ruanganRepoView.findByIdUnit(idUnit);;
            if(kapasitas != null){
                for(int i = 0;i<ruangans.size();i++){
                    Boolean validCapacity = false;
                    List<TblDetailMasterRuangan> layouts = ruangans.get(i).getLayouts();
                    for(TblDetailMasterRuangan item : layouts){
                        int kapasitasInt = Integer.parseInt(kapasitas);
                        int capacity = item.getKapasitas();
                        if(capacity >= kapasitasInt){
                            validCapacity = true;
                        }
                    }

                    /*remove data ruangan dengan kapasitas
                    kurang dari yang diinginkan*/
                    if(!validCapacity){
                        ruangans.remove(i);
                        i--; /*minus loop seq karena ketika remove data berkurang*/
                    }
                }
            }

            return ruangans;
        }
    }

    public Object getLayout(String idRuangan){
        if(idRuangan != null){
            return layoutRepository.findByIdRuangan(idRuangan);
        }else{
            return layoutRepository.findAll();
        }
    }

    public Object getFasilitas(String id, String username){
        if(id == null) {
            if(username != null){
                ViewTblUsers vtu = vtus.getByEmail(username);
                String roleId = vtu.getRoleId();
                String idUnit = vtu.getUnitId();
//                if(!roleId.equals(roleIdSuperadmin) && !roleAdminPromet.equals(roleId)){
                if(!roleId.equals(roleIdSuperadmin) && !roleId.equals(roleAdminPromet) && !roleId.equals(roleAdminGaf)){
                    return fasilitasRepo.findByIdUnit(idUnit);
                }else{
                    return fasilitasRepo.findAll();
                }
            }
            return fasilitasRepo.findAll();
        }else{
            return this.getFasilitasById(id);
        }
    }

    public Object getFasilitasById(String id){
        return fasilitasRepo.findById(id);
    }

    public Object getKonsumsi(String id){
        if(id == null) {
            return konsumsiRepo.findAll();
        }else{
            return konsumsiRepo.findById(id);
        }
    }

    public Object getGedung(String id, String idUnit, String username){
        if(username != null){
            ViewTblUsers vtu = vtus.getByEmail(username);
            String roleId = (vtu.getRoleId() == null) ? "" : vtu.getRoleId();
//            if(!roleId.equals(roleIdSuperadmin)){
//            if(!roleId.equals(roleIdSuperadmin) && !roleAdminPromet.equals(roleId)){
            if(!roleId.equals(roleIdSuperadmin) && !roleId.equals(roleAdminPromet) && !roleId.equals(roleAdminGaf)){
                idUnit = (vtu.getUnitId() == null) ? "" : vtu.getUnitId();
            }
        }

        if(id == null && idUnit == null) {
//            System.out.println("1");
            return vtmgr.findAll();
        }else if(id != null){
//            System.out.println("2");
            return vtmgr.findById(id);
        }else{
//            System.out.println("3");
            idUnit = (idUnit.equals("")) ? null : idUnit; /* uniqueidentifier cannot set "" */
            return vtmgr.findByIdUnit(idUnit);
        }
    }

    public Object postKonsumsi(TblMasterKonsumsi konsumsi){
        Map<String, Object> map = new HashMap<>();
        String id = konsumsi.getId();

        String nama = (konsumsi.getNama() == null) ? "" : konsumsi.getNama();
        if(nama.equals("")){
            map.put("code", 202);
            map.put("message", "Nama tidak boleh kosong !");
            return map;
        }

        if(id != null){
            Optional<TblMasterKonsumsi> kons = konsumsiRepo.findById(id);
            String idExst = kons.map(TblMasterKonsumsi::getId).orElse("");
            if(!idExst.equals("")){
                /*update data here*/
                konsumsiRepo.save(konsumsi);
                map.put("code", 200);
                map.put("message", "Update data konsumsi berhasil");
                return map;
            }else{
                /*data konsumsi not found*/
                map.put("code", 201);
                map.put("message", "Data konsumsi tidak ditemukan");
                return map;
            }
        }

        konsumsiRepo.save(konsumsi);
        map.put("code", 200);
        map.put("message", "Set data konsumsi berhasil");
        return map;
    }

    public Object postFasilitas(TblMasterFasilitas fasilitas){
        Map<String, Object> map = new HashMap<>();
        String id = fasilitas.getId();

        String nama = (fasilitas.getNama() == null) ? "" : fasilitas.getNama();
        String kode = (fasilitas.getKode() == null) ? "" : fasilitas.getKode();
        String status = (fasilitas.getStatus() == null) ? "" : fasilitas.getStatus();

        if(nama.equals("") || kode.equals("") || status.equals("")){
            map.put("code", 200);
            map.put("message", "Nama, kode dan status fasilitas tidak boleh kosong");
        }

        if(id != null){
            Optional<TblMasterFasilitas> fas = fasilitasRepo.findById(id);
            String idExst = fas.map(TblMasterFasilitas::getId).orElse("");
            if(!idExst.equals("")){
                /*update data here*/
                fasilitasRepo.save(fasilitas);
                map.put("code", 200);
                map.put("message", "Update data fasilitas berhasil");
                return map;
            }else{
                /*data konsumsi not found*/
                map.put("code", 201);
                map.put("message", "Data fasilitas tidak ditemukan");
                return map;
            }
        }

        fasilitasRepo.save(fasilitas);
        map.put("code", 200);
        map.put("message", "Set data fasilitas berhasil");
        return map;
    }

    public Object postRuangan(TblMasterRuangan ruangan){
        Map<String, Object> map = new HashMap<>();
        String id = ruangan.getId();

        String idUnit = (ruangan.getIdUnit() == null) ? "" : ruangan.getIdUnit();
        String idGedung = (ruangan.getIdGedung() == null) ? "" : ruangan.getIdGedung();;
        String status = (ruangan.getStatus() == null) ? "" : ruangan.getStatus();;

        if(idUnit.equals("") || idGedung.equals("") || status.equals("")){
            map.put("code", 202);
            map.put("message", "unit, gedung tidak boleh kosong!");
            return map;
        }

        if(id != null){
            Optional<TblMasterRuangan> fas = ruanganRepo.findById(id);
            String idExst = fas.map(TblMasterRuangan::getId).orElse("");

            if(!idExst.equals("")){
                /*update data here*/
                ruanganRepo.save(ruangan);

                /*delete unmapped data layout or data detail master ruangan*/
                List<TblDetailMasterRuangan> dataDelete = layoutRepository.findByIdRuanganIsNull();
                System.out.println(dataDelete.size());
                for(int i = 0;i<dataDelete.size();i++){
                    TblDetailMasterRuangan data = dataDelete.get(i);
                    try{
                        layoutRepository.delete(data);
                    }catch (Exception e){
                        /*kemungkinan data layout masih digunakan di data reservasi*/
                    }
                }

                map.put("code", 200);
                map.put("message", "Update data ruangan berhasil");
                return map;
            }else{
                /*data konsumsi not found*/
                map.put("code", 201);
                map.put("message", "Data ruangan tidak ditemukan");
                return map;
            }
        }

        ruanganRepo.save(ruangan);
        map.put("code", 200);
        map.put("message", "Set data ruangan berhasil");
        return map;
    }

    public Object postGedung(TblMasterGedung gedung){
        Map<String, Object> map = new HashMap<>();
        String id = gedung.getId();

        String kode = (gedung.getKodeGedung() == null) ? "" : gedung.getKodeGedung();
        String nama = (gedung.getNamaGedung() == null) ? "" : gedung.getNamaGedung();;
        String status = (gedung.getStatus() == null) ? "" : gedung.getStatus();
        String kodeKota = (gedung.getKodeKota() == null) ? "" : gedung.getKodeKota();
        String alamat = (gedung.getAlamat() == null) ? "" : gedung.getAlamat();

        if(kode.equals("") || nama.equals("") || status.equals("") || kodeKota.equals("") || alamat.equals("")){
            map.put("code", 202);
            map.put("message", "kode gedung, nama, status, kode kota dan alamat tidak boleh kosong!");
            return map;
        }

        if(id != null){
            Optional<TblMasterGedung> fas = gedungRepo.findById(id);
            String idExst = fas.map(TblMasterGedung::getId).orElse("");
            if(!idExst.equals("")){
                /*update data here*/
                gedungRepo.save(gedung);
                map.put("code", 200);
                map.put("message", "Update data gedung berhasil");
                return map;
            }else{
                /*data konsumsi not found*/
                map.put("code", 201);
                map.put("message", "Data gedung tidak ditemukan");
                return map;
            }
        }

        gedungRepo.save(gedung);
        map.put("code", 200);
        map.put("message", "Set data ruangan berhasil");
        return map;
    }

    public Object postUnit(TblMasterUnit unit){
        Map<String, Object> map = new HashMap<>();
        String id = unit.getId();

        String kode = (unit.getKodeUnit() == null) ? "" : unit.getKodeUnit();
        String nama = (unit.getNamaUnit() == null) ? "" : unit.getNamaUnit();;
        String status = (unit.getStatus() == null) ? "" : unit.getStatus();
        String kodeKota = (unit.getKodeKota() == null) ? "" : unit.getKodeKota();
        String provinsi = (unit.getKodeProvinsi() == null) ? "" : unit.getKodeProvinsi();

        if(kode.equals("") || nama.equals("") || status.equals("") || kodeKota.equals("") || provinsi.equals("")){
            map.put("code", 202);
            map.put("message", "kode unit, nama, status, kode kota, provinsi dan alamat tidak boleh kosong!");
            return map;
        }

        if(id != null){
            Optional<TblMasterUnit> fas = unitRepo.findById(id);
            String idExst = fas.map(TblMasterUnit::getId).orElse("");
            if(!idExst.equals("")){
                /*update data here*/
                unitRepo.save(unit);
                map.put("code", 200);
                map.put("message", "Update unit ruangan berhasil");
                return map;
            }else{
                /*data konsumsi not found*/
                map.put("code", 201);
                map.put("message", "Data unit tidak ditemukan");
                return map;
            }
        }

        unitRepo.save(unit);
        map.put("code", 200);
        map.put("message", "Set data unit berhasil");
        return map;
    }

    public Object postLayout(TblDetailMasterRuangan layout){
        Map<String, Object> map = new HashMap<>();
        String id = layout.getId();

        String nama = (layout.getLayout() == null) ? "" : layout.getLayout();
        String ruangan = (layout.getIdRuangan() == null) ? "" : layout.getIdRuangan();
        Integer kapasitas = (layout.getKapasitas() == null) ? 0 : layout.getKapasitas();

        if(nama.equals("") || ruangan.equals("") || kapasitas == 0){
            map.put("code", 200);
            map.put("message", "Nama, ruangan dan kapasitas tidak boleh kosong");
            return map;
        }

        if(id != null){
            Optional<TblDetailMasterRuangan> fas = layoutRepository.findById(id);
            String idExst = fas.map(TblDetailMasterRuangan::getId).orElse("");
            if(!idExst.equals("")){
                /*update data here*/
                layoutRepository.save(layout);
                map.put("code", 200);
                map.put("message", "Update layout ruangan berhasil");
                return map;
            }else{
                /*data konsumsi not found*/
                map.put("code", 201);
                map.put("message", "Data layout tidak ditemukan");
                return map;
            }
        }

        layoutRepository.save(layout);
        map.put("code", 200);
        map.put("message", "Set data layout berhasil");
        return map;
    }

    public Object deleteGedung(TblMasterGedung g){
        Optional<TblMasterGedung> gedung = gedungRepo.findById(g.getId());
        if(gedung.get() != null){
            List<TblMasterRuangan> ruanagans = ruanganRepo.findByIdGedung(g.getId());
            if(ruanagans.size() > 0){
                Map<String, Object> map = new HashMap<>();
                map.put("code", 201);
                map.put("message", "Gedung masih terikat dengan data ruangan.");
                return map;
            }else{
                gedungRepo.delete(gedung.get());
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "Delete gedung berhasil.");
        return map;
    }

    public Object deleteUnit(TblMasterUnit unt){
        Optional<TblMasterUnit> unit = unitRepo.findById(unt.getId());
        if(unit.get() != null){
            unitRepo.delete(unit.get());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "Delete unit berhasil.");
        return map;
    }

    public Object deleteRuangan(TblMasterRuangan r){
        Optional<TblMasterRuangan> ruangan = ruanganRepo.findById(r.getId());
        if(ruangan.get() != null){
            List<TblDetailReservasiLayout> reservasiLayouts = tdrlr.findByIdRuangan(r.getId());
            if(reservasiLayouts.size() > 0){
                Map<String, Object> map = new HashMap<>();
                map.put("code", 201);
                map.put("message", "Data ruangan terikat dengan data reservasi");
                return map;
            }
            ruanganRepo.delete(ruangan.get());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "Delete ruangan berhasil.");
        return map;
    }

    public Object deleteKonsumsi(TblMasterKonsumsi k){
        Optional<TblMasterKonsumsi> konsumsi = konsumsiRepo.findById(k.getId());
        if(konsumsi.get() != null){
            konsumsiRepo.delete(konsumsi.get());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "Delete konsumsi berhasil.");
        return map;
    }

    public Object deleteFasilitas(TblMasterFasilitas f){
        Optional<TblMasterFasilitas> fasilitas = fasilitasRepo.findById(f.getId());
        if(fasilitas.get() != null){
            List<TblDetailReservasiFasilitas> reservasiFasilitas = tdrfr.findByIdFasilitas(f.getId());
            if(reservasiFasilitas.size() > 0){
                Map<String, Object> map = new HashMap<>();
                map.put("code", 201);
                map.put("message", "Data fasilitas masih terikat dengan data reservasi.");
                return map;
            }
            fasilitasRepo.delete(fasilitas.get());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "Delete fasilitas berhasil.");
        return map;
    }

    public Object deleteLayout(TblDetailMasterRuangan l){
        Optional<TblDetailMasterRuangan> layout = layoutRepository.findById(l.getId());
        if(layout.get() != null){
            layoutRepository.delete(layout.get());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "Delete layout berhasil.");
        return map;
    }

    public Object getAllVendor(){
        return tmvr.findAll();
    }

    public Object getVendorById(String id){
        return tmvr.findById(id);
    }

    public Object postVendor(TblMasterVendor vendor){
        Map<String, Object> map = new HashMap<>();
        String id = vendor.getId();
        if(id != null){
            Optional<TblMasterVendor> vend = tmvr.findById(id);
            String idExst = vend.map(TblMasterVendor::getId).orElse("");
            if(!idExst.equals("")){
                /*update data here*/
                tmvr.save(vendor);
                map.put("code", 200);
                map.put("message", "Update data vendor berhasil");
                return map;
            }else{
                /*data konsumsi not found*/
                map.put("code", 201);
                map.put("message", "Data vendor tidak ditemukan");
                return map;
            }
        }
        tmvr.save(vendor);
        map.put("code", 200);
        map.put("message", "Set data vendor berhasil");
        return map;
    }

    public Object deleteVendor(TblMasterVendor v){
        Optional<TblMasterVendor> vendor = tmvr.findById(v.getId());
        if(vendor.get() != null){
            List<TblRealisasiBiayaKonsumsi> data = realisasiBiayaKonsumsiRepository.findBySnackPagiOrSnackSiangOrSnackSore(v.getId(), v.getId(), v.getId());
            if(data.size() > 0){
                Map<String, Object> map = new HashMap<>();
                map.put("code", 201);
                map.put("message", "Data vendor terikat dengan data reservasi");
                return map;
            }

            tmvr.delete(vendor.get());
            String urlImage = vendor.get().getUrlImage();
            Optional<TblUpload> upl = tur.findById(urlImage);
            if(upl.get() != null){
                String path = upl.get().getPath();
                sf.delete(path);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", "Delete vendor berhasil.");
        return map;
    }
}
