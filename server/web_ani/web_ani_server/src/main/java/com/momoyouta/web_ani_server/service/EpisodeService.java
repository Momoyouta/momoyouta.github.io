package com.momoyouta.web_ani_server.service;

import com.momoyouta.web_ani_pojo.entity.Episode;

public interface EpisodeService {
    public void add(Episode episode);

    public Episode getAnimeEpByAnimeId(Long animeId);
}
