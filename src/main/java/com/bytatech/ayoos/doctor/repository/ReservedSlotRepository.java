package com.bytatech.ayoos.doctor.repository;

import com.bytatech.ayoos.doctor.domain.ReservedSlot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReservedSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservedSlotRepository extends JpaRepository<ReservedSlot, Long> {

}
