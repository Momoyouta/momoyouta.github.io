package com.momoyouta.web_ani_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.momoyouta.web_ani_pojo.entity.Episode;
import com.momoyouta.web_ani_server.mapper.EpisodeMapper;
import com.momoyouta.web_ani_server.service.EpisodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EpisodeServiceImpl implements EpisodeService {
    @Autowired
    EpisodeMapper episodeMapper;

    @Override
    public void add(Episode episode) {
        episode.setId(UUID.randomUUID().toString());
        episodeMapper.insert(episode);
    }

    @Override
    public Episode getAnimeEpByAnimeId(Long animeId) {
        LambdaQueryWrapper<Episode> queryWrapper=new LambdaQueryWrapper<Episode>();
        queryWrapper.eq(Episode::getAnimeId,animeId);
        return episodeMapper.selectOne(queryWrapper);
    }
}
