package com.momoyouta.web_ani_pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("episodes")
public class Episode {
    private String id;
    private Long animeId;
    private int ep;
    private int totalEps;
    private String data;//预留
}
