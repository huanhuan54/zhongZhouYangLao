package com.ruoyi.nursing.domain;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Nursing project entity.
 */
public class NursingProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "项目名称")
    private String name;

    @Excel(name = "项目编号")
    private String projectCode;

    @Excel(name = "项目类型")
    private String projectType;

    @Excel(name = "排序号")
    private Integer orderNo;

    @Excel(name = "单位")
    private String unit;

    @Excel(name = "价格")
    private BigDecimal price;

    @Excel(name = "服务时长")
    private Integer serviceDuration;

    @Excel(name = "图片")
    private String image;

    @Excel(name = "护理要求")
    private String nursingRequirement;

    @Excel(name = "状态", readConverterExp = "0=禁用,1=启用")
    private Integer status;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "项目名称不能为空")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getProjectCode()
    {
        return projectCode;
    }

    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }

    public String getProjectType()
    {
        return projectType;
    }

    public void setProjectType(String projectType)
    {
        this.projectType = projectType;
    }

    @NotNull(message = "排序号不能为空")
    public Integer getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo)
    {
        this.orderNo = orderNo;
    }

    @NotBlank(message = "单位不能为空")
    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    @NotNull(message = "价格不能为空")
    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public Integer getServiceDuration()
    {
        return serviceDuration;
    }

    public void setServiceDuration(Integer serviceDuration)
    {
        this.serviceDuration = serviceDuration;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getNursingRequirement()
    {
        return nursingRequirement;
    }

    public void setNursingRequirement(String nursingRequirement)
    {
        this.nursingRequirement = nursingRequirement;
    }

    @NotNull(message = "状态不能为空")
    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("projectCode", getProjectCode())
                .append("projectType", getProjectType())
                .append("orderNo", getOrderNo())
                .append("unit", getUnit())
                .append("price", getPrice())
                .append("serviceDuration", getServiceDuration())
                .append("image", getImage())
                .append("nursingRequirement", getNursingRequirement())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
