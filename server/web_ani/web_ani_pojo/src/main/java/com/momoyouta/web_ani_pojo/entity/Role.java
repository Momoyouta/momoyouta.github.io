package com.momoyouta.web_ani_pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private int id;
    private String roleNameEn;
    private String roleNameCn;
}
