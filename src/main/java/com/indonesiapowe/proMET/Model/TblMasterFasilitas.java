package com.indonesiapowe.proMET.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_master_fasilitas")
@Getter
@Setter
public class TblMasterFasilitas {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    String nama;
    String kode;
    String deskripsi;
    String status;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "created_date")
    Date createdDate;

    @Column(name = "modify_by")
    String modifyBy;

    @Column(name = "modify_date")
    Date modifyDate;

    @Column(name = "jumlah")
    Integer jumlah;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_unit", insertable = false, updatable = false)
    String idUnit;

    @OneToOne(targetEntity = TblMasterUnit.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_unit", referencedColumnName = "id")
    TblMasterUnit unit;
}
