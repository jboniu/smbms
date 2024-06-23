package com.ssm.service.impl;

import com.ssm.mapper.SmbmsProviderMapper;
import com.ssm.pojo.SmbmsProvider;
import com.ssm.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    SmbmsProviderMapper providerMapper;

    @Override
    public List<SmbmsProvider> getProviderList(String queryProName, String queryProCode) {
        return providerMapper.selectByTwo(queryProName,queryProCode);
    }

    @Override
    public SmbmsProvider getProviderById(String id) {
        return providerMapper.selectByPrimaryKey(Long.valueOf(id));
    }

    @Override
    public boolean modify(SmbmsProvider provider) {
        return providerMapper.updateByPrimaryKey_(provider) ==1;
    }

    @Override
    public int deleteProviderById(String id) {
        if(providerMapper.deleteByPrimaryKey(Long.valueOf(id))==1){
            return 0;
        }
        return -1;
    }

    @Override
    public boolean add(SmbmsProvider provider) {
        return providerMapper.insert_(provider)==1;
    }
}
