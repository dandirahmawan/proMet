package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Model.TblMasterUnit;
import com.indonesiapowe.proMET.Model.ViewLdapEmployee;
import com.indonesiapowe.proMET.Model.ViewLdapTemp;
import com.indonesiapowe.proMET.Model.ViewTblUsers;
import com.indonesiapowe.proMET.Repository.TblMasterUnitRepository;
import com.indonesiapowe.proMET.Repository.ViewLdapEmployeeRepository;
import com.indonesiapowe.proMET.Repository.ViewLdapTempRepository;
import com.indonesiapowe.proMET.Repository.ViewTblMasterUnitRepository;
import com.indonesiapowe.proMET.Service.ViewTblUsersService;
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

    @Autowired
    ViewTblUsersService vtus;

    @GetMapping("/{email}")
    public Object getUser(@PathVariable("email") String email){
        Optional<ViewLdapEmployee> emp;
//        emp = vlr.findByEmail(email);
        ViewTblUsers data = vtus.getByEmail(email);
//        System.out.println(data.getSitegroup());
//        System.out.println("----------------");
        if(data != null){
            String unitNama = (data.getSitegroup().equals("IP")) ? "KANTOR PUSAT" : data.getSitegroup();
            Optional<TblMasterUnit> unit = unitRepo.findByNamaUnitIgnoreCase(unitNama);

            String ui = unit.map(TblMasterUnit::getId).orElse("");
            data.setUnitId(ui);
        }

        return data;

//        if(!id.equals("")){
//            Optional<TblMasterUnit> unit = unitRepo.findByNamaUnitIgnoreCase(emp.get().getSitegroup());
//            String ui = unit.map(TblMasterUnit::getId).orElse("");
//            emp.get().setUnitId(ui);
//            return emp;
//        }else{
//            /*search data on temp ldap view*/
//            Optional<ViewLdapTemp> emp2 = vltr.findByEmailIgnoreCase(email);
//            String id2 = emp2.map(ViewLdapTemp::getId).orElse("");
//            System.out.println(id2+", "+email);
//            if(!id2.equals("")){
//                String unitName = emp2.get().getSitegroup();
//                unitName = (unitName.equals("IP")) ? "KANTOR PUSAT" : unitName;
//                Optional<TblMasterUnit> unit = unitRepo.findByNamaUnitIgnoreCase(unitName);
//                String ui = unit.map(TblMasterUnit::getId).orElse("");
//
//                emp2.get().setUnitId(ui);
//                emp2.get().setSitegroup(unitName);
//                return emp2;
//            }else{
//                return emp;
//            }
//        }
    }
}
