package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_master_ruangan")
@Getter
@Setter
public class TblMasterRuanganView {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "nama_ruangan")
    String namaRuangan;

    @Column(name = "kode_ruangan")
    String kodeRuangan;

    @Column(name = "id_unit", insertable = false, updatable = false)
    String idUnit;

    @OneToOne(targetEntity = TblMasterUnit.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_unit", referencedColumnName = "id")
    TblMasterUnit dataUnit;

    @Column(name = "id_gedung", insertable = false, updatable = false)
    String idGedung;

    @OneToOne(targetEntity = TblMasterGedung.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_gedung", referencedColumnName = "id")
    TblMasterGedung dataGedung;

    @Column(name = "lantai")
    String lantai;

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

    @OneToMany(targetEntity = TblDetailMasterRuangan.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_ruangan", referencedColumnName = "id")
    List<TblDetailMasterRuangan> layouts;
}
