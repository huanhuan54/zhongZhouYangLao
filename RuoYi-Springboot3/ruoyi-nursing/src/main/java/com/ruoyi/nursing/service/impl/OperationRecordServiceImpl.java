package com.ruoyi.nursing.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.nursing.domain.OperationRecord;
import com.ruoyi.nursing.mapper.OperationRecordMapper;
import com.ruoyi.nursing.service.IOperationRecordService;

@Service
public class OperationRecordServiceImpl implements IOperationRecordService
{
    @Autowired
    private OperationRecordMapper mapper;

    @Override
    public List<OperationRecord> selectRecordList(OperationRecord record)
    {
        return mapper.selectRecordList(record);
    }

    @Override
    public OperationRecord selectRecordById(String module, Long id)
    {
        return mapper.selectRecordById(module, id);
    }

    @Override
    public int insertRecord(OperationRecord record, String username)
    {
        record.setCreateBy(username);
        record.setCreateTime(new Date());
        return mapper.insertRecord(record);
    }

    @Override
    public int updateRecord(OperationRecord record, String username)
    {
        record.setUpdateBy(username);
        record.setUpdateTime(new Date());
        return mapper.updateRecord(record);
    }

    @Override
    public int deleteRecord(String module, Long id)
    {
        return mapper.deleteRecord(module, id);
    }

    @Override
    public int updateStatus(String module, Long id, String status, String username)
    {
        return mapper.updateStatus(module, id, status, username);
    }
}
