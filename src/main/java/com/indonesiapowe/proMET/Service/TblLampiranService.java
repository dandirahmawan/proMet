package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.TblLampiranReservasi;
import com.indonesiapowe.proMET.Repository.TblLampiranReservasiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TblLampiranService {

    @Autowired
    TblLampiranReservasiRepository tlrr;

    public TblLampiranReservasi getLampiranById(String id){
        Optional<TblLampiranReservasi> lampiran = tlrr.findById(id);
        return lampiran.get();
    }
}
