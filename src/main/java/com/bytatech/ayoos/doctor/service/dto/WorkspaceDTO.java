package com.bytatech.ayoos.doctor.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Workspace entity.
 */
public class WorkspaceDTO implements Serializable {

    private Long id;

    private String workspaceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkspaceDTO workspaceDTO = (WorkspaceDTO) o;
        if (workspaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workspaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkspaceDTO{" +
            "id=" + getId() +
            ", workspaceId='" + getWorkspaceId() + "'" +
            "}";
    }
}
