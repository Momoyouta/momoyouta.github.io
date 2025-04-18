package com.momoyouta.web_ani_pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_role")
public class UserRole {
    private String id;
    private String userId;
    private int roleId;
}
