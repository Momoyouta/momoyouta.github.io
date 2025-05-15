package com.momoyouta.web_ani_pojo.VO;


import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.entity.AnimeInfo;
import com.momoyouta.web_ani_pojo.entity.AnimeRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimationDetailVO {
    private Anime anime;
    private AnimeInfo animeInfo;
    private AnimeRating animeRating;
    private int ep;
    private List<String> tags;
}
