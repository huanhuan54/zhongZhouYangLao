package com.ruoyi.nursing.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ruoyi.nursing.domain.OperationRecord;
import com.ruoyi.nursing.service.IOperationRecordService;

@RestController
@RequestMapping("/operations")
public class OperationRecordController extends BaseController
{
    @Autowired
    private IOperationRecordService service;

    @PreAuthorize("@ss.hasPermi('operations:record:list')")
    @GetMapping("/{module}/list")
    public TableDataInfo list(@PathVariable String module, OperationRecord record)
    {
        record.setModule(module);
        startPage();
        List<OperationRecord> list = service.selectRecordList(record);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('operations:record:query')")
    @GetMapping("/{module}/{id}")
    public AjaxResult getInfo(@PathVariable String module, @PathVariable Long id)
    {
        return success(service.selectRecordById(module, id));
    }

    @PreAuthorize("@ss.hasPermi('operations:record:edit')")
    @Log(title = "运营业务记录新增", businessType = BusinessType.INSERT)
    @PostMapping("/{module}")
    public AjaxResult add(@PathVariable String module, @RequestBody OperationRecord record)
    {
        record.setModule(module);
        return toAjax(service.insertRecord(record, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('operations:record:edit')")
    @Log(title = "运营业务记录修改", businessType = BusinessType.UPDATE)
    @PutMapping("/{module}")
    public AjaxResult edit(@PathVariable String module, @RequestBody OperationRecord record)
    {
        record.setModule(module);
        return toAjax(service.updateRecord(record, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('operations:record:edit')")
    @Log(title = "运营业务状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/{module}/{id}/status")
    public AjaxResult status(@PathVariable String module, @PathVariable Long id,
            @RequestBody Map<String, String> request)
    {
        return toAjax(service.updateStatus(module, id, request.get("status"), getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('operations:record:remove')")
    @Log(title = "运营业务记录删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{module}/{id}")
    public AjaxResult remove(@PathVariable String module, @PathVariable Long id)
    {
        return toAjax(service.deleteRecord(module, id));
    }
}
