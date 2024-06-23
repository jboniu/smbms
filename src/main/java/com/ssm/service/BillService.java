package com.ssm.service;

import com.ssm.pojo.SmbmsBill;

import java.util.List;

public interface BillService {
    List<SmbmsBill> getBillList(SmbmsBill bill);

    SmbmsBill getBillById(String id);

    boolean add(SmbmsBill bill);

    boolean modify(SmbmsBill bill);

    boolean deleteBillById(String id);
}
