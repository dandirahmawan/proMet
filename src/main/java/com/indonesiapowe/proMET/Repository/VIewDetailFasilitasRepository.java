package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.Integrasi.SummaryFasilitas;
import com.indonesiapowe.proMET.Model.ViewDetailFasilitas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface VIewDetailFasilitasRepository extends JpaRepository<ViewDetailFasilitas, String> {
    @Query("SELECT new com.indonesiapowe.proMET.Model.Integrasi.SummaryFasilitas( " +
            "data.idFasilitas, " +
            "sum(data.jumlah), " +
            "(SELECT data2.jumlah FROM TblMasterFasilitas data2 WHERE data2.id = data.idFasilitas), " +
            "data.nama " +
            ") " +
            "from ViewDetailFasilitas data " +
            "where data.jadwalMulai >= ?1 and data.jadwalSelesai <= ?2 AND data.idFasilitas in ?3 AND (data.status <> 'REJECT' OR data.status IS NULL)" +
            "group by data.idFasilitas, data.nama")
    List<SummaryFasilitas> findSummaryFasilitas(Date startDate, Date finishDate, List<String> idFasilitas);
}
