package com.indonesiapowe.proMET.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "view_tbl_upload")
@Getter
@Setter
public class ViewTblUpload {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "file_name")
    String fileName;

    @Column(name = "file_name_upload")
    String fileNameUpload;

    @Column(name = "path")
    String path;

    @Column(name = "upload_date")
    Date uploadDate;

    @Column(name = "upload_by")
    String uploadBy;

    @Column(name = "id_lampiran")
    String idLampiran;

    @Column(name = "gedung_upload")
    String gedungUpload;

    @Column(name = "file_vendor")
    String fileVendor;
}
