package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.ContactInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ContactInfo and its DTO ContactInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactInfoMapper extends EntityMapper<ContactInfoDTO, ContactInfo> {



    default ContactInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(id);
        return contactInfo;
    }
}
