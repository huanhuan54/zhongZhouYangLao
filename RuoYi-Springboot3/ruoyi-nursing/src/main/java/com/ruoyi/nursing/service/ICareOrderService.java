package com.ruoyi.nursing.service;

import java.util.List;
import com.ruoyi.nursing.domain.CareOrder;
import com.ruoyi.nursing.domain.RefundOrder;

public interface ICareOrderService
{
    List<CareOrder> selectCareOrderList(CareOrder order);

    CareOrder selectCareOrderById(Long id);

    int cancelOrder(Long id, String reason, String username);

    int applyRefund(Long id, String reason, String username);

    List<RefundOrder> selectRefundOrderList(RefundOrder refund);

    RefundOrder selectRefundOrderById(Long id);
}
