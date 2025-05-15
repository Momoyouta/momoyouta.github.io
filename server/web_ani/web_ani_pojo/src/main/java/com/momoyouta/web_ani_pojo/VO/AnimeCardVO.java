package com.momoyouta.web_ani_pojo.VO;

import com.momoyouta.web_ani_pojo.entity.Anime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimeCardVO {
    private Anime anime;
    private int ep;
    private int totalEps;
}
