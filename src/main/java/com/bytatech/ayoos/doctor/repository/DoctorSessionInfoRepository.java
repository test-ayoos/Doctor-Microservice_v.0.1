package com.bytatech.ayoos.doctor.repository;

import com.bytatech.ayoos.doctor.domain.DoctorSessionInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DoctorSessionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorSessionInfoRepository extends JpaRepository<DoctorSessionInfo, Long> {

}
