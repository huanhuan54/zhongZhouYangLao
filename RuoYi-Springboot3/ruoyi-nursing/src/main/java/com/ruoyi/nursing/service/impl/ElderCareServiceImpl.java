package com.ruoyi.nursing.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.nursing.domain.Caregiver;
import com.ruoyi.nursing.domain.CaregiverAssignment;
import com.ruoyi.nursing.domain.ResidentBed;
import com.ruoyi.nursing.domain.VisitRecord;
import com.ruoyi.nursing.domain.VisitorAppointment;
import com.ruoyi.nursing.mapper.ElderCareMapper;
import com.ruoyi.nursing.service.IElderCareService;

@Service
public class ElderCareServiceImpl implements IElderCareService
{
    @Autowired
    private ElderCareMapper elderCareMapper;

    @Override
    public List<VisitorAppointment> selectAppointmentList(VisitorAppointment appointment)
    {
        return elderCareMapper.selectAppointmentList(appointment);
    }

    @Override
    public int insertAppointment(VisitorAppointment appointment)
    {
        if (appointment.getStatus() == null)
        {
            appointment.setStatus(0);
        }
        return elderCareMapper.insertAppointment(appointment);
    }

    @Override
    @Transactional
    public int arrive(Long id, Date arrivalTime, String username)
    {
        VisitorAppointment appointment = elderCareMapper.selectAppointmentById(id);
        if (appointment == null || appointment.getStatus() == null || appointment.getStatus() != 0)
        {
            return 0;
        }

        Date actualArrivalTime = arrivalTime == null ? new Date() : arrivalTime;
        int rows = elderCareMapper.updateAppointmentStatus(id, 1, actualArrivalTime, username);
        if (rows > 0)
        {
            VisitRecord record = new VisitRecord();
            record.setAppointmentId(appointment.getId());
            record.setVisitType(appointment.getVisitType());
            record.setVisitorName(appointment.getVisitorName());
            record.setVisitorPhone(appointment.getVisitorPhone());
            record.setElderName(appointment.getElderName());
            record.setRelationName(appointment.getVisitType() != null && appointment.getVisitType() == 0 ? "参观访客" : "家属");
            record.setVisitTime(actualArrivalTime);
            record.setCreateBy(username);
            record.setRemark("由预约到院自动生成");
            elderCareMapper.insertVisitRecord(record);
        }
        return rows;
    }

    @Override
    public int cancel(Long id, String username)
    {
        return elderCareMapper.updateAppointmentStatus(id, 2, null, username);
    }

    @Override
    public List<VisitRecord> selectVisitRecordList(VisitRecord record)
    {
        return elderCareMapper.selectVisitRecordList(record);
    }

    @Override
    public int insertVisitRecord(VisitRecord record)
    {
        if (record.getVisitTime() == null)
        {
            record.setVisitTime(new Date());
        }
        return elderCareMapper.insertVisitRecord(record);
    }

    @Override
    public List<ResidentBed> selectResponsibleResidents(Integer floorNo, String elderName)
    {
        return elderCareMapper.selectResponsibleResidents(floorNo, elderName);
    }

    @Override
    public List<Caregiver> selectCaregivers()
    {
        return elderCareMapper.selectCaregivers();
    }

    @Override
    @Transactional
    public int assignCaregivers(CaregiverAssignment assignment, String username)
    {
        if (assignment == null || StringUtils.isEmpty(assignment.getResidentIds()))
        {
            return 0;
        }

        int rows = 0;
        for (Long residentId : assignment.getResidentIds())
        {
            elderCareMapper.deleteResidentCaregivers(residentId);
            if (StringUtils.isNotEmpty(assignment.getCaregiverIds()))
            {
                for (Long caregiverId : assignment.getCaregiverIds())
                {
                    rows += elderCareMapper.insertResidentCaregiver(residentId, caregiverId, username);
                }
            }
            else
            {
                rows++;
            }
        }
        return rows;
    }

    @Override
    public Map<String, Object> selectDashboardSummary()
    {
        return elderCareMapper.selectDashboardSummary();
    }
}
