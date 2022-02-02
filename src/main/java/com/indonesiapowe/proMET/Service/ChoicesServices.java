package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.*;
import com.indonesiapowe.proMET.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChoicesServices {

    @Autowired
    TblMasterRuanganViewRepository tmrr;

    @Autowired
    TblDetailMasterRuanganRepository tdmrr;

    @Autowired
    TblDetailReservasiLayoutRepository tdrlr;

    @Autowired
    ViewSumFasilitasReservasiRepository fasRepo;

    @Autowired
    TblDetailReservasiFasilitasRepository tdrfr;

    public Object getChoicesLayout(String idRuangan, String startDate, String finishDate, String kapasitas, String idReservasi){
        Integer kapasitasInt = Integer.parseInt(kapasitas);
        List<TblDetailMasterRuangan> layouts = new ArrayList<>();

//        System.out.println("id ruangan : "+idRuangan);
//        System.out.println("start : "+startDate);
//        System.out.println("finish : "+finishDate);
//        Date start = new Date(startDate);

        if(idReservasi == null){
            layouts = tdmrr.findChoicesLayout(idRuangan, startDate, finishDate);
        }else{
            System.out.println("id reservasi is : "+idReservasi);
            Boolean isMatch = false;
            Optional<TblDetailReservasiLayout> layoutReserv = tdrlr.findByIdReservasiAndIdRuangan(idReservasi, idRuangan);
            String idLayoutReserv = layoutReserv.map(TblDetailReservasiLayout::getId).orElse("");

            /*init data id layout*/
            String idLayoutData = "";
            if(!idLayoutReserv.equals("")){
                String idRuanganReserved = layoutReserv.get().getRuangan().getId();
                idLayoutData = layoutReserv.get().getLayout().getId();
                if(idRuanganReserved.equals(idRuangan)) isMatch = true;
            }

            if(isMatch) {
                System.out.println("id layout is "+idLayoutData);
                layouts = tdmrr.findChoicesLayoutEdit(idRuangan, startDate, finishDate, idLayoutData);
            }else{
                layouts = tdmrr.findChoicesLayout(idRuangan, startDate, finishDate);
            }
        }

        for(int i = 0;i<layouts.size();i++){
            int capacity = layouts.get(i).getKapasitas();
            if(capacity < kapasitasInt){
                layouts.remove(i);
                i--;
            }
        }

        return layouts;
    }

    public Object getChoicesRuangan(String idUnit, String startDate, String finishDate, String kapasitas, String idReservasi){
        List<TblMasterRuanganView> ruangans = new ArrayList<>();
        Integer capacityInit = (kapasitas.equals("")) ? 0 : Integer.parseInt(kapasitas);

        if(idReservasi == null){
            ruangans = tmrr.findChoicesRuangan(idUnit, startDate, finishDate);
        }else{
            ruangans = tmrr.findChoicesRuanganReservasi(idUnit, startDate, finishDate, idReservasi);
        }
        for(int i = 0;i<ruangans.size();i++){
            Boolean validCapacity = false;
            List<TblDetailMasterRuangan> layouts = ruangans.get(i).getLayouts();
            for(TblDetailMasterRuangan item : layouts){
                int capacity = item.getKapasitas();
                if(capacity >= capacityInit){
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

        return ruangans;
    }

    public Object getChoicesFasilitas(String idUnit, String startDate, String finishDate, String idReservasi){
        List<ViewSumFasilitasReservasi> fasData = new ArrayList<>();
        fasData = fasRepo.findChoicesData(startDate, finishDate, idUnit);

        if(idReservasi != null){
            /*fetch dara fasilitas yang sudah di set sebelumnya*/
            List<TblDetailReservasiFasilitas> fasReserved = tdrfr.findByIdReservasiAndIdReservasiIsNotNull(idReservasi);
            for(int i = 0; i<fasData.size();i++){
                for(TblDetailReservasiFasilitas itemReserved : fasReserved){
                    String idFasReserved = itemReserved.getDataFasilitas().getId();
                    String idFas = fasData.get(i).getIdFasiltas();

                    if(idFasReserved.equals(idFas)){
                        Integer jumlahMaster = fasData.get(i).getJumlahMaster();
                        Integer jumlahReserved = itemReserved.getJumlah();
                        Integer jumlahTersedia = fasData.get(i).getJumlahTersedia();

                        /*jumlah reserved diakumulasi dengan data tersedia,
                        karena ketika edi data reserve di hitung sebagai jumlah tersedia*/
                        Integer count = jumlahReserved + jumlahTersedia;

                        /*data terpakai hasil dari pengurangan data tersedia
                        dari akumulasi diatas*/
                        Integer count2 = jumlahMaster - count;

                        fasData.get(i).setJumlahTersedia(count);
                        fasData.get(i).setJumlahTerpakai(count2);
                    }
                }
            }
        }

        /*remove item dengan jumlah tersedia 0*/
        for(int i = 0; i<fasData.size();i++){
            ViewSumFasilitasReservasi item = fasData.get(i);
            Integer jumlahTersedia = item.getJumlahTersedia();

            /*remove data dengan ketersediaan 0*/
            if(jumlahTersedia <= 0){
                fasData.remove(i);
                i--;
            }
        }

        return fasData;
    }
}
