package com.ruoyi.nursing.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Resident caregiver assignment request.
 */
public class CaregiverAssignment implements Serializable
{
    private static final long serialVersionUID = 1L;

    private List<Long> residentIds;
    private List<Long> caregiverIds;

    public List<Long> getResidentIds()
    {
        return residentIds;
    }

    public void setResidentIds(List<Long> residentIds)
    {
        this.residentIds = residentIds;
    }

    public List<Long> getCaregiverIds()
    {
        return caregiverIds;
    }

    public void setCaregiverIds(List<Long> caregiverIds)
    {
        this.caregiverIds = caregiverIds;
    }
}
