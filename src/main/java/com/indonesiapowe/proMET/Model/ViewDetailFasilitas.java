package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "view_detail_fasilitas")
@Getter
@Setter
public class ViewDetailFasilitas {
    @Id
    String id;

    @Column(name = "id_reservasi")
    String idReservasi;

    @Column(name = "nama")
    String nama;

    @Column(name = "id_fasilitas")
    String idFasilitas;

    @Column(name = "jumlah")
    Integer jumlah;

    @Column(name = "keterangan")
    String keterangan;

    @Column(name = "jadwal_mulai")
    Date jadwalMulai;

    @Column(name = "jadwal_selesai")
    Date jadwalSelesai;

    @Column(name = "status")
    String status;
}
