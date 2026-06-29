package com.ruoyi.nursing.service;

import java.util.List;
import com.ruoyi.nursing.domain.OperationRecord;

public interface IOperationRecordService
{
    List<OperationRecord> selectRecordList(OperationRecord record);

    OperationRecord selectRecordById(String module, Long id);

    int insertRecord(OperationRecord record, String username);

    int updateRecord(OperationRecord record, String username);

    int deleteRecord(String module, Long id);

    int updateStatus(String module, Long id, String status, String username);
}
