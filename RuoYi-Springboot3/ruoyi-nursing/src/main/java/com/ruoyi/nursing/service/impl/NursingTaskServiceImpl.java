package com.ruoyi.nursing.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.nursing.domain.NursingTask;
import com.ruoyi.nursing.mapper.NursingTaskMapper;
import com.ruoyi.nursing.service.INursingTaskService;

@Service
public class NursingTaskServiceImpl implements INursingTaskService
{
    @Autowired
    private NursingTaskMapper nursingTaskMapper;

    @Override
    public List<NursingTask> selectNursingTaskList(NursingTask task)
    {
        return nursingTaskMapper.selectNursingTaskList(task);
    }

    @Override
    public NursingTask selectNursingTaskById(Long id)
    {
        return nursingTaskMapper.selectNursingTaskById(id);
    }

    @Override
    public int executeTask(NursingTask task, String username)
    {
        task.setStatus(1);
        task.setUpdateBy(username);
        if (task.getExecuteTime() == null)
        {
            task.setExecuteTime(new Date());
        }
        return nursingTaskMapper.executeTask(task);
    }

    @Override
    public int cancelTask(NursingTask task, String username)
    {
        task.setStatus(2);
        task.setUpdateBy(username);
        task.setCancelTime(new Date());
        return nursingTaskMapper.cancelTask(task);
    }

    @Override
    public int rescheduleTask(NursingTask task, String username)
    {
        task.setUpdateBy(username);
        return nursingTaskMapper.rescheduleTask(task);
    }

    @Override
    public int changeCaregiver(Long id, Long caregiverId, String username)
    {
        return nursingTaskMapper.changeCaregiver(id, caregiverId, username);
    }
}
