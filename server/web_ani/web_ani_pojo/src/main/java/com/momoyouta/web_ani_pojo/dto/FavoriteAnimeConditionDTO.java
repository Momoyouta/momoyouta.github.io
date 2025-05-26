package com.momoyouta.web_ani_pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteAnimeConditionDTO {
    /**
     * 状态：0-全部，1-在看，2-看过，3-弃置
     */
    private Integer status;
    /**
     * 排序方式：0-收藏时间，1-大众评分，2-个人评分
     */
    private Integer orderr;
}
