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
    public Object getAll(@RequestParam("username") String queryParameter){
        return rs.getByEmail(queryParameter);
    }

    @PostMapping()
    public Object post(@RequestBody TblReservasi tblReservasi){
        return rs.post(tblReservasi);
    }

    @DeleteMapping()
    public Object delete(@RequestBody TblReservasi reservasi){
        return rs.deleteReservasi(reservasi);
    }

    @PostMapping("/approve")
    public Object approve(@RequestBody TblReservasi reservasi){
        return rs.approve(reservasi);
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
