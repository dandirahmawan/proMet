package com.indonesiapowe.proMET.Controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.indonesiapowe.proMET.Service.AuthService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthService as;

    @PostMapping("/login")
    public Object login(@RequestBody String parameter){
        JSONObject obj = new JSONObject(parameter);
        return as.login(obj);
    }
}
