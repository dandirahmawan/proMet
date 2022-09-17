package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_hadir_reservasi_online")
@Getter
@Setter
public class TblHadirReservasiOnline {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    String nama;
    String nid;
    String instansi;

    @Column(name = "divisi_unit")
    String divisiUnit;

    String jabatan;

    @Column(name = "created_date")
    Date createdDate;

    @Column(name = "reservasi_id")
    String reservasiId;

}
