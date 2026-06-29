package com.ruoyi.nursing.service;

import java.util.List;
import com.ruoyi.nursing.domain.NursingTask;

public interface INursingTaskService
{
    List<NursingTask> selectNursingTaskList(NursingTask task);

    NursingTask selectNursingTaskById(Long id);

    int executeTask(NursingTask task, String username);

    int cancelTask(NursingTask task, String username);

    int rescheduleTask(NursingTask task, String username);

    int changeCaregiver(Long id, Long caregiverId, String username);
}
