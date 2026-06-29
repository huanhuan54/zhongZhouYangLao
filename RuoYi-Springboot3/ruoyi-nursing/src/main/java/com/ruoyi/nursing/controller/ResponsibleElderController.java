package com.ruoyi.nursing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nursing.domain.CaregiverAssignment;
import com.ruoyi.nursing.service.IElderCareService;

@RestController
@RequestMapping("/nursing/responsible")
public class ResponsibleElderController extends BaseController
{
    @Autowired
    private IElderCareService elderCareService;

    @PreAuthorize("@ss.hasPermi('nursing:responsible:list')")
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false) Integer floorNo,
            @RequestParam(required = false) String elderName)
    {
        return success(elderCareService.selectResponsibleResidents(floorNo, elderName));
    }

    @GetMapping("/caregivers")
    public AjaxResult caregivers()
    {
        return success(elderCareService.selectCaregivers());
    }

    @PreAuthorize("@ss.hasPermi('nursing:responsible:edit')")
    @Log(title = "设置护理员", businessType = BusinessType.UPDATE)
    @PostMapping("/assign")
    public AjaxResult assign(@RequestBody CaregiverAssignment assignment)
    {
        return toAjax(elderCareService.assignCaregivers(assignment, getUsername()));
    }

    @GetMapping("/dashboard")
    public AjaxResult dashboard()
    {
        return success(elderCareService.selectDashboardSummary());
    }
}
