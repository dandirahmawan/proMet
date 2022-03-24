package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblDetailMasterRuangan;
import com.indonesiapowe.proMET.Model.TblMasterRuangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblDetailMasterRuanganRepository extends JpaRepository<TblDetailMasterRuangan, String> {
    List<TblDetailMasterRuangan> findByIdRuangan(String idRuangan);
    List<TblDetailMasterRuangan> findByIdRuanganIsNull();

    @Query(value = "SELECT * FROM tbl_detail_master_ruangan\n" +
            "where id_ruangan = ?1 and id not in(\n" +
            "SELECT id from \n" +
            "view_ruangan_layout_reservasi \n" +
            "where jadwal_mulai >= ?2 and jadwal_selesai <= ?3)", nativeQuery = true)
    List<TblDetailMasterRuangan> findChoicesLayout(String idRuangan, String startDate, String finishDate);

    @Query(value = "SELECT * FROM tbl_detail_master_ruangan\n" +
            "where id_ruangan = ?1 and id not in(\n" +
            "SELECT id from \n" +
            "view_ruangan_layout_reservasi \n" +
            "where jadwal_mulai >= ?2 and jadwal_selesai <= ?3 and id != ?4)", nativeQuery = true)
    List<TblDetailMasterRuangan> findChoicesLayoutEdit(String idRuangan, String startDate, String finishDate, String idLayout);

}
