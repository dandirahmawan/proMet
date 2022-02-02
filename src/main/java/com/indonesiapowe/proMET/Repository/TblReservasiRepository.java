package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblReservasi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TblReservasiRepository extends JpaRepository<TblReservasi, String> {
    List<TblReservasi> findByCreatedByOrderByCreatedDateDesc(String email);
    List<TblReservasi> findByJadwalMulaiGreaterThanEqualAndJadwalSelesaiIsLessThanEqual(Date start, Date finish);
}
