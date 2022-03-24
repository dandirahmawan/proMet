package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewTblRealisasiBiayaKonsumsi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewTblRealisasiBiayaKonsumsiRepository extends JpaRepository<ViewTblRealisasiBiayaKonsumsi, String> {
    List<ViewTblRealisasiBiayaKonsumsi> findAllByOrderByCreatedDateDesc();
    List<ViewTblRealisasiBiayaKonsumsi> findByIdUnitOrderByCreatedDateDesc(String idUnit);
}
