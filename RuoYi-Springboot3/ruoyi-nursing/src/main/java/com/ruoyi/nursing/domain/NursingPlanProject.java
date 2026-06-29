package com.ruoyi.nursing.domain;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Nursing plan-project relation.
 */
public class NursingPlanProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long planId;

    private Long projectId;

    private String projectName;

    private String executeTime;

    private Integer executeCycle;

    private Integer executeFrequency;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getPlanId()
    {
        return planId;
    }

    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    @NotNull(message = "护理项目不能为空")
    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    @NotNull(message = "执行时间不能为空")
    public String getExecuteTime()
    {
        return executeTime;
    }

    public void setExecuteTime(String executeTime)
    {
        this.executeTime = executeTime;
    }

    @NotNull(message = "执行周期不能为空")
    public Integer getExecuteCycle()
    {
        return executeCycle;
    }

    public void setExecuteCycle(Integer executeCycle)
    {
        this.executeCycle = executeCycle;
    }

    @NotNull(message = "执行频次不能为空")
    public Integer getExecuteFrequency()
    {
        return executeFrequency;
    }

    public void setExecuteFrequency(Integer executeFrequency)
    {
        this.executeFrequency = executeFrequency;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("planId", getPlanId())
                .append("projectId", getProjectId())
                .append("projectName", getProjectName())
                .append("executeTime", getExecuteTime())
                .append("executeCycle", getExecuteCycle())
                .append("executeFrequency", getExecuteFrequency())
                .append("remark", getRemark())
                .toString();
    }
}
