package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewSumFasilitasReservasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewSumFasilitasReservasiRepository extends JpaRepository<ViewSumFasilitasReservasi, String> {
    @Query(value = "SELECT\n" +
            "\ta.id id_fasilitas,\n" +
            "\tCASE WHEN b.jumlah_terpakai IS NULL THEN 0 ELSE b.jumlah_terpakai END jumlah_terpakai,\n" +
            "\ta.jumlah jumlah_master,\n" +
            "\tCASE WHEN b.jumlah_tersedia IS NULL THEN a.jumlah ELSE b.jumlah_tersedia END jumlah_tersedia,\n" +
            "\ta.nama,\n" +
            "\ta.id_unit,\n" +
            "\ta.status status_fasilitas \n" +
            "FROM\n" +
            "\ttbl_master_fasilitas a\n" +
            "\tLEFT JOIN ( SELECT * FROM view_sum_fasilitas_reservasi\n" +
            "\tWHERE jadwal_mulai >= ?1 AND\n" +
            "\tjadwal_selesai <= ?2 ) b ON a.id = b.id_fasilitas \n" +
            "WHERE\n" +
            "\ta.id_unit = ?3 and (a.status = 'active' or a.status is null)", nativeQuery = true)
    List<ViewSumFasilitasReservasi> findChoicesData(String startDate, String finishDate, String idUnit);

//    @Query(value = "SELECT\n" +
//            "\ta.id id_fasilitas,\n" +
//            "\tCASE WHEN b.jumlah_terpakai IS NULL THEN 0 ELSE b.jumlah_terpakai END jumlah_terpakai,\n" +
//            "\ta.jumlah jumlah_master,\n" +
//            "\tCASE WHEN b.jumlah_tersedia IS NULL THEN a.jumlah ELSE b.jumlah_tersedia END jumlah_tersedia,\n" +
//            "\ta.nama,\n" +
//            "\ta.id_unit\n" +
//            "FROM\n" +
//            "\ttbl_master_fasilitas a\n" +
//            "\tLEFT JOIN ( SELECT * FROM view_sum_fasilitas_reservasi\n" +
//            "\tWHERE jadwal_mulai >= ?1 AND\n" +
//            "\tjadwal_selesai <= ?2 ) b ON a.id = b.id_fasilitas \n" +
//            "WHERE\n" +
//            "\ta.id_unit = ?3", nativeQuery = true)
//    List<ViewSumFasilitasReservasi> findChoicesDataEdit(String startDate, String finishDate, String idUnit);
}
