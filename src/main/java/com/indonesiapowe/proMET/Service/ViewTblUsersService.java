package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.ViewTblUsers;
import com.indonesiapowe.proMET.Repository.ViewTblUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewTblUsersService {
    @Autowired
    ViewTblUsersRepository vtur;

    public ViewTblUsers getByEmail(String email){
        ViewTblUsers viewTblUsers = new ViewTblUsers();
        List<ViewTblUsers> data = vtur.findByEmail(email);
        if(data.size() > 0){
            viewTblUsers = data.get(0);
        }

        return viewTblUsers;
    }
}
