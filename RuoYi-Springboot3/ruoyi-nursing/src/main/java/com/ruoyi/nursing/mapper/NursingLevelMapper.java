package com.ruoyi.nursing.mapper;

import java.util.List;
import com.ruoyi.nursing.domain.NursingLevel;

/**
 * Nursing level mapper.
 */
public interface NursingLevelMapper
{
    List<NursingLevel> selectNursingLevelList(NursingLevel nursingLevel);

    NursingLevel selectNursingLevelById(Long id);

    int insertNursingLevel(NursingLevel nursingLevel);

    int updateNursingLevel(NursingLevel nursingLevel);

    int deleteNursingLevelById(Long id);

    List<NursingLevel> selectAllNursingLevels();
}
