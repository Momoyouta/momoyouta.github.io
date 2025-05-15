package com.momoyouta.web_ani_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.momoyouta.web_ani_pojo.entity.WeekListItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WeekListMapper extends BaseMapper<WeekListItem> {

    @Select("select  anime_id from momoyouta_web_ani.week_ani_list where days=#{day}")
    List<Long> getAniId(int day);

    @Delete("delete from momoyouta_web_ani.week_ani_list where days=#{day}")
    void delByDay(int day);

}
