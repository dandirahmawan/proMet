package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewTblUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewTblUsersRepository extends JpaRepository<ViewTblUsers, String> {
    List<ViewTblUsers> findByEmail(String email);
}
