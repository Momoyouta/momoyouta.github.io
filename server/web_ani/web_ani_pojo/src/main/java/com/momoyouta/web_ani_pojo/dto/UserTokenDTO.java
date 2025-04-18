package com.momoyouta.web_ani_pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDTO {
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
    private UserBaseInfoDTO userBaseInfo;
}
