package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewLdapEmployee;
import com.indonesiapowe.proMET.Model.ViewLdapTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewLdapTempRepository extends JpaRepository<ViewLdapTemp, String> {
    Optional<ViewLdapTemp> findByEmailIgnoreCase(String email);
}
