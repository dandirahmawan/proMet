package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ModelView.TblReservasiView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblReservasiViewRepository extends JpaRepository<TblReservasiView, String> {
    List<TblReservasiView> findByCreatedByOrderByCreatedDateDesc(String email);
    List<TblReservasiView> findByStatus(String status);
    List<TblReservasiView> findByIdUnit(String idUnit);
    List<TblReservasiView> findAllByOrderByCreatedDateDesc();
}
