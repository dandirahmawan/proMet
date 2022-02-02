package com.indonesiapowe.proMET.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_detail_reservasi_konsumsi")
@Getter
@Setter
public class TblDetailReservasiKonsumsi {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "id_reservasi")
    String idReservasi;

    @OneToOne(targetEntity = TblMasterKonsumsi.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_konsumsi", referencedColumnName = "id")
    TblMasterKonsumsi dataKonsumsi;

    @Column(name = "created_by")
    @Getter(AccessLevel.NONE)
    String createdBy;

    @Column(name = "created_date")
    @Getter(AccessLevel.NONE)
    Date createdDate;

    @Column(name = "modify_date")
    @Getter(AccessLevel.NONE)
    Date modifyDate;

    @Column(name = "modify_by")
    @Getter(AccessLevel.NONE)
    Date modifyBy;
}
