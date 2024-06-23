package com.ssm.service.impl;

import com.ssm.mapper.SmbmsUserMapper;
import com.ssm.pojo.SmbmsUser;
import com.ssm.pojo.SmbmsUserExample;
import com.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    SmbmsUserMapper smbmsUserMapper;


    @Override
    public List<SmbmsUser> loginFind(SmbmsUser smbmsUser) {
        SmbmsUserExample smbmsUserExample = new SmbmsUserExample();
        smbmsUserExample.createCriteria()
                .andUsercodeEqualTo(smbmsUser.getUsercode())
                .andUserpasswordEqualTo(smbmsUser.getUserpassword());
        return smbmsUserMapper.selectByExample(smbmsUserExample);
    }

    @Override
    public Boolean updatePwd(Long id, String password) {
        return smbmsUserMapper.updateById(id, password) == 1;
    }

    @Override
    public int getUserCount(String queryUserName, int queryUserRole) {
        return smbmsUserMapper.selectCount(queryUserName, queryUserRole);
    }

    @Override
    public List<SmbmsUser> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        List<SmbmsUser> userList = new ArrayList<>();
        currentPageNo = (currentPageNo - 1) * pageSize;
        return smbmsUserMapper.selectUserAndRoleByExample(queryUserName, queryUserRole, currentPageNo, pageSize);
    }

    @Override
    public boolean addUser(SmbmsUser user) {
        return smbmsUserMapper.insert(user) == 1;
    }

    @Override
    public SmbmsUser getUserById(String uid) {
        return smbmsUserMapper.selectByPrimaryKey(Long.valueOf(uid));
    }

    @Override
    public boolean modifyUser(SmbmsUser user) {
        return smbmsUserMapper.updateByKey(user) == 1;
    }

    @Override
    public boolean deleUser(int delId) {
        return smbmsUserMapper.deleteByPrimaryKey(Long.valueOf(delId)) == 1;
    }
}
