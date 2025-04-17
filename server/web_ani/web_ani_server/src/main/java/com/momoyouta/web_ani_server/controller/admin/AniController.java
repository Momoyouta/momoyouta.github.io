package com.momoyouta.web_ani_server.controller.admin;


import com.momoyouta.web_ani_common.enumm.SearchType;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.dto.AniAddDTO;
import com.momoyouta.web_ani_pojo.entity.Animation;
import com.momoyouta.web_ani_server.mapper.AniMapper;
import com.momoyouta.web_ani_server.service.AniService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/admin/ani")
public class AniController {

    @Autowired
    private  AniService aniService;
    @GetMapping
    public Result<Animation> getOne(@RequestParam Long id) {
        Animation animation=aniService.getById(id);
        if(animation==null)
            return Result.error(2,"番不存在");
        return Result.success(animation);
    }

    @PutMapping("/add")
    public Result addAnimation(@RequestBody AniAddDTO aniAddDTO){
        return aniService.add(aniAddDTO);
    }

    @GetMapping("/search/bynamelike")
    public Result<List<Animation>> getByNamelike(@RequestParam String name,@RequestParam Integer page,@RequestParam Integer pageSize,@RequestParam boolean useDscp){
        List<Animation> animationList=new ArrayList<>();
        animationList=aniService.getByNamelike(name,page,pageSize);
        if(!useDscp){
            for (Animation tp:animationList){
                tp.setDescription("");
            }
        }
        return Result.success(animationList);
    }

//    @GetMapping("/search/byname")
//    public Result<Animation> getByName(@RequestParam String name,@RequestParam boolean useDscp){
//        Animation animation=new Animation();
//        animation=aniService.getByName(name);
//        if(!useDscp){
//            animation.setDescription("");
//        }
//        return Result.success(animation);
//    }

    @GetMapping("/search/page")
    public Result<Integer> getByNamelikeTotPage(@RequestParam String name){
        Integer totPages=0;
        totPages=aniService.getByNamelikeTotPage(name);
        log.info(totPages.toString());
        return Result.success(totPages);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Animation animation){
        Result result=aniService.update(animation);
        return result;
    }

    @PutMapping("/update/del/{name}")
    public Result delelte(@PathVariable String name){
        log.info(name);
        aniService.deleteByName(name);
        return  Result.success();
    }

    @GetMapping("/https")
    public String test1(){
        return "OK";
    }

}
