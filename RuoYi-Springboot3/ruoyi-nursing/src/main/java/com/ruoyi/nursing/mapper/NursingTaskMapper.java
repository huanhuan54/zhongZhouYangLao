package com.ruoyi.nursing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.nursing.domain.NursingTask;

public interface NursingTaskMapper
{
    List<NursingTask> selectNursingTaskList(NursingTask task);

    NursingTask selectNursingTaskById(Long id);

    int executeTask(NursingTask task);

    int cancelTask(NursingTask task);

    int rescheduleTask(NursingTask task);

    int changeCaregiver(@Param("id") Long id, @Param("caregiverId") Long caregiverId,
            @Param("updateBy") String updateBy);
}
