package com.momoyouta.web_ani_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.dto.BangumiBaseAnimeDTO;
import com.momoyouta.web_ani_pojo.entity.*;
import com.momoyouta.web_ani_server.mapper.AnimeInfoMapper;
import com.momoyouta.web_ani_server.mapper.AnimeMapper;
import com.momoyouta.web_ani_server.mapper.AnimeRatingMapper;
import com.momoyouta.web_ani_server.mapper.EpisodeMapper;
import com.momoyouta.web_ani_server.service.AniService;
import com.momoyouta.web_ani_server.service.BangumiService;
import com.momoyouta.web_ani_server.service.EpisodeService;
import com.momoyouta.web_ani_server.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BangumiServiceImpl implements BangumiService {

    @Autowired
    private AniService aniService;
    @Autowired
    private TagService tagService;
    @Autowired
    private AnimeMapper animeMapper;
    @Autowired
    private AnimeRatingMapper animeRatingMapper;
    @Autowired
    private AnimeInfoMapper animeInfoMapper;
    @Autowired
    private EpisodeMapper episodeMapper;
    @Autowired
    private EpisodeService episodeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transAnimeData(BangumiBaseAnimeDTO baseAnimeDTO) {
        Anime anime =new Anime();
        BeanUtils.copyProperties(baseAnimeDTO.getAnime(), anime);
        AnimeInfo animeInfo=new AnimeInfo();
        BeanUtils.copyProperties(baseAnimeDTO.getAnimeInfo(),animeInfo);
        AnimeRating animeRating=new AnimeRating();
        BeanUtils.copyProperties(baseAnimeDTO.getAnimeRating(),animeRating);
        Episode episode=new Episode();
        BeanUtils.copyProperties(baseAnimeDTO.getEpisode(),episode);
        Long animeId = anime.getId();
        animeRating.setAnimeId(animeId);
        animeInfo.setAnimeId(animeId);
        try {
            List<String> tags = baseAnimeDTO.getTags();
            List<Tag> tagList=new ArrayList<>();
            Long zero = Long.valueOf(0);
            for(String tp:tags){;
                Tag t=new Tag(null,tp,zero);
                tagList.add(t);
            }
            Anime aniTp=aniService.getById(anime.getId());
            if(aniTp!=null)
            {
                log.info("番已存在:"+ anime.getName());
                animeMapper.updateById(anime);
                LambdaQueryWrapper<AnimeRating> qw1=new LambdaQueryWrapper<>();
                qw1.eq(AnimeRating::getAnimeId,anime.getId());
                animeRatingMapper.update(animeRating,qw1);
                LambdaQueryWrapper<AnimeInfo> qw2=new LambdaQueryWrapper<>();
                qw2.eq(AnimeInfo::getAnimeId,anime.getId());
                animeInfoMapper.update(animeInfo,qw2);
                LambdaQueryWrapper<Episode> qw3=new LambdaQueryWrapper<>();
                qw3.eq(Episode::getAnimeId,anime.getId());
                episodeMapper.update(episode,qw3);
            }else{
                aniService.addNoDTO(anime);
                tagService.addTags(tagList);
                aniService.addAnimeInfo(animeInfo);
                aniService.addAnimeRating(animeRating);
                aniService.addTag(animeId,tags);
                episodeService.add(episode);
            }

            log.info("anime_id:" + anime.getId());
            log.info("info_id:" + animeInfo.getId());
            log.info("rating_id:" + animeRating.getId());
            log.info("episode_id:" + episode.getId());
        }
        catch (Exception e){
            System.out.println(e);
            return 0;
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateEps(List<Episode> animeEps) {
        try {
            for(Episode ep:animeEps){
                LambdaQueryWrapper<Episode> qw=new LambdaQueryWrapper<>();
                qw.eq(Episode::getAnimeId,ep.getAnimeId());
                Episode tp=episodeMapper.selectOne(qw);
                if(tp==null){
                    episodeMapper.insert(ep);
                }else{
                    ep.setId(tp.getId());
                    episodeMapper.updateById(ep);
                }
            }
        }catch (Exception e){
            System.out.println(e);
            return Result.success(0);
        }
        return Result.success(1);
    }
}
