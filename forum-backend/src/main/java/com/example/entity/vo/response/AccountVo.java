package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVo {

    private String username;
    private String email;
    private String role;
    private Date registerTime;
}
