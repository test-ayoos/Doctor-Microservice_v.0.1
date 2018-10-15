package com.bytatech.ayoos.doctor.service.mapper;

import com.bytatech.ayoos.doctor.domain.*;
import com.bytatech.ayoos.doctor.service.dto.ReservedSlotDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReservedSlot and its DTO ReservedSlotDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class, SlotStatusMapper.class})
public interface ReservedSlotMapper extends EntityMapper<ReservedSlotDTO, ReservedSlot> {

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "slotStatus.id", target = "slotStatusId")
    ReservedSlotDTO toDto(ReservedSlot reservedSlot);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(source = "slotStatusId", target = "slotStatus")
    ReservedSlot toEntity(ReservedSlotDTO reservedSlotDTO);

    default ReservedSlot fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReservedSlot reservedSlot = new ReservedSlot();
        reservedSlot.setId(id);
        return reservedSlot;
    }
}
