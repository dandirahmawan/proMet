package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblMasterGedung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblMasterGedungRepository extends JpaRepository<TblMasterGedung, String> {
    List<TblMasterGedung> findByIdUnit(String idUnit);
}
