package com.momoyouta.web_ani_server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.momoyouta.web_ani_pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper  extends BaseMapper<User> {

}
