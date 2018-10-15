package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.DoctorSessionInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DoctorSessionInfo and its DTO DoctorSessionInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface DoctorSessionInfoMapper extends EntityMapper<DoctorSessionInfoDTO, DoctorSessionInfo> {

    @Mapping(source = "doctor.id", target = "doctorId")
    DoctorSessionInfoDTO toDto(DoctorSessionInfo doctorSessionInfo);

    @Mapping(source = "doctorId", target = "doctor")
    DoctorSessionInfo toEntity(DoctorSessionInfoDTO doctorSessionInfoDTO);

    default DoctorSessionInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        DoctorSessionInfo doctorSessionInfo = new DoctorSessionInfo();
        doctorSessionInfo.setId(id);
        return doctorSessionInfo;
    }
}
