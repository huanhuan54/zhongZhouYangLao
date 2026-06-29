package com.ruoyi.nursing.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.nursing.domain.Caregiver;
import com.ruoyi.nursing.domain.ResidentBed;
import com.ruoyi.nursing.domain.VisitRecord;
import com.ruoyi.nursing.domain.VisitorAppointment;

/**
 * Mapper for visitor and resident-caregiver features.
 */
public interface ElderCareMapper
{
    List<VisitorAppointment> selectAppointmentList(VisitorAppointment appointment);

    VisitorAppointment selectAppointmentById(Long id);

    int insertAppointment(VisitorAppointment appointment);

    int updateAppointmentStatus(@Param("id") Long id, @Param("status") Integer status,
            @Param("arrivalTime") Date arrivalTime, @Param("updateBy") String updateBy);

    List<VisitRecord> selectVisitRecordList(VisitRecord record);

    int insertVisitRecord(VisitRecord record);

    List<ResidentBed> selectResponsibleResidents(@Param("floorNo") Integer floorNo,
            @Param("elderName") String elderName);

    List<Caregiver> selectCaregivers();

    int deleteResidentCaregivers(Long residentId);

    int insertResidentCaregiver(@Param("residentId") Long residentId,
            @Param("caregiverId") Long caregiverId, @Param("createBy") String createBy);

    Map<String, Object> selectDashboardSummary();
}
