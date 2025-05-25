package com.momoyouta.web_ani_pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("favorite")
public class FavoriteAnime {
    private String id;
    private String userId;
    private Long animeId;
    private BigDecimal score;
    private int favoriteStatus;
    private int status;
}
