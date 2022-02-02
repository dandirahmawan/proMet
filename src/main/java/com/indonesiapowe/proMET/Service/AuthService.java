package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.TblMasterUnit;
import com.indonesiapowe.proMET.Model.ViewLdapEmployee;
import com.indonesiapowe.proMET.Repository.TblMasterUnitRepository;
import com.indonesiapowe.proMET.Repository.ViewLdapEmployeeRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Value("${url.api-ldap}")
    private String urlApiLdap;

    @Autowired
    ViewLdapEmployeeRepository ldapRepo;

    @Autowired
    TblMasterUnitRepository unitRepo;

    public Object login(JSONObject obj){
        String userName = obj.getString("username");
        String password = obj.getString("password");
        Map<String, Object> map = this.loginToLdap(userName, password);

        ViewLdapEmployee emp;

        if((Boolean) map.get("result")){
            String email = map.get("email").toString();
            emp = ldapRepo.findByEmail(email).get();

            /*get unit id user*/
            Optional<TblMasterUnit> unit = unitRepo.findByNamaUnitIgnoreCase(emp.getSitegroup());
            String ui = unit.map(TblMasterUnit::getId).orElse("");
            emp.setUnitId(ui);
            emp = ldapRepo.findByEmail(email).get();
            return emp;
        }else{
            return null;
        }
    }

    public Map<String, Object> loginToLdap(String username, String password) {
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("username", username);
        request.put("password", password);

        String url = urlApiLdap + "/login";
        RestTemplate restTemplate = new RestTemplate();

        HashMap<String, Object> result = new HashMap<String, Object>();
        try {
            result = restTemplate.postForObject(url, request, HashMap.class);
        } catch (RestClientException e) {
            if (e instanceof HttpStatusCodeException) {
                String errorResponse = ((HttpStatusCodeException) e).getResponseBodyAsString();
//                log.error("error messsage login ldap = " + errorResponse);

                result.put("statusLogin", "GAGAL");
                result.put("message", errorResponse);
            }
        }
        if (result.get("statusLogin").toString().equalsIgnoreCase("BERHASIL"))
            result.put("result", true);
        else
            result.put("result", false);
        return result;
    }
}
