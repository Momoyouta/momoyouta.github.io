package com.momoyouta.web_ani_server.controller.admin;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.dto.BangumiBaseAnimeDTO;
import com.momoyouta.web_ani_pojo.entity.Episode;
import com.momoyouta.web_ani_server.service.BangumiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/bangumi")
public class BangumiController {

    @Autowired
    private BangumiService bangumiService;

    @PutMapping("/baseadd")
    @PreAuthorize("hasAuthority('admin')")
    public Result<Integer> getbaseAnimes(@RequestBody BangumiBaseAnimeDTO baseAnimeDTO){
        int p=bangumiService.transAnimeData(baseAnimeDTO);
        return Result.success(p);
    }

    @PutMapping("/updateEps")
    @PreAuthorize("hasAuthority('admin')")
    public Result<Integer> updateEps(@RequestBody List<Episode> animeEps){
        return bangumiService.updateEps(animeEps);
    }


}
