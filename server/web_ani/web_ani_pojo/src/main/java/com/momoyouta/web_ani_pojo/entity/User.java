package com.momoyouta.web_ani_pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String account;
    private String password;
    private String email;
    private String phone;
    private String wxid;
    private String avatarUrl;
    private String signature;
    private Long birthday;
    private int sex;
    private int status;
}
