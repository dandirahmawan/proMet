package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_detail_reservasi_fasilitas")
@Getter
@Setter
public class TblDetailReservasiFasilitas {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "id_reservasi")
    String idReservasi;

    @OneToOne(targetEntity = TblMasterFasilitas.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_fasilitas", referencedColumnName = "id")
    TblMasterFasilitas dataFasilitas;

    @Column(name = "jumlah")
    Integer jumlah;

    @Column(name = "keterangan")
    String keterangan;

    @Column(name = "created_date")
    Date createdDate;

    @Column(name = "modify_date")
    Date modifyDate;

    @Column(name = "modify_by")
    String modifyBy;
}
