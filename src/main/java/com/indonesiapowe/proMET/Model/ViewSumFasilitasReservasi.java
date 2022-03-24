package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "view_sum_fasilitas_reservasi")
@Getter
@Setter
public class ViewSumFasilitasReservasi {
    @Id
    @Column(name = "id_fasilitas")
    String idFasiltas;

    @Column(name = "jumlah_terpakai")
    Integer jumlahTerpakai;

    @Column(name = "jumlah_master")
    Integer jumlahMaster;

    @Column(name = "jumlah_tersedia")
    Integer jumlahTersedia;

    @Column(name = "nama")
    String nama;

    @Column(name = "id_unit")
    String idUnit;

    @Column(name = "status_fasilitas")
    String statusFasilitas;
}
