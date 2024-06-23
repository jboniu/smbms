package com.ssm.service;

import com.ssm.pojo.SmbmsUser;

import java.util.List;

public interface UserService {
    List<SmbmsUser> loginFind(SmbmsUser smbmsUser);

    Boolean updatePwd(Long id, String password);

    int getUserCount(String queryUserName, int queryUserRole);

    List<SmbmsUser> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    boolean addUser(SmbmsUser user);

    SmbmsUser getUserById(String uid);

    boolean modifyUser(SmbmsUser user);

    boolean deleUser(int delId);
}
