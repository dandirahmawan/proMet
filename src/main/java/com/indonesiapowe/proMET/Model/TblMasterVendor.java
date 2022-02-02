package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tbl_master_vendor")
@Getter
@Setter
public class TblMasterVendor {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "nama_vendor")
    String namaVendor;

    @Column(name = "alamat")
    String alamat;

    @Column(name = "pic")
    String pic;

    @Column(name = "telepon")
    String telepon;

    @Column(name = "email")
    String email;

    @Column(name = "bisa_snack")
    String bisaSnack;

    @Column(name = "harga_snack")
    BigDecimal hargaSnack;

    @Column(name = "bisa_makan")
    String bisaMakan;

    @Column(name = "harga_makan")
    BigDecimal hargaMakan;

    @Column(name = "url_image")
    String urlImage;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "createdDate")
    Date createdDate;

    @Column(name = "modify_by")
    String modifyBy;

    @Column(name = "modify_date")
    Date modifyDate;
}
