package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_detail_master_ruangan")
@Getter
@Setter
public class TblDetailMasterRuangan {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "id_ruangan")
    String idRuangan;

    @Column(name = "layout")
    String layout;

    @Column(name = "kapasitas")
    Integer kapasitas;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "createdDate")
    Date createdDate;

    @Column(name = "modify_by")
    String modifyBy;

    @Column(name = "modify_date")
    Date modifyDate;
}
