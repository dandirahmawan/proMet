package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.TblDetailReservasiFasilitas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblDetailReservasiFasilitasRepository extends JpaRepository<TblDetailReservasiFasilitas, String> {
    List<TblDetailReservasiFasilitas> findByIdReservasiAndIdReservasiIsNotNull(String idReservasi);
}
