package com.ruoyi.nursing.mapper;

import java.util.List;
import com.ruoyi.nursing.domain.NursingProject;
import com.ruoyi.nursing.domain.vo.NursingProjectVo;

/**
 * Nursing project mapper.
 */
public interface NursingProjectMapper
{
    List<NursingProject> selectNursingProjectList(NursingProject nursingProject);

    NursingProject selectNursingProjectById(Long id);

    int insertNursingProject(NursingProject nursingProject);

    int updateNursingProject(NursingProject nursingProject);

    int deleteNursingProjectById(Long id);

    List<NursingProjectVo> selectEnabledProjectOptions();
}
