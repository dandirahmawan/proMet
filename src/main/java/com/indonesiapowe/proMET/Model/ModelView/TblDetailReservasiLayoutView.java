package com.indonesiapowe.proMET.Model.ModelView;

import com.indonesiapowe.proMET.Model.TblDetailMasterRuangan;
import com.indonesiapowe.proMET.Model.ViewMstRuanganGedung;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_detail_reservasi_layout")
@Getter
/*model untuk keperluan view saja*/
public class TblDetailReservasiLayoutView {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    String id;

    @Column(name = "id_reservasi")
    String idReservasi;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_ruangan", insertable = false, updatable = false)
    String idRuangan;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_layout", insertable = false, updatable = false)
    String idLayout;

//    @OneToOne(targetEntity = TblMasterRuangan.class, cascade = CascadeType.REFRESH)
//    @JoinColumn(name = "id_ruangan", referencedColumnName = "id")
//    TblMasterRuangan ruangan;

    @OneToOne(targetEntity = ViewMstRuanganGedung.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_ruangan", referencedColumnName = "id")
    ViewMstRuanganGedung ruangan;

    @OneToOne(targetEntity = TblDetailMasterRuangan.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_layout", referencedColumnName = "id")
    TblDetailMasterRuangan layout;


    @Column(name = "created_by")
    @Getter(AccessLevel.NONE)
    String createdBy;

    @Column(name = "created_date")
    @Getter(AccessLevel.NONE)
    Date createdDate;

    @Column(name = "modify_by")
    @Getter(AccessLevel.NONE)
    String modifyBy;

    @Column(name = "modify_date")
    @Getter(AccessLevel.NONE)
    Date modifyDate;
}
