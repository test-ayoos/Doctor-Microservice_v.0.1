package com.bytatech.ayoos.doctor.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DoctorSessionInfo entity.
 */
public class DoctorSessionInfoDTO implements Serializable {

    private Long id;

    private String sessionName;

    private LocalDate date;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private ZonedDateTime interval;

    private Long doctorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
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

    public ZonedDateTime getInterval() {
        return interval;
    }

    public void setInterval(ZonedDateTime interval) {
        this.interval = interval;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DoctorSessionInfoDTO doctorSessionInfoDTO = (DoctorSessionInfoDTO) o;
        if (doctorSessionInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctorSessionInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DoctorSessionInfoDTO{" +
            "id=" + getId() +
            ", sessionName='" + getSessionName() + "'" +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", interval='" + getInterval() + "'" +
            ", doctor=" + getDoctorId() +
            "}";
    }
}
