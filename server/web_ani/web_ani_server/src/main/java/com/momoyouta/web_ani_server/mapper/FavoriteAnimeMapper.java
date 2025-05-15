package com.momoyouta.web_ani_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.momoyouta.web_ani_pojo.entity.FavoriteAnime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

@Mapper
public interface FavoriteAnimeMapper extends BaseMapper<FavoriteAnime> {


    @Insert( "INSERT INTO favorite (user_id, anime_id, status) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.userId}, #{item.animeId}, #{item.status})" +
            "</foreach>" +
            "ON DUPLICATE KEY UPDATE status = VALUES(status)")
    int insertOrUpdateBatch(@Param("list") List<FavoriteAnime> favoriteAnimes);
}
