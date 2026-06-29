package com.ruoyi.nursing.service;

import java.util.List;
import com.ruoyi.nursing.domain.NursingProject;
import com.ruoyi.nursing.domain.vo.NursingProjectVo;

/**
 * Nursing project service.
 */
public interface INursingProjectService
{
    List<NursingProject> selectNursingProjectList(NursingProject nursingProject);

    NursingProject selectNursingProjectById(Long id);

    int insertNursingProject(NursingProject nursingProject);

    int updateNursingProject(NursingProject nursingProject);

    int deleteNursingProjectById(Long id);

    List<NursingProjectVo> selectEnabledProjectOptions();
}
