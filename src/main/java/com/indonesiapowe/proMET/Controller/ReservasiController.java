package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Model.TblReservasi;
import com.indonesiapowe.proMET.Repository.TblDetailReservasiLayoutRepository;
import com.indonesiapowe.proMET.Service.ReservasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservasi")
public class ReservasiController {

    @Autowired
    ReservasiService rs;

    @Autowired
    TblDetailReservasiLayoutRepository tdrlr;

    @GetMapping()
    public Object getAll(@RequestParam(value = "username", required = false) String queryParameter){
        if(queryParameter != null && !queryParameter.equals("")) {
            return rs.getByEmail(queryParameter);
        }else{
            return rs.getAll();
        }
    }

    @GetMapping("/persetujuan")
    public Object getPersetujuan(@RequestParam(value = "username", required = false) String queryParameter){
        if(queryParameter != null && !queryParameter.equals("")) {
            return rs.getPersetujuan(queryParameter);
        }else{
            return rs.getAll();
        }
    }

    @PostMapping()
    public Object post(@RequestBody TblReservasi tblReservasi){
        return rs.post(tblReservasi, false);
    }

    @DeleteMapping()
    public Object delete(@RequestBody TblReservasi reservasi){
        return rs.deleteReservasi(reservasi);
    }

    @PostMapping("/approve")
    public Object approve(@RequestBody TblReservasi reservasi){
        return rs.post(reservasi, true);
    }

    @PostMapping("/reject")
    public Object reject(@RequestBody TblReservasi reservasi){
        return rs.reject(reservasi);
    }

    @GetMapping("/{id}")
    public Object getLayout(@PathVariable String id){
        return rs.getById(id);
    }
}
