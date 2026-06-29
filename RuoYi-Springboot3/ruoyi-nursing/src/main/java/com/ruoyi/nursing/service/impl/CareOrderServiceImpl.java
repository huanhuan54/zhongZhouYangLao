package com.ruoyi.nursing.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.nursing.domain.CareOrder;
import com.ruoyi.nursing.domain.RefundOrder;
import com.ruoyi.nursing.mapper.CareOrderMapper;
import com.ruoyi.nursing.mapper.RefundOrderMapper;
import com.ruoyi.nursing.service.ICareOrderService;

@Service
public class CareOrderServiceImpl implements ICareOrderService
{
    @Autowired
    private CareOrderMapper careOrderMapper;

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Override
    public List<CareOrder> selectCareOrderList(CareOrder order)
    {
        return careOrderMapper.selectCareOrderList(order);
    }

    @Override
    public CareOrder selectCareOrderById(Long id)
    {
        return careOrderMapper.selectCareOrderById(id);
    }

    @Override
    public int cancelOrder(Long id, String reason, String username)
    {
        CareOrder order = careOrderMapper.selectCareOrderById(id);
        if (order == null || order.getOrderStatus() == null || order.getOrderStatus() != 0)
        {
            throw new ServiceException("只有待支付订单可以取消");
        }
        return careOrderMapper.cancelOrder(id, reason, username);
    }

    @Override
    @Transactional
    public int applyRefund(Long id, String reason, String username)
    {
        CareOrder order = careOrderMapper.selectCareOrderById(id);
        if (order == null || order.getPaymentStatus() == null || order.getPaymentStatus() != 1)
        {
            throw new ServiceException("订单尚未支付，不能申请退款");
        }
        if (order.getOrderStatus() == null || (order.getOrderStatus() != 1 && order.getOrderStatus() != 2))
        {
            throw new ServiceException("当前订单状态不能申请退款");
        }
        if (order.getRefundStatus() != null && order.getRefundStatus() != 0)
        {
            throw new ServiceException("该订单已有退款记录");
        }

        RefundOrder refund = new RefundOrder();
        refund.setRefundNo("TK" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        refund.setOrderId(order.getId());
        refund.setOrderNo(order.getOrderNo());
        refund.setRefundAmount(order.getOrderAmount());
        refund.setApplicant(username);
        refund.setApplicantType("后台用户");
        refund.setApplyTime(new Date());
        refund.setRefundReason(reason);
        refund.setRefundChannel("原路退回");
        refund.setRefundMethod(order.getPayChannel());
        refund.setRefundStatus(1);
        refund.setCreateBy(username);
        refund.setCreateTime(new Date());
        refundOrderMapper.insertRefundOrder(refund);
        return careOrderMapper.markRefunding(id, username);
    }

    @Override
    public List<RefundOrder> selectRefundOrderList(RefundOrder refund)
    {
        return refundOrderMapper.selectRefundOrderList(refund);
    }

    @Override
    public RefundOrder selectRefundOrderById(Long id)
    {
        return refundOrderMapper.selectRefundOrderById(id);
    }
}
