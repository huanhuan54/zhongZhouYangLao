package com.ruoyi.nursing.domain;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Nursing level entity.
 */
public class NursingLevel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "护理等级名称")
    private String name;

    @Excel(name = "护理计划ID")
    private Long lplanId;

    @Excel(name = "护理计划")
    private String planName;

    @Excel(name = "护理费用")
    private BigDecimal fee;

    @Excel(name = "状态", readConverterExp = "0=禁用,1=启用")
    private Integer status;

    @Excel(name = "等级说明")
    private String description;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "护理等级名称不能为空")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @NotNull(message = "护理计划不能为空")
    public Long getLplanId()
    {
        return lplanId;
    }

    public void setLplanId(Long lplanId)
    {
        this.lplanId = lplanId;
    }

    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    @NotNull(message = "护理费用不能为空")
    public BigDecimal getFee()
    {
        return fee;
    }

    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("lplanId", getLplanId())
                .append("planName", getPlanName())
                .append("fee", getFee())
                .append("status", getStatus())
                .append("description", getDescription())
                .append("remark", getRemark())
                .toString();
    }
}
