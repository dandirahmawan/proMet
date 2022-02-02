package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_reservasi")
@Getter
@Setter
public class TblReservasi {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(name = "nama_rapat")
    String namaRapat;

    String deskripsi;

    @Column(name = "jenis_kegiatan")
    String jenisKegiatan;

    @OneToOne(targetEntity = TblMasterUnit.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_unit", referencedColumnName = "id")
    TblMasterUnit unit;

    @Column(name = "bidang")
    String bidang;

    @Column(name = "pic_rapat")
    String picRapat;

    @Column(name = "jumlah_internal")
    int jumlahInternal;

    @Column(name = "jumlah_external")
    int jumlahExternal;

    @Column(name = "keterangan_peserta")
    String keteranganPeserta;

    @Column(name = "jadwal_mulai")
    Date jadwalMulai;

    @Column(name = "jadwal_selesai")
    Date jadwalSelesai;

    @OneToMany(targetEntity = TblDetailReservasiLayout.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reservasi", referencedColumnName = "id")
    List<TblDetailReservasiLayout> layouts;

    @Column(name = "keterangan_vicon")
    String keteranganVicon;

    @OneToMany(targetEntity = TblDetailReservasiFasilitas.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reservasi", referencedColumnName = "id")
    List<TblDetailReservasiFasilitas> fasilitas;

    @OneToMany(targetEntity = TblDetailReservasiKonsumsi.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reservasi", referencedColumnName = "id")
    List<TblDetailReservasiKonsumsi> konsumsi;

    @OneToMany(targetEntity = TblLampiranReservasi.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reservasi", referencedColumnName = "id")
    List<TblLampiranReservasi> lampiran;

    @Column(name = "status")
    String status;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "created_date")
    Date createdDate;

    @Column(name = "modify_by")
    String modifyBy;

    @Column(name = "modify_date")
    Date modifyDate;
}
