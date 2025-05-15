package com.momoyouta.web_ani_pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowAnime {
    private String id;
    private String userId;
    private Long animeId;
    private int process;
}
