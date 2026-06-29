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
import com.ruoyi.nursing.domain.CareOrder;
import com.ruoyi.nursing.domain.RefundOrder;
import com.ruoyi.nursing.service.ICareOrderService;

@RestController
@RequestMapping("/nursing/order")
public class CareOrderController extends BaseController
{
    @Autowired
    private ICareOrderService careOrderService;

    @PreAuthorize("@ss.hasPermi('nursing:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(CareOrder order)
    {
        startPage();
        List<CareOrder> list = careOrderService.selectCareOrderList(order);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('nursing:order:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(careOrderService.selectCareOrderById(id));
    }

    @PreAuthorize("@ss.hasPermi('nursing:order:cancel')")
    @Log(title = "取消护理订单", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/cancel")
    public AjaxResult cancel(@PathVariable Long id, @RequestBody Map<String, String> request)
    {
        return toAjax(careOrderService.cancelOrder(id, request.get("reason"), getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('nursing:order:refund')")
    @Log(title = "申请订单退款", businessType = BusinessType.INSERT)
    @PutMapping("/{id}/refund")
    public AjaxResult refund(@PathVariable Long id, @RequestBody Map<String, String> request)
    {
        return toAjax(careOrderService.applyRefund(id, request.get("reason"), getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('nursing:refund:list')")
    @GetMapping("/refund/list")
    public TableDataInfo refundList(RefundOrder refund)
    {
        startPage();
        return getDataTable(careOrderService.selectRefundOrderList(refund));
    }

    @PreAuthorize("@ss.hasPermi('nursing:refund:query')")
    @GetMapping("/refund/{id}")
    public AjaxResult refundInfo(@PathVariable Long id)
    {
        return success(careOrderService.selectRefundOrderById(id));
    }
}
