package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Model.TblMasterUnit;
import com.indonesiapowe.proMET.Model.ViewLdapEmployee;
import com.indonesiapowe.proMET.Model.ViewLdapTemp;
import com.indonesiapowe.proMET.Repository.TblMasterUnitRepository;
import com.indonesiapowe.proMET.Repository.ViewLdapEmployeeRepository;
import com.indonesiapowe.proMET.Repository.ViewLdapTempRepository;
import com.indonesiapowe.proMET.Repository.ViewTblMasterUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/user")
@RestController

public class UserController {

    @Autowired
    ViewLdapEmployeeRepository vlr;

    @Autowired
    ViewLdapTempRepository vltr;

    @Autowired
    TblMasterUnitRepository unitRepo;

    @GetMapping("/{email}")
    public Object getUser(@PathVariable("email") String email){
        Optional<ViewLdapEmployee> emp;
        emp = vlr.findByEmail(email);
        String id = emp.map(ViewLdapEmployee::getEmployeenumber).orElse("");
        if(!id.equals("")){
            Optional<TblMasterUnit> unit = unitRepo.findByNamaUnitIgnoreCase(emp.get().getSitegroup());
            String ui = unit.map(TblMasterUnit::getId).orElse("");
            emp.get().setUnitId(ui);
            return emp;
        }else{
            /*search data on temp ldap view*/
            Optional<ViewLdapTemp> emp2 = vltr.findByEmailIgnoreCase(email);
            String id2 = emp2.map(ViewLdapTemp::getId).orElse("");
            if(!id2.equals("")){
                Optional<TblMasterUnit> unit = unitRepo.findByNamaUnitIgnoreCase(emp2.get().getSitegroup());
                String ui = unit.map(TblMasterUnit::getId).orElse("");
                emp2.get().setUnitId(ui);
                return emp2;
            }else{
                return emp;
            }
        }
    }
}
