package com.ssm.mapper;

import com.ssm.pojo.SmbmsBill;
import com.ssm.pojo.SmbmsBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmbmsBillMapper {
//    long countByExample(SmbmsBillExample example);

//    int deleteByExample(SmbmsBillExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmbmsBill record);

//    int insertSelective(SmbmsBill record);

    List<SmbmsBill> selectByExample(SmbmsBillExample example);

    SmbmsBill selectByPrimaryKey(Long id);

//    int updateByExampleSelective(@Param("record") SmbmsBill record, @Param("example") SmbmsBillExample example);
//
//    int updateByExample(@Param("record") SmbmsBill record, @Param("example") SmbmsBillExample example);
//
//    int updateByPrimaryKeySelective(SmbmsBill record);
//
//    int updateByPrimaryKey(SmbmsBill record);

    List<SmbmsBill> selectBillAndPro(SmbmsBill bill);

    int insert_(SmbmsBill bill);

    int updateByBill(SmbmsBill bill);
}