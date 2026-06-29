package com.ruoyi.nursing.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nursing.domain.NursingTask;
import com.ruoyi.nursing.service.INursingTaskService;

@RestController
@RequestMapping("/nursing/task")
public class NursingTaskController extends BaseController
{
    @Autowired
    private INursingTaskService nursingTaskService;

    @PreAuthorize("@ss.hasPermi('nursing:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(NursingTask task)
    {
        startPage();
        List<NursingTask> list = nursingTaskService.selectNursingTaskList(task);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('nursing:task:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(nursingTaskService.selectNursingTaskById(id));
    }

    @PreAuthorize("@ss.hasPermi('nursing:task:execute')")
    @Log(title = "护理任务执行", businessType = BusinessType.UPDATE)
    @PutMapping("/execute")
    public AjaxResult execute(@RequestBody NursingTask task)
    {
        return toAjax(nursingTaskService.executeTask(task, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('nursing:task:cancel')")
    @Log(title = "护理任务取消", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel")
    public AjaxResult cancel(@RequestBody NursingTask task)
    {
        return toAjax(nursingTaskService.cancelTask(task, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('nursing:task:edit')")
    @Log(title = "护理任务改期", businessType = BusinessType.UPDATE)
    @PutMapping("/reschedule")
    public AjaxResult reschedule(@RequestBody NursingTask task)
    {
        return toAjax(nursingTaskService.rescheduleTask(task, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('nursing:task:edit')")
    @Log(title = "更换护理员", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/caregiver")
    public AjaxResult changeCaregiver(@PathVariable Long id, @RequestBody Map<String, Long> request)
    {
        return toAjax(nursingTaskService.changeCaregiver(id, request.get("caregiverId"), getUsername()));
    }
}
