package com.bytatech.ayoos.doctor.repository;

import com.bytatech.ayoos.doctor.domain.Doctor;
import com.bytatech.ayoos.doctor.domain.DoctorSessionInfo;
import com.bytatech.ayoos.doctor.domain.ReservedSlot;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Doctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	@Query("select distinct s  from Doctor d join  d.doctorSessionInfos s join d.profileInfo p where s.date=:date And p.profileName=:profileName")
	public Set<DoctorSessionInfo> findSessions(@Param("profileName")String profileName ,@Param("date") LocalDate date);
	
	@Query("select distinct rs  from Doctor d join  d.reservedSlots rs join d.profileInfo p where rs.date=:date And p.profileName=:profileName")
	public Set<ReservedSlot> findReservedSlot(@Param("profileName")String profileName ,@Param("date") LocalDate date);
	
	
	Doctor findByProfileInfo_ProfileName(String profileName);
}
