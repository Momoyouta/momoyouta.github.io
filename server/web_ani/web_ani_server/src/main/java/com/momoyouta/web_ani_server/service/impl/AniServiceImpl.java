package com.momoyouta.web_ani_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.momoyouta.web_ani_common.enumm.ResponseCode;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimationDetailVO;
import com.momoyouta.web_ani_pojo.VO.AnimeCard2VO;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.VO.AnimeKeywordSearchVO;
import com.momoyouta.web_ani_pojo.dto.AniAddDTO;
import com.momoyouta.web_ani_pojo.dto.DirCondition;
import com.momoyouta.web_ani_pojo.entity.*;
import com.momoyouta.web_ani_server.mapper.*;
import com.momoyouta.web_ani_server.service.AniService;
import com.momoyouta.web_ani_server.service.EpisodeService;
import com.momoyouta.web_ani_server.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AniServiceImpl implements AniService {

    @Autowired
    private AnimeMapper animeMapper;

    @Autowired
    private AnimeInfoMapper infoMapper;

    @Autowired
    private AnimeRatingMapper ratingMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private AnimeAndTagsMapper animeAndTagsMapper;

    @Autowired
    private EpisodeService episodeService;


    @Override
    public Anime getById(Long id) {
        return animeMapper.getById(id);
    }


    @Override
    public Result update(Anime anime){
        LambdaUpdateWrapper<Anime> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        anime.setUpdateTime(LocalDateTime.now().withNano(0).withSecond(0));
        log.info(animeMapper.update(anime,
                lambdaUpdateWrapper.eq(Anime::getId, anime.getId()))+"");
        return Result.success("更新成功");
    }

    @Override
    public Result add(AniAddDTO aniAddDTO) {
        Anime anime =new Anime();
        BeanUtils.copyProperties(aniAddDTO, anime);
        anime.setUpdateTime(LocalDateTime.now().withNano(0).withSecond(0));
        anime.setBan(1);
        if(animeMapper.getByName(anime.getName())!=null){
            return Result.error(ResponseCode.ERROR,"番剧已存在");
        }
        animeMapper.insert(anime);
        return Result.success("添加成功");
    }

    @Override
    public Result addNoDTO(Anime anime) {
        anime.setUpdateTime(LocalDateTime.now().withNano(0).withSecond(0));
        anime.setBan(0);
        if(animeMapper.getByName(anime.getName())!=null){
            return Result.error(ResponseCode.ERROR,"番剧已存在");
        }
        animeMapper.insert(anime);
        return Result.success("添加成功");
    }

    @Override
    public List<Anime> getByNamelike(String name, int page, int pageSize) {
        page=(page-1)*pageSize;
        List<Anime> list= animeMapper.getByNamelike(name,page,pageSize);
        return list;
    }

    @Override
    public Anime getByName(String name){
        return animeMapper.getByName(name);
    }

    @Override
    public Integer getByNamelikeTotPage(String name) {
        int pages= animeMapper.getByNamelikeTotPage(name);
        return pages;
    }

    @Override
    public void deleteByName(String name){

        animeMapper.deleteByName(name);
    }

    @Override
    public Result addAnimeInfo(AnimeInfo animeInfo) {
        infoMapper.insertOrUpdate(animeInfo);
        return Result.success();
    }

    @Override
    public Result addAnimeRating(AnimeRating animeRating) {
        ratingMapper.insertOrUpdate(animeRating);
        return Result.success();
    }

    @Override
    public void addTag(Long animeId, List<String> tag) {
        for(String i :tag){
            Long tagId= tagService.getTagByName(i).getId();
            AnimeAtags atags=new AnimeAtags(null,tagId,animeId);
            animeAndTagsMapper.insertOrUpdate(atags);
        }
    }
    @Override
    public AnimeInfo getInfoByAnimeId(Long animeId) {
        LambdaQueryWrapper<AnimeInfo> qw=new LambdaQueryWrapper<>();
        qw.eq(AnimeInfo::getAnimeId,animeId);
        return infoMapper.selectOne(qw);
    }

    @Override
    public AnimeRating getRatingByAnimeId(Long animeId) {
        LambdaQueryWrapper<AnimeRating> qw=new LambdaQueryWrapper<>();
        qw.eq(AnimeRating::getAnimeId,animeId);
        return ratingMapper.selectOne(qw);
    }

    @Override
    public Result<List<AnimeCardVO>> getByDirCondition(DirCondition dirCondition) {
        if(dirCondition.getYear().equals("全部")) dirCondition.setYear("");
        if(dirCondition.getTag().equals("全部")) dirCondition.setTag("TV");
        dirCondition.setTagId(tagService.getTagByName(dirCondition.getTag()).getId());
        dirCondition.setOffset(dirCondition.getOffset()* dirCondition.getPageSize());
        List<Anime> animeList= animeMapper.getByDirCondition(dirCondition);
        List<AnimeCardVO> animeCardVOList=new ArrayList<>();
        for (Anime anime : animeList) {
            Episode ep=episodeService.getAnimeEpByAnimeId(anime.getId());
            anime.setDescription(null);
            animeCardVOList.add(AnimeCardVO.builder()
                            .anime(anime)
                            .ep(ep.getEp())
                            .totalEps(ep.getTotalEps())
                    .build());
        }
        return Result.success(animeCardVOList);
    }

    @Override
    public List<AnimeKeywordSearchVO> searchAnimeByKeyword(String keyword,int offset,int pageSize) {
        List<Anime> animes= animeMapper.getByNamelike(keyword,offset,pageSize);
        List<AnimeKeywordSearchVO> voList=new ArrayList<>();
        for(Anime anime:animes){
            Long animeId=anime.getId();
            Episode ep=episodeService.getAnimeEpByAnimeId(animeId);
            AnimeKeywordSearchVO vo=AnimeKeywordSearchVO.builder()
                    .name(anime.getName())
                    .animeId(animeId)
                    .ep(ep.getEp())
                    .totalEps(ep.getTotalEps())
                    .image(anime.getImage())
                    .description(anime.getDescription()).build();
            if(vo.getCompany()==null) vo.setCompany("未知");
            AnimeInfo info= infoMapper.getAnimeId(animeId);
            vo.setDate(info.getStartDate());
            vo.setCompany(info.getCompany());
            List<String> excludedWords = Arrays.asList("TV", "日本", "漫画改", "漫改", "TVA", "动画", "续作", "日漫", "未确定");
            List<String> tags=tagService.getAnimeTags(animeId).stream().filter((tag)->
                    !excludedWords.contains(tag)
            ).collect(Collectors.toList());
            List<String> newtags=new ArrayList<>();
            for(int i=0; i<Math.min(6,tags.size());i++){
                newtags.add(tags.get(i));
            }
            vo.setTags(newtags);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<AnimeCard2VO> getRecentlyUpdate() {
        LambdaQueryWrapper<Anime> qw= new LambdaQueryWrapper<>();
        qw.orderByDesc(Anime::getUpdateTime).last("limit 0,30");
        List<Anime> animes= animeMapper.selectList(qw);
        List<AnimeCard2VO> voList=new ArrayList<>();
        for(Anime anime:animes){
            Long animeId=anime.getId();
            Episode ep=episodeService.getAnimeEpByAnimeId(animeId);
            AnimeCard2VO vo=AnimeCard2VO.builder()
                    .name(anime.getName())
                    .animeId(animeId)
                    .ep(ep.getEp())
                    .totalEps(ep.getTotalEps())
                    .image(anime.getImage())
                    .description(anime.getDescription()).build();
            AnimeInfo info= infoMapper.getAnimeId(animeId);
            vo.setDate(info.getStartDate());
            List<String> excludedWords = Arrays.asList("TV", "日本", "漫画改", "漫改", "TVA", "动画", "续作", "日漫", "未确定");
            List<String> tags=tagService.getAnimeTags(animeId).stream().filter((tag)->
                    !excludedWords.contains(tag)
            ).collect(Collectors.toList());
            List<String> newtags=new ArrayList<>();
            for(int i=0; i<Math.min(6,tags.size());i++){
                newtags.add(tags.get(i));
            }
            vo.setTags(newtags);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public Result<List<AnimeCardVO>> getCardByNamelike(String name, Integer page, Integer pageSize,boolean useDscp) {
        List<Anime> animeList =new ArrayList<>();
        animeList =getByNamelike(name,page,pageSize);
        List<AnimeCardVO> resultList=new ArrayList<>();
        for (Anime tp: animeList){
            if(!useDscp){
                tp.setDescription("");
            }
            Episode ep=episodeService.getAnimeEpByAnimeId(tp.getId());
            if(ep==null){
                log.info(tp.toString());
            }
            resultList.add(AnimeCardVO.builder()
                    .anime(tp)
                    .ep(ep.getEp())
                    .totalEps(ep.getTotalEps())
                    .build());
        }
        return Result.success(resultList);
    }


    @Override
    public AnimationDetailVO getAnimeDetail(Long animeId) {
        AnimationDetailVO detailVO=AnimationDetailVO.builder()
                .anime(getById(animeId))
                .animeInfo(getInfoByAnimeId(animeId))
                .animeRating(getRatingByAnimeId(animeId))
                .tags(tagService.getAnimeTags(animeId))
                .ep(episodeService.getAnimeEpByAnimeId(animeId).getEp())
                .build();
        return detailVO;
    }


}
