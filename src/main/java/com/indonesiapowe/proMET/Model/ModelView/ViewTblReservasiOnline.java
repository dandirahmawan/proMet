package com.indonesiapowe.proMET.Model.ModelView;

import com.indonesiapowe.proMET.Model.TblHadirReservasiOnline;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "view_tbl_reservasi_online")
@Getter
@Setter
public class ViewTblReservasiOnline {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    String title;
    String description;
    String link;
    String password;
    String media;
    String pic;
    String lokasi;

    @Column(name = "created_date")
    Date createdDate;

    @Column(name = "modified_date")
    Date modifiedDate;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "id_meeting")
    String idMeeting;

    @Column(name = "schedule_meet")
    Date scheduleMeet;

    @Column(name = "schedule_meet_end")
    Date scheduleMeetEnd;

    @Column(name = "generated_link")
    String generatedLink;

    @Column(name = "qr_code")
    String qrCode;
}
