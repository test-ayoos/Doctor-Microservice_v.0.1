package com.bytatech.ayoos.doctor.repository;

import com.bytatech.ayoos.doctor.domain.PaymentSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PaymentSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentSettingsRepository extends JpaRepository<PaymentSettings, Long> {

}
