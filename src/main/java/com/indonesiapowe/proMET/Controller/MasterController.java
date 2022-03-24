package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Model.*;
import com.indonesiapowe.proMET.Service.MasterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/master")
public class MasterController {

    @Autowired
    MasterService ms;

    @GetMapping("/ruangan")
    public Object getRuangan(
            @RequestParam(required = false, name = "username") String username,
            @RequestParam(required = false, name = "id_unit") String idUnit,
            @RequestParam(required = false, name = "id") String id,
            @RequestParam(required = false, name = "kapasitas") String kapasitas
    ){
        return ms.getRuangan(idUnit, id, kapasitas, username);
    }

    @GetMapping("/konsumsi")
    public Object getKonsumsi(@RequestParam(required = false, name = "id") String id){
        return ms.getKonsumsi(id);
    }

    @GetMapping("/fasilitas")
    public Object getFasilitas(@RequestParam(required = false, name = "id") String id, @RequestParam(name = "username", required = false) String username){
        return ms.getFasilitas(id, username);
    }

    @GetMapping("/layout")
    public Object Layout(@RequestParam(required = false, name = "id") String id){
        return ms.getLayout(id);
    }

    @GetMapping("/unit")
    public Object posttUnit(@RequestParam(required = false, name = "id") String id){
        return ms.getUnit(id);
    }

    @GetMapping("/gedung")
    public Object getGedung(
            @RequestParam(required = false, name = "id") String id,
            @RequestParam(required = false, name = "username") String username,
            @RequestParam(required = false, name = "id_unit") String idUnit){
        return ms.getGedung(id, idUnit, username);
    }

    @GetMapping("/vendor")
    public Object getVendor(@RequestParam(required = false, name = "id") String id){
        if(id == null){
            return ms.getAllVendor();
        }else{
            return ms.getVendorById(id);
        }
    }

    @PostMapping("/ruangan")
    public Object postRuangan(@RequestBody TblMasterRuangan ruangan){
        return ms.postRuangan(ruangan);
    }

    @PostMapping("/konsumsi")
    public Object postKonsumsi(@RequestBody TblMasterKonsumsi konsumsi){
        return ms.postKonsumsi(konsumsi);
    }

    @PostMapping("/fasilitas")
    public Object postFasilitas(@RequestBody TblMasterFasilitas fasilitas){
        return ms.postFasilitas(fasilitas);
    }

    @PostMapping("/fasilitas/list")
    public Object getListFasilitas(@RequestBody String body){
        JSONObject obj = new JSONObject(body);
        Map<String, Object> map = new HashMap<>();
        map.put("param", obj.getString("idUnit"));
        return map;
    }

    @PostMapping("/layout")
    public Object postLayout(@RequestBody TblDetailMasterRuangan layout){
        return ms.postLayout(layout);
    }

    @PostMapping("/unit")
    public Object postUnit(@RequestBody TblMasterUnit unit){
        return ms.postUnit(unit);
    }

    @PostMapping("/gedung")
    public Object postGedung(@RequestBody TblMasterGedung gedung){
        return ms.postGedung(gedung);
    }

    @DeleteMapping("/gedung")
    public Object deletGedung(@RequestBody TblMasterGedung gedung){
        return ms.deleteGedung(gedung);
    }

    @DeleteMapping("/ruangan")
    public Object deleteRuangan(@RequestBody TblMasterRuangan ruangan){
        return ms.deleteRuangan(ruangan);
    }

    @DeleteMapping("/konsumsi")
    public Object deleteKonsumsi(@RequestBody TblMasterKonsumsi konsumsi){
        return ms.deleteKonsumsi(konsumsi);
    }

    @DeleteMapping("/fasilitas")
    public Object deleteFasilitas(@RequestBody TblMasterFasilitas fasilitas){
        return ms.deleteFasilitas(fasilitas);
    }

    @DeleteMapping("/layout")
    public Object deleteLayout(@RequestBody TblDetailMasterRuangan layout){
        return ms.deleteLayout(layout);
    }

    @DeleteMapping("/unit")
    public Object deleteUnit(@RequestBody TblMasterUnit unit){
        return ms.deleteUnit(unit);
    }

    @PostMapping("/vendor")
    public Object postVendor(@RequestBody TblMasterVendor vendor){
        return ms.postVendor(vendor);
    }

    @DeleteMapping("/vendor")
    public Object delete(@RequestBody TblMasterVendor vendor){
        return ms.deleteVendor(vendor);
    }
}