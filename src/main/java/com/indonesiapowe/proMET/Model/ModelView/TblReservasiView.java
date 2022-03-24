package com.indonesiapowe.proMET.Model.ModelView;

import com.indonesiapowe.proMET.Model.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_reservasi")
@Getter
/*model untuk keperluan select data saja*/
public class TblReservasiView {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(name = "nama_rapat")
    String namaRapat;

    String deskripsi;

    @Column(name = "jenis_kegiatan")
    String jenisKegiatan;

    @Column(name = "id_unit", insertable = false, updatable = false)
    String idUnit;

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

    @OneToMany(targetEntity = TblDetailReservasiLayoutView.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reservasi", referencedColumnName = "id")
    List<TblDetailReservasiLayoutView> layouts;

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

    @Column(name = "created_by", insertable = false, updatable = false)
    String createdBy;

    @OneToOne(targetEntity = ViewTblUsers.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "created_by", referencedColumnName = "email")
    ViewTblUsers createdByData;

    @Column(name = "created_date")
    Date createdDate;

    @Column(name = "modify_by")
    String modifyBy;

    @Column(name = "modify_date")
    Date modifyDate;
}
