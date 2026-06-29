package com.ruoyi.nursing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.nursing.domain.CareOrder;

public interface CareOrderMapper
{
    List<CareOrder> selectCareOrderList(CareOrder order);

    CareOrder selectCareOrderById(Long id);

    int cancelOrder(@Param("id") Long id, @Param("reason") String reason, @Param("username") String username);

    int markRefunding(@Param("id") Long id, @Param("username") String username);
}
