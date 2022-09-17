package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.ModelView.ViewTblReservasiOnline;
import com.indonesiapowe.proMET.Model.TblHadirReservasiOnline;
import com.indonesiapowe.proMET.Model.TblReservasiOnline;
import com.indonesiapowe.proMET.Model.TblReservasiOnlineDetail;
import com.indonesiapowe.proMET.Repository.TblHadirReservasiOnlineRepository;
import com.indonesiapowe.proMET.Repository.TblReservasiOnlineDetailRepository;
import com.indonesiapowe.proMET.Repository.TblReservasiOnlineRepository;
import com.indonesiapowe.proMET.Repository.ViewTblReservasiOnlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    public Object post(HttpServletRequest request, TblReservasiOnline tblReservasiOnline){
        repo.save(tblReservasiOnline);
        String nid = tblReservasiOnline.getId();
        Map<String, Object> map = new HashMap<>();

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
