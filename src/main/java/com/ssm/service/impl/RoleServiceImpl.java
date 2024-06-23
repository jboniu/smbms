package com.ssm.service.impl;

import com.ssm.mapper.SmbmsRoleMapper;
import com.ssm.pojo.SmbmsRole;
import com.ssm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    SmbmsRoleMapper smbmsRoleMapper;

    @Override
    public List<SmbmsRole> getRolelist() {
        return smbmsRoleMapper.selectAll();
    }
}
