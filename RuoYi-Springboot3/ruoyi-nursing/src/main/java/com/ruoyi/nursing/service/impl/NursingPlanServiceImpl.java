package com.ruoyi.nursing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.nursing.domain.NursingPlan;
import com.ruoyi.nursing.domain.NursingPlanProject;
import com.ruoyi.nursing.mapper.NursingPlanMapper;
import com.ruoyi.nursing.service.INursingPlanService;

/**
 * Nursing plan service implementation.
 */
@Service
public class NursingPlanServiceImpl implements INursingPlanService
{
    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan)
    {
        return nursingPlanMapper.selectNursingPlanList(nursingPlan);
    }

    @Override
    public NursingPlan selectNursingPlanById(Long id)
    {
        NursingPlan nursingPlan = nursingPlanMapper.selectNursingPlanById(id);
        if (nursingPlan != null)
        {
            nursingPlan.setProjectPlans(nursingPlanMapper.selectProjectPlansByPlanId(id));
        }
        return nursingPlan;
    }

    @Override
    @Transactional
    public int insertNursingPlan(NursingPlan nursingPlan)
    {
        int rows = nursingPlanMapper.insertNursingPlan(nursingPlan);
        insertProjectPlans(nursingPlan);
        return rows;
    }

    @Override
    @Transactional
    public int updateNursingPlan(NursingPlan nursingPlan)
    {
        nursingPlanMapper.deleteProjectPlansByPlanId(nursingPlan.getId());
        insertProjectPlans(nursingPlan);
        return nursingPlanMapper.updateNursingPlan(nursingPlan);
    }

    @Override
    @Transactional
    public int deleteNursingPlanById(Long id)
    {
        nursingPlanMapper.deleteProjectPlansByPlanId(id);
        return nursingPlanMapper.deleteNursingPlanById(id);
    }

    @Override
    public List<NursingPlan> selectEnabledPlans()
    {
        return nursingPlanMapper.selectEnabledPlans();
    }

    private void insertProjectPlans(NursingPlan nursingPlan)
    {
        List<NursingPlanProject> projectPlans = nursingPlan.getProjectPlans();
        if (projectPlans == null || projectPlans.isEmpty())
        {
            return;
        }
        for (NursingPlanProject projectPlan : projectPlans)
        {
            projectPlan.setPlanId(nursingPlan.getId());
            projectPlan.setCreateBy(nursingPlan.getCreateBy() == null ? nursingPlan.getUpdateBy() : nursingPlan.getCreateBy());
        }
        nursingPlanMapper.batchInsertProjectPlans(projectPlans);
    }
}
