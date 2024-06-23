package com.ssm.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SmbmsBill {
    private Long id;

    private String billcode;

    private String productname;

    private String productdesc;

    private String productunit;

    private BigDecimal productcount;

    private BigDecimal totalprice;

    private Integer ispayment;

    private Long createdby;

    private Date creationdate;

    private Long modifyby;

    private Date modifydate;

    private Long providerid;

    private SmbmsProvider provider;

    private String providername;//供应商名称
}