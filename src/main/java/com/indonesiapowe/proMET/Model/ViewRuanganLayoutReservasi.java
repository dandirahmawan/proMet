package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "view_ruangan_layout_reservasi")
@Getter
@Setter
public class ViewRuanganLayoutReservasi {
    @Id
    double num;

    String id;

    String layout;

    @Column(name = "id_ruangan")
    String idRuangan;

    @Column(name = "nama_ruangan")
    String namaRuangan;

    @Column(name = "id_reservasi")
    String idReservasi;

    @Column(name = "jadwal_mulai")
    Date jadwalMulai;

    @Column(name = "jadwal_selesai")
    Date jadwalSelesai;
}
