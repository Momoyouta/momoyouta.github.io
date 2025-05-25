package com.momoyouta.web_ani_pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekListStoreDTO {
    public Long animeId;
    public int day;
    public String name;
}
