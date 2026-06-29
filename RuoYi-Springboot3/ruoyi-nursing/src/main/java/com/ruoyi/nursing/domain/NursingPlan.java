package com.ruoyi.nursing.domain;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Nursing plan entity.
 */
public class NursingPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "排序号")
    private Integer sortNo;

    @Excel(name = "护理计划名称")
    private String planName;

    @Excel(name = "状态", readConverterExp = "0=禁用,1=启用")
    private Integer status;

    @Valid
    private List<NursingPlanProject> projectPlans;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotNull(message = "排序号不能为空")
    public Integer getSortNo()
    {
        return sortNo;
    }

    public void setSortNo(Integer sortNo)
    {
        this.sortNo = sortNo;
    }

    @NotBlank(message = "护理计划名称不能为空")
    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
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

    public List<NursingPlanProject> getProjectPlans()
    {
        return projectPlans;
    }

    public void setProjectPlans(List<NursingPlanProject> projectPlans)
    {
        this.projectPlans = projectPlans;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sortNo", getSortNo())
                .append("planName", getPlanName())
                .append("status", getStatus())
                .append("projectPlans", getProjectPlans())
                .append("remark", getRemark())
                .toString();
    }
}
