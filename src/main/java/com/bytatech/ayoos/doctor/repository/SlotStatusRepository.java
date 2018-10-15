package com.bytatech.ayoos.doctor.repository;

import com.bytatech.ayoos.doctor.domain.SlotStatus;

import java.util.Set;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SlotStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlotStatusRepository extends JpaRepository<SlotStatus, Long> {
	
	
	
	 SlotStatus findByStatus(String status);
	
}
