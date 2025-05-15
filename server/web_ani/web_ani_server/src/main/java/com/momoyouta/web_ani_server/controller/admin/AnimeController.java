package com.momoyouta.web_ani_server.controller.admin;


import com.momoyouta.web_ani_common.enumm.ResponseCode;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.dto.AniAddDTO;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_server.service.AniService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController("adminAnimeController")
@RequestMapping("/admin/ani")
public class AnimeController {

    @Autowired
    private  AniService aniService;
    @GetMapping
    public Result<Anime> getOne(@RequestParam Long id) {
        Anime anime =aniService.getById(id);
        if(anime ==null)
            return Result.error(ResponseCode.ERROR,"番不存在");
        return Result.success(anime);
    }

    @PutMapping("/add")
    public Result addAnimation(@RequestBody AniAddDTO aniAddDTO){
        return aniService.add(aniAddDTO);
    }

    @GetMapping("/search/bynamelike")
    public Result<List<Anime>> getByNamelike(@RequestParam String name, @RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam boolean useDscp){
        List<Anime> animeList =new ArrayList<>();
        animeList =aniService.getByNamelike(name,page,pageSize);
        if(!useDscp){
            for (Anime tp: animeList){
                tp.setDescription("");
            }
        }
        return Result.success(animeList);
    }

//    @GetMapping("/search/byname")
//    public Result<Anime> getByName(@RequestParam String name,@RequestParam boolean useDscp){
//        Anime anime=new Anime();
//        anime=aniService.getByName(name);
//        if(!useDscp){
//            anime.setDescription("");
//        }
//        return Result.success(anime);
//    }

    @GetMapping("/search/page")
    public Result<Integer> getByNamelikeTotPage(@RequestParam String name){
        Integer totPages=0;
        totPages=aniService.getByNamelikeTotPage(name);
        log.info(totPages.toString());
        return Result.success(totPages);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Anime anime){
        Result result=aniService.update(anime);
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
