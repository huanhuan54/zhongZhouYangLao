package com.ruoyi.nursing.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Completed visitor record.
 */
public class VisitRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long appointmentId;
    private Integer visitType;
    private String visitorName;
    private String visitorPhone;
    private String elderName;
    private String relationName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getAppointmentId()
    {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId)
    {
        this.appointmentId = appointmentId;
    }

    public Integer getVisitType()
    {
        return visitType;
    }

    public void setVisitType(Integer visitType)
    {
        this.visitType = visitType;
    }

    public String getVisitorName()
    {
        return visitorName;
    }

    public void setVisitorName(String visitorName)
    {
        this.visitorName = visitorName;
    }

    public String getVisitorPhone()
    {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone)
    {
        this.visitorPhone = visitorPhone;
    }

    public String getElderName()
    {
        return elderName;
    }

    public void setElderName(String elderName)
    {
        this.elderName = elderName;
    }

    public String getRelationName()
    {
        return relationName;
    }

    public void setRelationName(String relationName)
    {
        this.relationName = relationName;
    }

    public Date getVisitTime()
    {
        return visitTime;
    }

    public void setVisitTime(Date visitTime)
    {
        this.visitTime = visitTime;
    }
}
