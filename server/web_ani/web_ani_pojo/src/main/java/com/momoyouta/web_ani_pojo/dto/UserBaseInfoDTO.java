package com.momoyouta.web_ani_pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseInfoDTO {
    private String id;
    private String name;
    private String account;
    private int sex;
    private Long birthday;
    private String avatarUrl;
    private String email;
    private String signature;
}
