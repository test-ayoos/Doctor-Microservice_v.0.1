package com.bytatech.ayoos.doctor.repository;

import com.bytatech.ayoos.doctor.domain.DoctorSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DoctorSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorSettingsRepository extends JpaRepository<DoctorSettings, Long> {

}
