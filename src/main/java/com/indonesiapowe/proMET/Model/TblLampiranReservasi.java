package com.indonesiapowe.proMET.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_lampiran_reservasi")
@Getter
@Setter
public class
TblLampiranReservasi {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(name = "id_reservasi")
    String idReservasi;

    @Column(name = "judul_dokumen")
    @Getter(AccessLevel.NONE)
    String judulDokumen;

    @Column(name = "tanggal_diunggah")
    @Getter(AccessLevel.NONE)
    Date tanggalDiunggah;

    @Column(name = "url")
    @Getter(AccessLevel.NONE)
    String url;

    @OneToOne(targetEntity = TblUpload.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_upload", referencedColumnName = "id")
    TblUpload file;
}
