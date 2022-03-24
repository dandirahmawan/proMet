package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblDetailReservasiLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TblDetailReservasiLayoutRepository extends JpaRepository<TblDetailReservasiLayout, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM TblDetailReservasiLayout data where data.idReservasi IS NULL")
    int deleteByIdReservasiIsNull();

    Optional<TblDetailReservasiLayout> findByIdReservasiAndIdRuangan(String idReservasi, String idRuangan);
    List<TblDetailReservasiLayout> findByIdRuangan(String idRuangan);
}
