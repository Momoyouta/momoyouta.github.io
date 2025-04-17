package com.momoyouta.web_ani_server.controller.user;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimationDetailVO;
import com.momoyouta.web_ani_pojo.VO.AnimeCard2VO;
import com.momoyouta.web_ani_pojo.VO.AnimeKeywordSearchVO;
import com.momoyouta.web_ani_pojo.dto.AniAddDTO;
import com.momoyouta.web_ani_pojo.dto.DirCondition;
import com.momoyouta.web_ani_pojo.entity.Animation;
import com.momoyouta.web_ani_server.service.AniService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/ani")
public class AnimeController {
    @Autowired
    private AniService aniService;
    @GetMapping
    public Result<Animation> getOne(@RequestParam Long id) {
        Animation animation=aniService.getById(id);
        if(animation==null)
            return Result.error(2,"番不存在");
        return Result.success(animation);
    }

    @GetMapping("/search/bynamelike")
    public Result<List<Animation>> getByNamelike(@RequestParam String name, @RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam boolean useDscp){
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

    @GetMapping("/getdetail/{animeid}")
    public Result<AnimationDetailVO> getAnimeDetail(@PathVariable Long animeid){
        AnimationDetailVO detailVO= aniService.getAnimeDetail(animeid);
        log.info("animeDetail: "+detailVO);
        return Result.success(detailVO);
    }

    @PostMapping("/dir/select")
    public Result<List<Animation>> dirSelect(@RequestBody DirCondition dirCondition){
        log.info("dirCondition:"+dirCondition.toString());
        List<Animation> list=aniService.getByDirCondition(dirCondition);
        log.info(list.toString());
        return Result.success(list);
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
