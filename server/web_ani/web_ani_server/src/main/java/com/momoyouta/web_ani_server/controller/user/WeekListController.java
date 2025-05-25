package com.momoyouta.web_ani_server.controller.user;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.dto.WeekListStoreDTO;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.entity.Episode;
import com.momoyouta.web_ani_server.service.AniService;
import com.momoyouta.web_ani_server.service.WeekListService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController("userWeekListController")
@RequestMapping("/user/weeklist")
public class WeekListController {

    @Autowired
    private WeekListService weekListService;

    @Autowired
    private AniService aniService;

    @GetMapping("/request")
    public Result<List<AnimeCardVO>> getDaylist(@RequestParam Integer day){
        return weekListService.getDaylist(day);
    }

    @GetMapping("/getWeekListStore")
    @PermitAll
    public Result<List<WeekListStoreDTO>> getWeekListStore(){
        return weekListService.getWeekListStore();
    }

}
