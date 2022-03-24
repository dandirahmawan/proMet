package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblRealisasiBiayaKonsumsi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblRealisasiBiayaKonsumsiRepository extends JpaRepository<TblRealisasiBiayaKonsumsi, String> {
    List<TblRealisasiBiayaKonsumsi> findBySnackPagiOrSnackSiangOrSnackSore(String p1, String p2, String p3);
}
