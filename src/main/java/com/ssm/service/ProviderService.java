package com.ssm.service;

import com.ssm.pojo.SmbmsProvider;

import java.util.List;

public interface ProviderService {
    List<SmbmsProvider> getProviderList(String queryProName, String queryProCode);

    SmbmsProvider getProviderById(String id);

    boolean modify(SmbmsProvider provider);

    int deleteProviderById(String id);

    boolean add(SmbmsProvider provider);
}
