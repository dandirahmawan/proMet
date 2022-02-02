package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblMasterRuangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblMasterRuanganRepository extends JpaRepository<TblMasterRuangan, String> {
    @Query(value = "SELECT DISTINCT a.* FROM tbl_master_ruangan a\n" +
            "JOIN tbl_detail_master_ruangan b ON a.id = b.id_ruangan\n" +
            "where id_unit = ?1 AND b.id not in(\n" +
            "SELECT id from \n" +
            "view_ruangan_layout_reservasi \n" +
            "where jadwal_mulai >= ?2 and jadwal_selesai <= ?3)", nativeQuery = true)
    List<TblMasterRuangan> findChoicesRuangan(String idUnit, String startDate, String finishDate);
}
