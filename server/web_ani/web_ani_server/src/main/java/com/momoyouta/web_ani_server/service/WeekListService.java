package com.momoyouta.web_ani_server.service;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.entity.WeekListItem;

import java.util.List;

public interface WeekListService {
    List<Long> getDayAnis(int day);

    void updateDayList(List<WeekListItem> dayList,int day);

    Result<List<List<AnimeCardVO>>>  getQuarterAnime(Long quarter);

    Result<List<AnimeCardVO>> getDaylist(Integer day);
}
