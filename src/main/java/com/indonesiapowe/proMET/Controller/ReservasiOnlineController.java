package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Model.TblHadirReservasiOnline;
import com.indonesiapowe.proMET.Model.TblReservasiOnline;
import com.indonesiapowe.proMET.Service.PdfServices;
import com.indonesiapowe.proMET.Service.TblReservasiOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/reservasi_online")
public class ReservasiOnlineController {

    @Autowired
    TblReservasiOnlineService reservOnlineServ;

    @Autowired
    PdfServices ps;

    @PostMapping()
    public Object post(HttpServletRequest request, @RequestBody TblReservasiOnline body){
        return  reservOnlineServ.post(request, body);
    }

    @GetMapping()
    public Object getAll(HttpServletRequest request){
        return reservOnlineServ.getAll(request);
    }

    @GetMapping("/{id}")
    public Object getById(HttpServletRequest request, @PathVariable String id){
        return reservOnlineServ.getById(request, id);
    }

    @PostMapping("/{id}/attendance")
    public Object PostAttendance(HttpServletRequest request, @PathVariable String id, @RequestBody TblHadirReservasiOnline body){
        return reservOnlineServ.postAttendance(request, id, body);
    }

    @GetMapping("/{id}/report")
    public void getReport(HttpServletRequest request, HttpServletResponse response, @PathVariable String id){
        ps.exportPdf(request, id, response);
    }
}
