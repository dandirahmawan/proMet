package com.indonesiapowe.proMET.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "view_tbl_master_gedung")
public class ViewTblMasterGedung {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "nama_gedung")
    String namaGedung;

    @Column(name = "kode_gedung")
    String kodeGedung;

    @Column(name = "kode_provinsi")
    String kodeProvinsi;

    @Column(name = "kode_kota")
    String kodeKota;

    @Column(name = "alamat")
    String alamat;

    @Column(name = "url_image")
    String uriImage;

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

    @Column(name = "kota")
    String kota;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_unit", insertable = false, updatable = false)
    String idUnit;

    @OneToOne(targetEntity = TblMasterUnit.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_unit", referencedColumnName = "id")
    TblMasterUnit unit;

    @OneToOne(targetEntity = TblUpload.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_upload", referencedColumnName = "id")
    TblUpload fileUpload;
}
