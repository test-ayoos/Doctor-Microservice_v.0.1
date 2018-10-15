package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.DoctorSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DoctorSettings and its DTO DoctorSettingsDTO.
 */
@Mapper(componentModel = "spring", uses = {PaymentSettingsMapper.class})
public interface DoctorSettingsMapper extends EntityMapper<DoctorSettingsDTO, DoctorSettings> {

    @Mapping(source = "paymentSettings.id", target = "paymentSettingsId")
    DoctorSettingsDTO toDto(DoctorSettings doctorSettings);

    @Mapping(source = "paymentSettingsId", target = "paymentSettings")
    DoctorSettings toEntity(DoctorSettingsDTO doctorSettingsDTO);

    default DoctorSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        DoctorSettings doctorSettings = new DoctorSettings();
        doctorSettings.setId(id);
        return doctorSettings;
    }
}
