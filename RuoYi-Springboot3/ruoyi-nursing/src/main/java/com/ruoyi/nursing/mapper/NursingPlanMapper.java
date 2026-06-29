package com.ruoyi.nursing.mapper;

import java.util.List;
import com.ruoyi.nursing.domain.NursingPlan;
import com.ruoyi.nursing.domain.NursingPlanProject;

/**
 * Nursing plan mapper.
 */
public interface NursingPlanMapper
{
    List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan);

    NursingPlan selectNursingPlanById(Long id);

    List<NursingPlanProject> selectProjectPlansByPlanId(Long planId);

    int insertNursingPlan(NursingPlan nursingPlan);

    int updateNursingPlan(NursingPlan nursingPlan);

    int deleteNursingPlanById(Long id);

    int deleteProjectPlansByPlanId(Long planId);

    int batchInsertProjectPlans(List<NursingPlanProject> projectPlans);

    List<NursingPlan> selectEnabledPlans();
}
