package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.PaymentSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PaymentSettings and its DTO PaymentSettingsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentSettingsMapper extends EntityMapper<PaymentSettingsDTO, PaymentSettings> {



    default PaymentSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.setId(id);
        return paymentSettings;
    }
}
