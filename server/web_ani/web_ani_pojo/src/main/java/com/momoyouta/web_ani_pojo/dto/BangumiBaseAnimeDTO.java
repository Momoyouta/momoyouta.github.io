package com.momoyouta.web_ani_pojo.dto;

import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.entity.AnimeInfo;
import com.momoyouta.web_ani_pojo.entity.AnimeRating;
import com.momoyouta.web_ani_pojo.entity.Episode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BangumiBaseAnimeDTO implements Serializable {
    private Anime anime;
    private AnimeInfo animeInfo;
    private AnimeRating animeRating;
    private Episode episode;
    private List<String> tags;
}
