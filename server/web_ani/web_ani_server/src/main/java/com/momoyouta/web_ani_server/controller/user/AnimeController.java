package com.momoyouta.web_ani_server.controller.user;

import com.momoyouta.web_ani_common.enumm.ResponseCode;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimationDetailVO;
import com.momoyouta.web_ani_pojo.VO.AnimeCard2VO;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.VO.AnimeKeywordSearchVO;
import com.momoyouta.web_ani_pojo.dto.DirCondition;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_server.service.AniService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController("userAnimeController")
@RequestMapping("/user/ani")
public class AnimeController {
    @Autowired
    private AniService aniService;
    @GetMapping
    public Result<Anime> getOne(@RequestParam Long id) {
        Anime anime =aniService.getById(id);
        if(anime ==null)
            return Result.error(ResponseCode.ERROR,"番不存在");
        return Result.success(anime);
    }

    @GetMapping("/search/bynamelike")
    public Result<List<AnimeCardVO>> getByNamelike(@RequestParam String name, @RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam boolean useDscp){
        return aniService.getCardByNamelike(name,page,pageSize,useDscp);
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

    @GetMapping("/getdetail/{animeid}")
    public Result<AnimationDetailVO> getAnimeDetail(@PathVariable Long animeid){
        AnimationDetailVO detailVO= aniService.getAnimeDetail(animeid);
        log.info("animeDetail: "+detailVO.getAnime().getName());
        return Result.success(detailVO);
    }

    @PostMapping("/dir/select")
    public Result<List<AnimeCardVO>> dirSelect(@RequestBody DirCondition dirCondition){
        return aniService.getByDirCondition(dirCondition);
    }

    @GetMapping("/search/keyword")
    public Result<List<AnimeKeywordSearchVO>> searchAnimeByKeyword(@RequestParam String keyword,@RequestParam int offset,@RequestParam int pageSize){
        List<AnimeKeywordSearchVO> list=aniService.searchAnimeByKeyword(keyword,offset,pageSize);
        log.info("keywordAnimes:"+list.toString());
        return Result.success(list);
    }

    @GetMapping("/update/recently")
    public Result<List<AnimeCard2VO>> getRecentlyUpdateAnime(){
        List <AnimeCard2VO> list =aniService.getRecentlyUpdate();
        return Result.success(list);
    }
}
