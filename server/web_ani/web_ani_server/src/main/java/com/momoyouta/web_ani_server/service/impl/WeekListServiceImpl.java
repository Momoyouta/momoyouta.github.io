package com.momoyouta.web_ani_server.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.entity.AnimeInfo;
import com.momoyouta.web_ani_pojo.entity.Episode;
import com.momoyouta.web_ani_pojo.entity.WeekListItem;
import com.momoyouta.web_ani_server.mapper.AnimeInfoMapper;
import com.momoyouta.web_ani_server.mapper.EpisodeMapper;
import com.momoyouta.web_ani_server.mapper.WeekListMapper;
import com.momoyouta.web_ani_server.service.AniService;
import com.momoyouta.web_ani_server.service.EpisodeService;
import com.momoyouta.web_ani_server.service.WeekListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WeekListServiceImpl implements WeekListService {

    @Autowired
    private WeekListMapper weekListMapper;

    @Autowired
    private AnimeInfoMapper infoMapper;

    @Autowired
    private AniService aniService;

    @Autowired
    private EpisodeService episodeService;

    @Override
    public List<Long> getDayAnis(int day) {
        List<Long> aniList=weekListMapper.getAniId(day);
        return aniList;
    }


    @Override
    public void updateDayList(List<WeekListItem> dayList, int day) {
        weekListMapper.delByDay(day);
        weekListMapper.insert(dayList);
    }

    @Override
    public Result<List<List<AnimeCardVO>>> getQuarterAnime(Long quarter) {
        Long endTime=quarter+24*60*60*31*3;
        LambdaQueryWrapper <AnimeInfo> qw=new LambdaQueryWrapper<>();
        qw.select(AnimeInfo::getAnimeId,AnimeInfo::getWeekday,AnimeInfo::getStartDate).ge(AnimeInfo::getStartDate,quarter).le(AnimeInfo::getStartDate,endTime);
        List<AnimeInfo> animes=infoMapper.selectList(qw);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        String startDate=sdf.format(new Date(quarter*1000L));
        String endDate=sdf.format(new Date(endTime*1000L));
        List<List<Long>> ids=new ArrayList<>();
        List<List<AnimeCardVO>>  weeklist=new ArrayList<>();
        for(int i=0;i<8;i++){
            ids.add(new ArrayList<>());
            weeklist.add(new ArrayList<>());
        }
        for(AnimeInfo ani:animes){
            if(!sdf.format(new Date(ani.getStartDate()*1000L)).equals(endDate)){
                ids.get(Integer.valueOf(ani.getWeekday())).add(ani.getAnimeId());
            }
        }
        log.info("周表动漫id："+ids.toString());
        int day=0;
        for(List<Long> idDay : ids){
            for(Long id :idDay){
                Anime anime =aniService.getById(id);
                anime.setDescription("");
                Episode ep=episodeService.getAnimeEpByAnimeId(id);
                if(ep==null){
                    log.info(anime.toString());
                }
                weeklist.get(day).add(AnimeCardVO.builder()
                        .anime(anime)
                        .ep(ep.getEp())
                        .totalEps(ep.getTotalEps())
                        .build());
            }
            day++;
            if(day>7) break;
        }
        log.info(weeklist.toString());
        return Result.success(weeklist);
    }

    @Override
    public Result<List<AnimeCardVO>> getDaylist(Integer day) {
        List<Long> idList=getDayAnis(day);
        List<AnimeCardVO> aniList=new ArrayList<>();
        for(Long id : idList){
            Anime anime =aniService.getById(id);
            anime.setDescription("");
            Episode ep=episodeService.getAnimeEpByAnimeId(id);
            if(ep==null){
                log.info(anime.toString());
            }
            aniList.add(AnimeCardVO.builder()
                            .anime(anime)
                            .ep(ep.getEp())
                            .totalEps(ep.getTotalEps())
                    .build());
        }
        log.info(aniList.toString());
        return Result.success(aniList);
    }
}
