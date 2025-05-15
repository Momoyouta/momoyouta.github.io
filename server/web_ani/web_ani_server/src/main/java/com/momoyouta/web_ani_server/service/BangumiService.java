package com.momoyouta.web_ani_server.service;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.dto.BangumiBaseAnimeDTO;
import com.momoyouta.web_ani_pojo.entity.Episode;

import java.util.List;

public interface BangumiService {
    public int transAnimeData(BangumiBaseAnimeDTO baseAnimeDTO);

    Result<Integer> updateEps(List<Episode> animeEps);
}
