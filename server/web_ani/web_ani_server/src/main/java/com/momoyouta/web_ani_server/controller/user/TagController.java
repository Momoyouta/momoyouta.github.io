package com.momoyouta.web_ani_server.controller.user;

import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_pojo.entity.Tag;
import com.momoyouta.web_ani_server.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userTagController")
@RequestMapping("/user/tag")
public class TagController {

    @Autowired
    TagService tagService;

    @PutMapping("/updatecount")
    public Result<String> updateCount(){
        tagService.updateCount();
        return Result.success("更新成功");
    }

    @GetMapping("/hot")
    public Result<List<Tag>> getHotTag(){
        List<Tag> list=tagService.getHotTag();
        return Result.success(list);
    }
}
