package com.momoyouta.web_ani_pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("animeinfo_list")
public class AnimeInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long animeId;
    private Long startDate;
    private String weekday;
    private String company;
    private String officialWeb;
    private Long videolistId;
}
