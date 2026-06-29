package com.ruoyi.nursing.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Nursing task schedule entity.
 */
public class NursingTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long residentId;
    private Long projectId;
    private Long caregiverId;
    private Integer taskType;
    private Integer status;
    private String relatedOrderNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedServiceTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    private String executeImage;
    private String executeRecord;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    private String cancelReason;

    private String elderName;
    private String elderGender;
    private String bedNo;
    private String nursingLevel;
    private String projectName;
    private String caregiverName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getResidentId() { return residentId; }
    public void setResidentId(Long residentId) { this.residentId = residentId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getCaregiverId() { return caregiverId; }
    public void setCaregiverId(Long caregiverId) { this.caregiverId = caregiverId; }
    public Integer getTaskType() { return taskType; }
    public void setTaskType(Integer taskType) { this.taskType = taskType; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRelatedOrderNo() { return relatedOrderNo; }
    public void setRelatedOrderNo(String relatedOrderNo) { this.relatedOrderNo = relatedOrderNo; }
    public Date getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(Date expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public Date getExecuteTime() { return executeTime; }
    public void setExecuteTime(Date executeTime) { this.executeTime = executeTime; }
    public String getExecuteImage() { return executeImage; }
    public void setExecuteImage(String executeImage) { this.executeImage = executeImage; }
    public String getExecuteRecord() { return executeRecord; }
    public void setExecuteRecord(String executeRecord) { this.executeRecord = executeRecord; }
    public Date getCancelTime() { return cancelTime; }
    public void setCancelTime(Date cancelTime) { this.cancelTime = cancelTime; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getElderGender() { return elderGender; }
    public void setElderGender(String elderGender) { this.elderGender = elderGender; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getNursingLevel() { return nursingLevel; }
    public void setNursingLevel(String nursingLevel) { this.nursingLevel = nursingLevel; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getCaregiverName() { return caregiverName; }
    public void setCaregiverName(String caregiverName) { this.caregiverName = caregiverName; }
}
