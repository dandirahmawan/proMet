package com.indonesiapowe.proMET.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "view_tbl_users")
@Getter
@Setter
public class ViewTblUsers implements Serializable {
    @Id
    @JsonProperty("ID")
    String id;

    @JsonProperty("USERNAME")
    String username;

    @JsonProperty("EMAIL")
    String email;

    @JsonProperty("ROLEID")
    @Column(name = "role_id")
    String roleId;

    @JsonProperty("UNITID")
    @Column(name = "unit_id")
    String unitId;

//    @Column(name = "direktorat_id")
//    String direktoratId;

//    @Column(name = "active_date")
//    Date activeDate;
//
//    @Column(name = "inactive_date")
//    Date inactiveDate;
//
//    @Column(name = "user_type")
//    String userType;

//    @Column(name = "create_date")
//    Date createDate;

//    @Column(name = "create_by")
//    String createdBy;

    @JsonProperty("SITEGROUP")
    String sitegroup;

    @JsonProperty("ORGANIZATION")
    String organization;

    @JsonProperty("EMPLOYEENUMBER")
    String employeenumber;

    @JsonProperty("JENISKELAMIN")
    String jeniskelamin;

    @JsonProperty("DEPARTMENT")
    String department;

    @JsonProperty("JOB")
    String job;

    @JsonProperty("JOBTITLE")
    String jobtitle;

    @JsonProperty("KDCOA")
    String kdcoa;

    @JsonProperty("LASNAME")
    String lastname;
}
