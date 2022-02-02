package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewTblMasterGedung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewTblMasterGedungRepository extends JpaRepository<ViewTblMasterGedung, String> {
    List<ViewTblMasterGedung> findByIdUnit(String idUnit);
}
