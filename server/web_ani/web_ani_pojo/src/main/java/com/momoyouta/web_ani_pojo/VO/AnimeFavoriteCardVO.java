package com.momoyouta.web_ani_pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimeFavoriteCardVO {
    private Long id;
    private String name;
    private String image;
    private String description;
    private BigDecimal score; // 大众评分
    private LocalDateTime updateTime;
    private int ep;
    private int totalEps;
    private int status;
    private BigDecimal userScore;
    private Long favoriteTime;
}