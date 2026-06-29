package com.ruoyi.nursing.mapper;

import java.util.List;
import com.ruoyi.nursing.domain.RefundOrder;

public interface RefundOrderMapper
{
    List<RefundOrder> selectRefundOrderList(RefundOrder refund);

    RefundOrder selectRefundOrderById(Long id);

    int insertRefundOrder(RefundOrder refund);
}
