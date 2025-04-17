package com.momoyouta.web_ani_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimationDetailVO;
import com.momoyouta.web_ani_pojo.VO.AnimeCard2VO;
import com.momoyouta.web_ani_pojo.VO.AnimeKeywordSearchVO;
import com.momoyouta.web_ani_pojo.dto.AniAddDTO;
import com.momoyouta.web_ani_pojo.dto.DirCondition;
import com.momoyouta.web_ani_pojo.entity.*;
import com.momoyouta.web_ani_server.mapper.AniMapper;
import com.momoyouta.web_ani_server.mapper.AnimeAndTagsMapper;
import com.momoyouta.web_ani_server.mapper.AnimeInfoMapper;
import com.momoyouta.web_ani_server.mapper.AnimeRatingMapper;
import com.momoyouta.web_ani_server.service.AniService;
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
    private AniMapper aniMapper;

    @Autowired
    private AnimeInfoMapper infoMapper;

    @Autowired
    private AnimeRatingMapper ratingMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private AnimeAndTagsMapper animeAndTagsMapper;


    @Override
    public Animation getById(Long id) {
        return aniMapper.getById(id);
    }


    @Override
    public Result update(Animation animation){
        LambdaUpdateWrapper<Animation> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        animation.setUpdateTime(LocalDateTime.now().withNano(0).withSecond(0));
        log.info(aniMapper.update(animation,
                lambdaUpdateWrapper.eq(Animation::getId, animation.getId()))+"");
        return Result.success("更新成功");
    }

    @Override
    public Result add(AniAddDTO aniAddDTO) {
        Animation animation=new Animation();
        BeanUtils.copyProperties(aniAddDTO,animation);
        animation.setUpdateTime(LocalDateTime.now().withNano(0).withSecond(0));
        animation.setBan(1);
        if(aniMapper.getByName(animation.getName())!=null){
            return Result.error(2,"番剧已存在");
        }
        aniMapper.insert(animation);
        return Result.success("添加成功");
    }

    @Override
    public Result addNoDTO(Animation animation) {
        animation.setUpdateTime(LocalDateTime.now().withNano(0).withSecond(0));
        animation.setBan(0);
        if(aniMapper.getByName(animation.getName())!=null){
            return Result.error(2,"番剧已存在");
        }
        aniMapper.insert(animation);
        return Result.success("添加成功");
    }

    @Override
    public List<Animation> getByNamelike(String name,int page,int pageSize) {
        page=(page-1)*pageSize;
        List<Animation> list=aniMapper.getByNamelike(name,page,pageSize);
        return list;
    }

    @Override
    public List<Animation> getByName(String name){
        return aniMapper.getByName(name);
    }

    @Override
    public Integer getByNamelikeTotPage(String name) {
        int pages=aniMapper.getByNamelikeTotPage(name);
        return pages;
    }

    @Override
    public void deleteByName(String name){

        aniMapper.deleteByName(name);
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
    public List<Animation> getByDirCondition(DirCondition dirCondition) {
        if(dirCondition.getYear().equals("全部")) dirCondition.setYear("");
        if(dirCondition.getTag().equals("全部")) dirCondition.setTag("TV");
        dirCondition.setTagId(tagService.getTagByName(dirCondition.getTag()).getId());
        dirCondition.setOffset(dirCondition.getOffset()* dirCondition.getPageSize());
        List<Animation> list=aniMapper.getByDirCondition(dirCondition);
        for (Animation animation : list) {
            animation.setDescription(null);
        }
        return list;
    }

    @Override
    public List<AnimeKeywordSearchVO> searchAnimeByKeyword(String keyword,int offset,int pageSize) {
        List<Animation> animes= aniMapper.getByNamelike(keyword,offset,pageSize);
        List<AnimeKeywordSearchVO> voList=new ArrayList<>();
        for(Animation anime:animes){
            Long animeId=anime.getId();
            AnimeKeywordSearchVO vo=AnimeKeywordSearchVO.builder()
                    .name(anime.getName())
                    .animeId(animeId)
                    .ep(anime.getEp())
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
        LambdaQueryWrapper<Animation> qw= new LambdaQueryWrapper<>();
        qw.orderByDesc(Animation::getUpdateTime).last("limit 0,30");
        List<Animation> animes= aniMapper.selectList(qw);
        List<AnimeCard2VO> voList=new ArrayList<>();
        for(Animation anime:animes){
            Long animeId=anime.getId();
            AnimeCard2VO vo=AnimeCard2VO.builder()
                    .name(anime.getName())
                    .animeId(animeId)
                    .ep(anime.getEp())
                    .image(anime.getImage())
                    .description(anime.getDescription()).build();
            AnimeInfo info= infoMapper.getAnimeId(animeId);
            vo.setDate(info.getStartDate().substring(0,4));
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
    public AnimationDetailVO getAnimeDetail(Long animeId) {
        AnimationDetailVO detailVO=new AnimationDetailVO();
        detailVO.setAnimation(getById(animeId));
        detailVO.setAnimeRating(getRatingByAnimeId(animeId));
        detailVO.setAnimeInfo(getInfoByAnimeId(animeId));
        detailVO.setTags(tagService.getAnimeTags(animeId));
        return detailVO;
    }


}
