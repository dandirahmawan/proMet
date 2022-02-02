package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_master_unit")
@Getter
@Setter
public class TblMasterUnitView {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "nama_unit")
    String namaUnit;

    @Column(name = "kode_unit")
    String kodeUnit;

    @Column(name = "kode_provinsi")
    String kodeProvinsi;

    @Column(name = "kode_kota")
    String kodeKota;

    @Column(name = "admin_fasilitas")
    String adminFasilitas;

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
