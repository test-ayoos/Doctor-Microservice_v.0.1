package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.ProfileInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProfileInfo and its DTO ProfileInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfileInfoMapper extends EntityMapper<ProfileInfoDTO, ProfileInfo> {



    default ProfileInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setId(id);
        return profileInfo;
    }
}
