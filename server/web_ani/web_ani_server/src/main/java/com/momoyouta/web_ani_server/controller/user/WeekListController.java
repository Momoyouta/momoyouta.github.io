package com.momoyouta.web_ani_server.controller.user;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.entity.Animation;
import com.momoyouta.web_ani_pojo.entity.WeekListItem;
import com.momoyouta.web_ani_server.service.AniService;
import com.momoyouta.web_ani_server.service.WeekListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Result<List<Animation>> getDaylist(@RequestParam Integer day){
        List<Long> idList=weekListService.getDayAnis(day);
        List<Animation> aniList=new ArrayList<>();
        for(Long id : idList){
            Animation animation=aniService.getById(id);
            animation.setDescription("");
            aniList.add(animation);
        }
        log.info(aniList.toString());
        return Result.success(aniList);
    }

}
