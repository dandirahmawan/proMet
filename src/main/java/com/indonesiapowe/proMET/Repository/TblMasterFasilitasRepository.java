package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblMasterFasilitas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblMasterFasilitasRepository extends JpaRepository<TblMasterFasilitas, String> {
    List<TblMasterFasilitas> findByIdIn(List<String> ids);
    List<TblMasterFasilitas> findByIdUnit(String idUnit);
}
