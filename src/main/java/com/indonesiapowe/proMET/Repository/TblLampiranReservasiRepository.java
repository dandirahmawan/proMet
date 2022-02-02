package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblLampiranReservasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TblLampiranReservasiRepository extends JpaRepository<TblLampiranReservasi, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM TblLampiranReservasi data where data.idReservasi IS NULL")
    int deleteByIdReservasiIsNull();
}
