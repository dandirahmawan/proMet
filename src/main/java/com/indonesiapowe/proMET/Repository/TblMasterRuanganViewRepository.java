package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ModelView.TblMasterRuanganView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblMasterRuanganViewRepository extends JpaRepository<TblMasterRuanganView, String> {
    List<TblMasterRuanganView> findByIdUnit(String idUnit);

    @Query(value = "SELECT DISTINCT a.* FROM tbl_master_ruangan a\n" +
//            "JOIN tbl_detail_master_ruangan b ON a.id = b.id_ruangan\n" +
            "where id_unit = ?1 AND a.id not in(\n" +
            "SELECT id_ruangan from \n" +
            "view_ruangan_layout_reservasi \n" +
            "where jadwal_mulai <= ?2 and jadwal_selesai >= ?3) and (lower(a.status) = 'active' or a.status is null)", nativeQuery = true)
    List<TblMasterRuanganView> findChoicesRuangan(String idUnit, String startDate, String finishDate);

    @Query(value = "SELECT DISTINCT a.* FROM tbl_master_ruangan a\n" +
//            "JOIN tbl_detail_master_ruangan b ON a.id = b.id_ruangan\n" +
            "where id_unit = ?1 AND a.id not in(\n" +
            "SELECT id_ruangan from \n" +
            "view_ruangan_layout_reservasi \n" +
            "where jadwal_mulai <= ?2 and jadwal_selesai >= ?3 and id_reservasi != ?4) and (lower(a.status) = 'active' or a.status is null)" +
            "UNION\n" +
            "SELECT DISTINCT a.* FROM tbl_master_ruangan a where id = (\n" +
            "SELECT id_ruangan from\n" +
            "view_ruangan_layout_reservasi\n" +
            "where jadwal_mulai <= ?2 and jadwal_selesai >= ?3 and id_reservasi = ?4)", nativeQuery = true)
    List<TblMasterRuanganView> findChoicesRuanganReservasi(String idUnit, String startDate, String finishDate, String idReservasi);
}
