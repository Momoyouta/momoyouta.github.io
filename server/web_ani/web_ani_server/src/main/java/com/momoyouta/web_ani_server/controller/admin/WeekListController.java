package com.momoyouta.web_ani_server.controller.admin;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.entity.Anime;
import com.momoyouta.web_ani_pojo.entity.WeekListItem;
import com.momoyouta.web_ani_server.service.AniService;
import com.momoyouta.web_ani_server.service.WeekListService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController("adminWeekListController")
@RequestMapping("/admin/weeklist")
public class WeekListController {

    @Autowired
    private WeekListService weekListService;

    @Autowired
    private AniService aniService;

    @PutMapping("/update/{day}")
    @PreAuthorize("hasAuthority('admin')")
    public Result<String> updateList(@RequestBody  List<WeekListItem>  dayList, @PathVariable int day){
        log.info(dayList.toString());
        weekListService.updateDayList(dayList,day);
        return Result.success("更新成功");
    }

    @GetMapping ("/request/quarter")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<List<AnimeCardVO>>> getQuarterlist(@RequestParam Long quarter){

        return weekListService.getQuarterAnime(quarter);
    }

}
