package com.indonesiapowe.proMET.Model;

import com.indonesiapowe.proMET.Model.ModelView.TblReservasiView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "view_tbl_realisasi_biaya_konsumsi")
@Getter
public class ViewTblRealisasiBiayaKonsumsi {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @OneToOne(targetEntity = TblReservasiView.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "reservasi_id", referencedColumnName = "id")
    TblReservasiView reservasi;

    @Column(name = "id_unit")
    String idUnit;

    @Column(name = "tanggal_permohonan")
    Date tanggalPermohonan;

    @Column(name = "tanggal_acara")
    Date tanggalAcara;

    @Column(name = "jumlah_konsumsi_disetujui")
    Integer jumlahKonsumsiDisetujui;

    @OneToOne(targetEntity = TblMasterVendor.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "snack_pagi", referencedColumnName = "id")
    TblMasterVendor snackPagi;

    @OneToOne(targetEntity = TblMasterVendor.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "snack_siang", referencedColumnName = "id")
    TblMasterVendor snackSiang;

    @OneToOne(targetEntity = TblMasterVendor.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "snack_sore", referencedColumnName = "id")
    TblMasterVendor snackSore;

    @Column(name = "rupiah_konsumsi_pagi")
    BigDecimal rupiahKonsumsiPagi;

    @Column(name = "rupiah_konsumsi_siang")
    BigDecimal rupiahKonsumsiSiang;

    @Column(name = "rupiah_konsumsi_sore")
    BigDecimal rupiahKonsumsiSore;

    @Column(name = "sum_rupiah_konsumsi")
    BigDecimal sumRupiahKonsumsi;

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

    @Column(name = "peserta_internal")
    Integer pesertaInternal;

    @Column(name = "peserta_external")
    Integer pesertaExternal;

    @Column(name = "keterangan_peserta_external")
    String keteranganPesertaExternal;

    String keterangan;
}
