package com.momoyouta.web_ani_server.service;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimationDetailVO;
import com.momoyouta.web_ani_pojo.VO.AnimeCard2VO;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.VO.AnimeKeywordSearchVO;
import com.momoyouta.web_ani_pojo.dto.AniAddDTO;
import com.momoyouta.web_ani_pojo.dto.DirCondition;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.entity.AnimeInfo;
import com.momoyouta.web_ani_pojo.entity.AnimeRating;

import java.util.List;

public interface AniService {

    Anime getById(Long id);


    public Result update(Anime anime);

    Result add(AniAddDTO aniAddDTO);

    void deleteByName(String name);

    List<Anime> getByNamelike(String name, int page, int pageSize);

    Anime getByName(String name);

    Integer getByNamelikeTotPage(String name);
    public Result addNoDTO(Anime anime) ;

    public Result addAnimeInfo(AnimeInfo animeInfo);
    public Result addAnimeRating(AnimeRating animeRating);

    public void addTag(Long animeId,List<String> tag);

    public AnimationDetailVO getAnimeDetail(Long animeId);

    public AnimeInfo getInfoByAnimeId(Long animeId);

    public AnimeRating getRatingByAnimeId(Long animeId);

    public Result<List<AnimeCardVO>> getByDirCondition(DirCondition dirCondition);

    List<AnimeKeywordSearchVO> searchAnimeByKeyword(String keyword,int offset,int pageSize);

    List<AnimeCard2VO> getRecentlyUpdate();

    Result<List<AnimeCardVO>> getCardByNamelike(String name, Integer page, Integer pageSize, boolean useDscp);
}
