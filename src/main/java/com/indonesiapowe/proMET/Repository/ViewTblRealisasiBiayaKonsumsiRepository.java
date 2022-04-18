package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewTblRealisasiBiayaKonsumsi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ViewTblRealisasiBiayaKonsumsiRepository extends JpaRepository<ViewTblRealisasiBiayaKonsumsi, String> {
    List<ViewTblRealisasiBiayaKonsumsi> findAllByOrderByCreatedDateDesc();
    List<ViewTblRealisasiBiayaKonsumsi> findByIdUnitOrderByCreatedDateDesc(String idUnit);
    List<ViewTblRealisasiBiayaKonsumsi> findByCreatedByOrderByCreatedDateDesc(String email);

    List<ViewTblRealisasiBiayaKonsumsi> findAllByTanggalAcaraBetweenOrderByCreatedDateDesc(Date sd, Date ed);
    List<ViewTblRealisasiBiayaKonsumsi> findByIdUnitAndTanggalAcaraBetweenOrderByCreatedDateDesc(String idUnit, Date sd, Date ed);
    List<ViewTblRealisasiBiayaKonsumsi> findByCreatedByAndTanggalAcaraBetweenOrderByCreatedDateDesc(String email, Date sd, Date ed);
}
