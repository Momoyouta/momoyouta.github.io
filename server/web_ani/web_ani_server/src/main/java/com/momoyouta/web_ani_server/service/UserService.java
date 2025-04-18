package com.momoyouta.web_ani_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.dto.RegisterDTO;
import com.momoyouta.web_ani_pojo.dto.UserBaseInfoDTO;
import com.momoyouta.web_ani_pojo.dto.UserTokenDTO;
import org.springframework.stereotype.Service;

public interface UserService {
    Result<String> registerByAccount(RegisterDTO registerDTO);

    Result<UserTokenDTO> loginByAccount(String account, String password) throws JsonProcessingException;

    UserBaseInfoDTO getBaseInfo(String userId);
}
