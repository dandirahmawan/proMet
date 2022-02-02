package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Service.ChoicesServices;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/choices")
public class ChoicesController {

    @Autowired
    ChoicesServices cs;

    @PostMapping("/layout")
    public Object listLayout(@RequestBody String body, @RequestParam(name = "id_reservasi", required = false) String idReservasi){
        JSONObject jo = new JSONObject(body);
        try{
            String ruanganId = jo.getString("idRuangan");
            String startDate = jo.getString("startDate");
            String finishDate = jo.getString("finishDate");
            String kapasitas = jo.getString("kapasitas");
            return cs.getChoicesLayout(ruanganId, startDate, finishDate, kapasitas, idReservasi);
        }catch (Exception e){
            e.printStackTrace();
            /*paramter tidak valid*/
            Map<String, Object> map = new HashMap<>();
            map.put("code", -1);
            map.put("message", "Parameter layout tidak valid");
            return map;
        }
    }

    @PostMapping("/ruangan")
    public Object listRuangan(@RequestBody String body, @RequestParam(name = "id_reservasi", required = false) String idReservasi){
        JSONObject jo = new JSONObject(body);
        try{
            String idUnit = jo.getString("idUnit");
            String startDate = jo.getString("startDate");
            String finishDate = jo.getString("finishDate");
            String kapasitas = jo.getString("kapasitas");
            return cs.getChoicesRuangan(idUnit, startDate, finishDate, kapasitas, idReservasi);
        }catch (Exception e){
            e.printStackTrace();
            Map<String, Object> map = new HashMap<>();
            map.put("code", -1);
            map.put("message", "Parameter tidak valid");
            return map;
        }
    }

    @PostMapping("/fasilitas")
    public Object listFasilitas(@RequestBody String body, @RequestParam(name = "id_reservasi", required = false) String idReservasi){
        JSONObject jo = new JSONObject(body);
        try{
            String idUnit = jo.getString("idUnit");
            String startDate = jo.getString("startDate");
            String finishDate = jo.getString("finishDate");
            return cs.getChoicesFasilitas(idUnit, startDate, finishDate, idReservasi);
        }catch (Exception e){
            e.printStackTrace();
            Map<String, Object> map = new HashMap<>();
            map.put("code", -1);
            map.put("message", "Parameter tidak valid");
            return map;
        }
    }
}
