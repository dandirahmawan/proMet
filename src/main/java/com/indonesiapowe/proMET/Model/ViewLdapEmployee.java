package com.indonesiapowe.proMET.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "VIEW_LDAP_EMPLOYEE")
@Getter
@Setter
public class ViewLdapEmployee {
    @Id
    @Getter(AccessLevel.NONE)
    String id;

    @Column(name = "LASTNAME")
    @JsonProperty("LASNAME")
    String lastname;

    @Column(name = "EMPLOYEENUMBER")
    @JsonProperty("EMPLOYEENUMBER")
    String employeenumber;

    @Column(name = "JENISKELAMIN")
    @JsonProperty("JENISKELAMIN")
    String jenisKelamin;

    @Column(name = "JOB")
    @JsonProperty("JOB")
    String job;

    @JsonProperty("AGAMA")
    String agama;

    @JsonProperty("JOBTITLE")
    String jobtitle;

    @JsonProperty("ORGANIZATION")
    String organization;

    @JsonProperty("DEPARTMENT")
    String department;

    @JsonProperty("SITEGROUP")
    String sitegroup;

    @JsonProperty("EMAIL")
    String email;

    @JsonProperty("KDCOA")
    String kdcoa;

    @Transient
    @JsonProperty("UNITID")
    String unitId;
}
