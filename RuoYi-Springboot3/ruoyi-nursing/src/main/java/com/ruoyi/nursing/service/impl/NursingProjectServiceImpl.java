package com.ruoyi.nursing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.nursing.domain.NursingProject;
import com.ruoyi.nursing.domain.vo.NursingProjectVo;
import com.ruoyi.nursing.mapper.NursingProjectMapper;
import com.ruoyi.nursing.service.INursingProjectService;

/**
 * Nursing project service implementation.
 */
@Service
public class NursingProjectServiceImpl implements INursingProjectService
{
    @Autowired
    private NursingProjectMapper nursingProjectMapper;

    @Override
    public List<NursingProject> selectNursingProjectList(NursingProject nursingProject)
    {
        return nursingProjectMapper.selectNursingProjectList(nursingProject);
    }

    @Override
    public NursingProject selectNursingProjectById(Long id)
    {
        return nursingProjectMapper.selectNursingProjectById(id);
    }

    @Override
    public int insertNursingProject(NursingProject nursingProject)
    {
        return nursingProjectMapper.insertNursingProject(nursingProject);
    }

    @Override
    public int updateNursingProject(NursingProject nursingProject)
    {
        return nursingProjectMapper.updateNursingProject(nursingProject);
    }

    @Override
    public int deleteNursingProjectById(Long id)
    {
        return nursingProjectMapper.deleteNursingProjectById(id);
    }

    @Override
    public List<NursingProjectVo> selectEnabledProjectOptions()
    {
        return nursingProjectMapper.selectEnabledProjectOptions();
    }
}
