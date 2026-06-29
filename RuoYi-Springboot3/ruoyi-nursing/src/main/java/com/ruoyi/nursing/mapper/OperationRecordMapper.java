package com.ruoyi.nursing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.nursing.domain.OperationRecord;

public interface OperationRecordMapper
{
    List<OperationRecord> selectRecordList(OperationRecord record);

    OperationRecord selectRecordById(@Param("module") String module, @Param("id") Long id);

    int insertRecord(OperationRecord record);

    int updateRecord(OperationRecord record);

    int deleteRecord(@Param("module") String module, @Param("id") Long id);

    int updateStatus(@Param("module") String module, @Param("id") Long id,
            @Param("status") String status, @Param("username") String username);
}
