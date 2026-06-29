package com.ruoyi.nursing.service;

import java.util.List;
import com.ruoyi.nursing.domain.NursingPlan;

/**
 * Nursing plan service.
 */
public interface INursingPlanService
{
    List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan);

    NursingPlan selectNursingPlanById(Long id);

    int insertNursingPlan(NursingPlan nursingPlan);

    int updateNursingPlan(NursingPlan nursingPlan);

    int deleteNursingPlanById(Long id);

    List<NursingPlan> selectEnabledPlans();
}
