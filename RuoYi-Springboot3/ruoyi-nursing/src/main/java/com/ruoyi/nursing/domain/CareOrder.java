package com.ruoyi.nursing.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Elder-care service order.
 */
public class CareOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderNo;
    private Long residentId;
    private Long projectId;
    private Long taskId;
    private BigDecimal orderAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedServiceTime;

    private String orderUser;
    private String orderUserPhone;
    private Integer orderStatus;
    private Integer paymentStatus;
    private String payChannel;
    private String payOrderNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    private String cancelReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    private Integer refundStatus;
    private String elderName;
    private String bedNo;
    private String projectName;
    private String caregiverName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    private String executeImage;
    private String executeRecord;
    private String refundNo;
    private BigDecimal refundAmount;
    private String applicant;
    private String applicantType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    private String refundReason;
    private String refundChannel;
    private String refundMethod;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refundTime;

    private String refundTransactionNo;
    private String failureCode;
    private String failureReason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getResidentId() { return residentId; }
    public void setResidentId(Long residentId) { this.residentId = residentId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public Date getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(Date expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public String getOrderUser() { return orderUser; }
    public void setOrderUser(String orderUser) { this.orderUser = orderUser; }
    public String getOrderUserPhone() { return orderUserPhone; }
    public void setOrderUserPhone(String orderUserPhone) { this.orderUserPhone = orderUserPhone; }
    public Integer getOrderStatus() { return orderStatus; }
    public void setOrderStatus(Integer orderStatus) { this.orderStatus = orderStatus; }
    public Integer getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(Integer paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPayChannel() { return payChannel; }
    public void setPayChannel(String payChannel) { this.payChannel = payChannel; }
    public String getPayOrderNo() { return payOrderNo; }
    public void setPayOrderNo(String payOrderNo) { this.payOrderNo = payOrderNo; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public Date getCancelTime() { return cancelTime; }
    public void setCancelTime(Date cancelTime) { this.cancelTime = cancelTime; }
    public Integer getRefundStatus() { return refundStatus; }
    public void setRefundStatus(Integer refundStatus) { this.refundStatus = refundStatus; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getCaregiverName() { return caregiverName; }
    public void setCaregiverName(String caregiverName) { this.caregiverName = caregiverName; }
    public Date getExecuteTime() { return executeTime; }
    public void setExecuteTime(Date executeTime) { this.executeTime = executeTime; }
    public String getExecuteImage() { return executeImage; }
    public void setExecuteImage(String executeImage) { this.executeImage = executeImage; }
    public String getExecuteRecord() { return executeRecord; }
    public void setExecuteRecord(String executeRecord) { this.executeRecord = executeRecord; }
    public String getRefundNo() { return refundNo; }
    public void setRefundNo(String refundNo) { this.refundNo = refundNo; }
    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getApplicantType() { return applicantType; }
    public void setApplicantType(String applicantType) { this.applicantType = applicantType; }
    public Date getApplyTime() { return applyTime; }
    public void setApplyTime(Date applyTime) { this.applyTime = applyTime; }
    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }
    public String getRefundChannel() { return refundChannel; }
    public void setRefundChannel(String refundChannel) { this.refundChannel = refundChannel; }
    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
    public Date getRefundTime() { return refundTime; }
    public void setRefundTime(Date refundTime) { this.refundTime = refundTime; }
    public String getRefundTransactionNo() { return refundTransactionNo; }
    public void setRefundTransactionNo(String refundTransactionNo) { this.refundTransactionNo = refundTransactionNo; }
    public String getFailureCode() { return failureCode; }
    public void setFailureCode(String failureCode) { this.failureCode = failureCode; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
