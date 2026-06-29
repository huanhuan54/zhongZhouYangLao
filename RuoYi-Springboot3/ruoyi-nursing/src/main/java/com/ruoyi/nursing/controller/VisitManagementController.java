package com.ruoyi.nursing.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nursing.domain.VisitRecord;
import com.ruoyi.nursing.domain.VisitorAppointment;
import com.ruoyi.nursing.service.IElderCareService;

@RestController
@RequestMapping("/visit")
public class VisitManagementController extends BaseController
{
    @Autowired
    private IElderCareService elderCareService;

    @PreAuthorize("@ss.hasPermi('visit:appointment:list')")
    @GetMapping("/appointment/list")
    public TableDataInfo appointmentList(VisitorAppointment appointment)
    {
        startPage();
        List<VisitorAppointment> list = elderCareService.selectAppointmentList(appointment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('visit:appointment:add')")
    @Log(title = "预约登记", businessType = BusinessType.INSERT)
    @PostMapping("/appointment")
    public AjaxResult addAppointment(@RequestBody VisitorAppointment appointment)
    {
        appointment.setCreateBy(getUsername());
        return toAjax(elderCareService.insertAppointment(appointment));
    }

    @PreAuthorize("@ss.hasPermi('visit:appointment:arrive')")
    @Log(title = "预约到院", businessType = BusinessType.UPDATE)
    @PutMapping("/appointment/{id}/arrive")
    public AjaxResult arrive(@PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date arrivalTime)
    {
        return toAjax(elderCareService.arrive(id, arrivalTime, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('visit:appointment:edit')")
    @Log(title = "取消预约", businessType = BusinessType.UPDATE)
    @PutMapping("/appointment/{id}/cancel")
    public AjaxResult cancel(@PathVariable Long id)
    {
        return toAjax(elderCareService.cancel(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('visit:record:list')")
    @GetMapping("/record/list")
    public TableDataInfo recordList(VisitRecord record)
    {
        startPage();
        List<VisitRecord> list = elderCareService.selectVisitRecordList(record);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('visit:record:add')")
    @Log(title = "来访登记", businessType = BusinessType.INSERT)
    @PostMapping("/record")
    public AjaxResult addRecord(@RequestBody VisitRecord record)
    {
        record.setCreateBy(getUsername());
        return toAjax(elderCareService.insertVisitRecord(record));
    }
}
