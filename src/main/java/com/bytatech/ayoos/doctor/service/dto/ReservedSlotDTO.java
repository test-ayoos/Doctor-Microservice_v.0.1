package com.bytatech.ayoos.doctor.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ReservedSlot entity.
 */
public class ReservedSlotDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private Long doctorId;

    private Long slotStatusId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getSlotStatusId() {
        return slotStatusId;
    }

    public void setSlotStatusId(Long slotStatusId) {
        this.slotStatusId = slotStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReservedSlotDTO reservedSlotDTO = (ReservedSlotDTO) o;
        if (reservedSlotDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservedSlotDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservedSlotDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", doctor=" + getDoctorId() +
            ", slotStatus=" + getSlotStatusId() +
            "}";
    }
}
