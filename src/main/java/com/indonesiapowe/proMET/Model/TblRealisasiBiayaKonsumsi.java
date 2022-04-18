package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tbl_realisasi_biaya_konsumsi")
@Getter
@Setter
public class TblRealisasiBiayaKonsumsi {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "reservasi_id")
    String reservasiId;

    @Column(name = "jumlah_konsumsi_disetujui")
    Integer jumlahKonsumsiDisetujui;

    @Column(name = "snack_pagi")
    String snackPagi;

    @Column(name = "snack_siang")
    String snackSiang;

    @Column(name = "snack_sore")
    String snackSore;

    @Column(name = "rupiah_konsumsi_pagi")
    BigDecimal rupiahKonsumsiPagi;

    @Column(name = "rupiah_konsumsi_siang")
    BigDecimal rupiahKonsumsiSiang;

    @Column(name = "rupiah_konsumsi_sore")
    BigDecimal rupiahKonsumsiSore;

    @Column(name = "biaya_tambahan")
    BigDecimal biayaTambahan;

    @Column(name = "created_date")
    Date createdDate;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "last_modified_date")
    Date lastModifiedDate;

    @Column(name = "last_modified_by")
    String lastModifiedBy;

    @Column(name = "tanggal_permohonan")
    Date tanggalPermohonan;

    @Column(name = "tanggal_acara")
    Date tanggalAcara;

    @Column(name = "peserta_internal")
    Integer pesertaInternal;

    @Column(name = "peserta_external")
    Integer pesertaExternal;

    @Column(name = "keterangan_peserta_external")
    String keteranganPesertaExternal;

    String keterangan;
}
