package com.momoyouta.web_ani_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.VO.AnimeFavoriteCardVO;
import com.momoyouta.web_ani_pojo.dto.RegisterDTO;
import com.momoyouta.web_ani_pojo.dto.UserBaseInfoDTO;
import com.momoyouta.web_ani_pojo.dto.UserTokenDTO;
import com.momoyouta.web_ani_pojo.dto.FavoriteAnimeConditionDTO;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    Result<String> registerByAccount(RegisterDTO registerDTO);

    Result<UserTokenDTO> loginByAccount(String account, String password) throws JsonProcessingException;

    UserBaseInfoDTO getBaseInfo(String userId);

    Result<String> updateUserInfo(UserBaseInfoDTO info);

    Result<String> unFollowAnimeByList(List<Long> animeIds);

    Result<String> followAnimeByList(List<Long> animeIds);

    Result<String> score(Long animeId, BigDecimal score);

    Result<String> favorite(Long animeId);

    Result<String> unFavorite(Long animeId);

    Result<String> updateFollowProcess(Long animeId, int process);

    Result<String> finishFollow(Long animeId);

    Result<Boolean> getFavoriteStatus(Long animeId);

    Result<List<AnimeCardVO>> getFavoriteAnimes(String userId);

    Result<List<AnimeFavoriteCardVO>> getFavoriteAnimesByCondition(int offset, int pageSize, FavoriteAnimeConditionDTO condition);

    Result<String> updateWatchStatus(Long animeId, int status);
}
