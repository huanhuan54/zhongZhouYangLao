package com.ruoyi.nursing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.nursing.domain.NursingLevel;
import com.ruoyi.nursing.mapper.NursingLevelMapper;
import com.ruoyi.nursing.service.INursingLevelService;

/**
 * Nursing level service implementation.
 */
@Service
public class NursingLevelServiceImpl implements INursingLevelService
{
    @Autowired
    private NursingLevelMapper nursingLevelMapper;

    @Override
    public List<NursingLevel> selectNursingLevelList(NursingLevel nursingLevel)
    {
        return nursingLevelMapper.selectNursingLevelList(nursingLevel);
    }

    @Override
    public NursingLevel selectNursingLevelById(Long id)
    {
        return nursingLevelMapper.selectNursingLevelById(id);
    }

    @Override
    public int insertNursingLevel(NursingLevel nursingLevel)
    {
        return nursingLevelMapper.insertNursingLevel(nursingLevel);
    }

    @Override
    public int updateNursingLevel(NursingLevel nursingLevel)
    {
        return nursingLevelMapper.updateNursingLevel(nursingLevel);
    }

    @Override
    public int deleteNursingLevelById(Long id)
    {
        return nursingLevelMapper.deleteNursingLevelById(id);
    }

    @Override
    public List<NursingLevel> selectAllNursingLevels()
    {
        return nursingLevelMapper.selectAllNursingLevels();
    }
}
