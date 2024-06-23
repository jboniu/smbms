package com.ssm.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SmbmsUser {
    private Long id;

    private String usercode;

    private String username;

    private String userpassword;

    private Integer gender;

    private Date birthday;

    private String phone;

    private String address;

    private Long userrole;

    private Long createdby;

    private Date creationdate;

    private Long modifyby;

    private Date modifydate;
    private Integer age;    //年龄

}