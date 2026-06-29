package com.ruoyi.nursing.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Resident bed and caregiver view.
 */
public class ResidentBed extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer floorNo;
    private String roomNo;
    private String bedNo;
    private String elderName;
    private String elderGender;
    private String nursingLevel;
    private String caregiverNames;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getFloorNo()
    {
        return floorNo;
    }

    public void setFloorNo(Integer floorNo)
    {
        this.floorNo = floorNo;
    }

    public String getRoomNo()
    {
        return roomNo;
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = roomNo;
    }

    public String getBedNo()
    {
        return bedNo;
    }

    public void setBedNo(String bedNo)
    {
        this.bedNo = bedNo;
    }

    public String getElderName()
    {
        return elderName;
    }

    public void setElderName(String elderName)
    {
        this.elderName = elderName;
    }

    public String getElderGender()
    {
        return elderGender;
    }

    public void setElderGender(String elderGender)
    {
        this.elderGender = elderGender;
    }

    public String getNursingLevel()
    {
        return nursingLevel;
    }

    public void setNursingLevel(String nursingLevel)
    {
        this.nursingLevel = nursingLevel;
    }

    public String getCaregiverNames()
    {
        return caregiverNames;
    }

    public void setCaregiverNames(String caregiverNames)
    {
        this.caregiverNames = caregiverNames;
    }
}
