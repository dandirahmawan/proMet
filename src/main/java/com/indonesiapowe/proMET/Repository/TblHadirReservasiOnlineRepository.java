package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblHadirReservasiOnline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblHadirReservasiOnlineRepository extends JpaRepository<TblHadirReservasiOnline, String> {
    List<TblHadirReservasiOnline> findByReservasiId(String reservasiId);
}
