package com.momoyouta.web_ani_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.momoyouta.web_ani_pojo.dto.DirCondition;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.VO.AnimeFavoriteCardVO;
import com.momoyouta.web_ani_pojo.dto.FavoriteAnimeConditionDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnimeMapper extends BaseMapper<Anime> {

    @Select("select * from anime where id = #{id}")
    Anime getById(Long id);

    @Select("select * from anime where name = #{name}")
    Anime getByName(String name);

    @Select("select * from anime where  name like concat(#{name},'%') limit #{page},#{pageSize}")
    List<Anime> getByNamelike(String name, int page, int pageSize);

    @Delete("delete from anime where name = #{name}")
    void deleteByName(String name);

    @Select("select count(*) from anime where name like concat(#{name},'%')")
    Integer getByNamelikeTotPage(String name);

    @Select("select  description from anime where id = #{id}")
    String getDscpById(Long id);

    List<Anime> getByDirCondition(DirCondition dirCondition);

    List<AnimeFavoriteCardVO> selectFavoriteAnimesByCondition(@Param("userId") String userId,
                                                              @Param("offset") int offset,
                                                              @Param("pageSize") int pageSize,
                                                              @Param("condition") FavoriteAnimeConditionDTO condition);
}
