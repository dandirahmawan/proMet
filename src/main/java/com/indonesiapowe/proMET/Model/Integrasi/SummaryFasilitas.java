package com.indonesiapowe.proMET.Model.Integrasi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryFasilitas {
    String idFasilitas;
    Long jumlah;
    Integer jumlahMaster;
    String name;

    public SummaryFasilitas(String idFasilitas, Long jumlah, Integer jumlahMaster, String name){
        this.idFasilitas = idFasilitas;
        this.jumlah = jumlah;
        this.jumlahMaster = jumlahMaster;
        this.name = name;
    }
}
