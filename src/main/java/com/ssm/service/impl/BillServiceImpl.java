package com.ssm.service.impl;

import com.ssm.mapper.SmbmsBillMapper;
import com.ssm.pojo.SmbmsBill;
import com.ssm.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    SmbmsBillMapper smbmsBillMapper;
    @Override
    public List<SmbmsBill> getBillList(SmbmsBill bill) {
        return smbmsBillMapper.selectBillAndPro(bill);
    }

    @Override
    public SmbmsBill getBillById(String id) {
        return smbmsBillMapper.selectByPrimaryKey(Long.valueOf(id));
    }

    @Override
    public boolean add(SmbmsBill bill) {
        return smbmsBillMapper.insert_(bill)==1;
    }

    @Override
    public boolean modify(SmbmsBill bill) {
        return smbmsBillMapper.updateByBill(bill)==1;
    }

    @Override
    public boolean deleteBillById(String id) {
        return smbmsBillMapper.deleteByPrimaryKey(Long.valueOf(id))==1;
    }
}
