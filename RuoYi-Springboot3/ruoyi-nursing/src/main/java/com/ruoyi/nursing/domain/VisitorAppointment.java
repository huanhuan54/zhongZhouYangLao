package com.ruoyi.nursing.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Visitor appointment entity.
 */
public class VisitorAppointment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer visitType;
    private String visitorName;
    private String visitorPhone;
    private String elderName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public Date getAppointmentTime()
    {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime)
    {
        this.appointmentTime = appointmentTime;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }
}
