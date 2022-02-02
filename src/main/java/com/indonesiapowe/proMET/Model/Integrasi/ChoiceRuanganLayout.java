package com.indonesiapowe.proMET.Model.Integrasi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChoiceRuanganLayout {
    String id;
    String nama;

    public ChoiceRuanganLayout(String id, String nama){
        this.id = id;
        this.nama = nama;
    }
}
