package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.DoctorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Doctor and its DTO DoctorDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileInfoMapper.class, LocationMapper.class, ContactInfoMapper.class, DoctorSettingsMapper.class, WorkspaceMapper.class})
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {

    @Mapping(source = "profileInfo.id", target = "profileInfoId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "contactInfo.id", target = "contactInfoId")
    @Mapping(source = "doctorSettings.id", target = "doctorSettingsId")
    @Mapping(source = "workspace.id", target = "workspaceId")
    DoctorDTO toDto(Doctor doctor);

    @Mapping(source = "profileInfoId", target = "profileInfo")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "contactInfoId", target = "contactInfo")
    @Mapping(source = "doctorSettingsId", target = "doctorSettings")
    @Mapping(source = "workspaceId", target = "workspace")
    @Mapping(target = "doctorSessionInfos", ignore = true)
    @Mapping(target = "reservedSlots", ignore = true)
    Doctor toEntity(DoctorDTO doctorDTO);

    default Doctor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
