package com.ruoyi.nursing.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nursing.domain.NursingPlan;
import com.ruoyi.nursing.service.INursingPlanService;

/**
 * Nursing plan controller.
 */
@RestController
@RequestMapping("/nursing/plan")
public class NursingPlanController extends BaseController
{
    @Autowired
    private INursingPlanService nursingPlanService;

    @PreAuthorize("@ss.hasPermi('nursing:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(NursingPlan nursingPlan)
    {
        startPage();
        List<NursingPlan> list = nursingPlanService.selectNursingPlanList(nursingPlan);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('nursing:plan:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(nursingPlanService.selectNursingPlanById(id));
    }

    @PreAuthorize("@ss.hasPermi('nursing:plan:add')")
    @Log(title = "护理计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody NursingPlan nursingPlan)
    {
        nursingPlan.setCreateBy(getUsername());
        return toAjax(nursingPlanService.insertNursingPlan(nursingPlan));
    }

    @PreAuthorize("@ss.hasPermi('nursing:plan:edit')")
    @Log(title = "护理计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody NursingPlan nursingPlan)
    {
        nursingPlan.setUpdateBy(getUsername());
        return toAjax(nursingPlanService.updateNursingPlan(nursingPlan));
    }

    @PreAuthorize("@ss.hasPermi('nursing:plan:remove')")
    @Log(title = "护理计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(nursingPlanService.deleteNursingPlanById(id));
    }

    @GetMapping("/all")
    public AjaxResult all()
    {
        return success(nursingPlanService.selectEnabledPlans());
    }
}
