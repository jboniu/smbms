package com.ssm.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SmbmsAddress {
    private Long id;

    private String contact;

    private String addressdesc;

    private String postcode;

    private String tel;

    private Long createdby;

    private Date creationdate;

    private Long modifyby;

    private Date modifydate;

    private Long userid;
}