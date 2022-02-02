package com.indonesiapowe.proMET.Repository;

import com.indonesiapowe.proMET.Model.ViewTblUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewTblUploadRepository extends JpaRepository<ViewTblUpload, String> {
    List<ViewTblUpload> findByIdLampiranIsNullAndGedungUploadIsNullAndFileVendorIsNull();
}
