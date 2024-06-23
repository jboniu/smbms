package com.ssm.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SmbmsRole {
    private Long id;

    private String rolecode;

    private String rolename;

    private Long createdby;

    private Date creationdate;

    private Long modifyby;

    private Date modifydate;


}