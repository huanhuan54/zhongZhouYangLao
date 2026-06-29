package com.ruoyi.nursing.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.ruoyi.nursing.domain.Caregiver;
import com.ruoyi.nursing.domain.CaregiverAssignment;
import com.ruoyi.nursing.domain.ResidentBed;
import com.ruoyi.nursing.domain.VisitRecord;
import com.ruoyi.nursing.domain.VisitorAppointment;

public interface IElderCareService
{
    List<VisitorAppointment> selectAppointmentList(VisitorAppointment appointment);

    int insertAppointment(VisitorAppointment appointment);

    int arrive(Long id, Date arrivalTime, String username);

    int cancel(Long id, String username);

    List<VisitRecord> selectVisitRecordList(VisitRecord record);

    int insertVisitRecord(VisitRecord record);

    List<ResidentBed> selectResponsibleResidents(Integer floorNo, String elderName);

    List<Caregiver> selectCaregivers();

    int assignCaregivers(CaregiverAssignment assignment, String username);

    Map<String, Object> selectDashboardSummary();
}
