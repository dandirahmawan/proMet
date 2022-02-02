package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblMasterUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblMasterUnitRepository extends JpaRepository<TblMasterUnit, String> {
    Optional<TblMasterUnit> findByNamaUnitIgnoreCase(String unit);
}
