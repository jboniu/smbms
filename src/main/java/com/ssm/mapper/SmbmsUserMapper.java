package com.ssm.mapper;

import com.ssm.pojo.SmbmsUser;
import com.ssm.pojo.SmbmsUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmbmsUserMapper {
    long countByExample(SmbmsUserExample example);

    int deleteByExample(SmbmsUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmbmsUser record);

    int insertSelective(SmbmsUser record);

    List<SmbmsUser> selectByExample(SmbmsUserExample example);

    List<SmbmsUser> selectUserAndRoleByExample(@Param("queryUserName")String queryUserName,@Param("queryUserRole")int queryUserRole,@Param("currentPageNo")int currentPageNo,@Param("pageSize")int pageSize);

    SmbmsUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmbmsUser record, @Param("example") SmbmsUserExample example);

    int updateByExample(@Param("record") SmbmsUser record, @Param("example") SmbmsUserExample example);

    int updateByPrimaryKeySelective(SmbmsUser record);

    int updateByPrimaryKey(SmbmsUser record);

    Integer selectCount(@Param("userName") String queryUserName, @Param("userRole") int queryUserRole);

    int updateById(@Param("id")Long id, @Param("password") String password);//

    int updateByKey(SmbmsUser user);
}