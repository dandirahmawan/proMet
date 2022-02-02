package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewLdapEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewLdapEmployeeRepository extends JpaRepository<ViewLdapEmployee, String> {
    Optional<ViewLdapEmployee> findByEmail(String email);
}
