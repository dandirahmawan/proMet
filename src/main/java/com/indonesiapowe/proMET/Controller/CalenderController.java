package com.indonesiapowe.proMET.Controller;

import com.indonesiapowe.proMET.Service.ReservasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calender")
public class CalenderController {

    @Autowired
    ReservasiService rs;

    @GetMapping()
    public Object getAll(){
        return rs.getApprovedData();
    }
}
