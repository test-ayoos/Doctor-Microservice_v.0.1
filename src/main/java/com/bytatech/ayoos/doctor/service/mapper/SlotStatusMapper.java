package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.SlotStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SlotStatus and its DTO SlotStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SlotStatusMapper extends EntityMapper<SlotStatusDTO, SlotStatus> {



    default SlotStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        SlotStatus slotStatus = new SlotStatus();
        slotStatus.setId(id);
        return slotStatus;
    }
}
