package com.momoyouta.web_ani_server.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_common.utils.JwtUtil;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.VO.AnimeFavoriteCardVO;
import com.momoyouta.web_ani_pojo.dto.FavoriteAnimeConditionDTO;
import com.momoyouta.web_ani_pojo.dto.ForgotPasswordDTO;
import com.momoyouta.web_ani_pojo.dto.RegisterDTO;
import com.momoyouta.web_ani_pojo.dto.UserBaseInfoDTO;
import com.momoyouta.web_ani_pojo.dto.UserTokenDTO;
import com.momoyouta.web_ani_pojo.entity.User;
import com.momoyouta.web_ani_server.service.UserService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;;

    @GetMapping("/testJWT")
    @PreAuthorize("hasAuthority('admin')")
    public Result<String> testJWT(String jwt) throws Exception {
        String id= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info(id);
        log.info(SecurityContextHolder.getContext().getAuthentication().toString());
        return Result.success("success");
    }

    @GetMapping("/login")
    @PermitAll
    public Result<UserTokenDTO> loginByAccount(@RequestParam String  account, @RequestParam String password) throws JsonProcessingException {
        return userService.loginByAccount(account,password);
    }

    @PostMapping("/register")
    @PermitAll
    public Result<String> register(@RequestBody RegisterDTO registerDTO) {
        return userService.registerByAccount(registerDTO);
    }

    @PostMapping("/forgotPassword")
    @PermitAll
    public Result<String> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return Result.success("修改成功");
    }

    @PostMapping("/updateUserInfo")
    @PreAuthorize("hasAnyAuthority('user','admin')")
    public Result<String> updateUserInfo(@RequestBody UserBaseInfoDTO info) {
        return userService.updateUserInfo(info);
    }

    @PostMapping("/followAnime")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> followAnime(@RequestBody List<Long> animeId) {
        return userService.followAnimeByList(animeId);
    }

    @PostMapping("/unFollowAnime")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> unFollowAnime(@RequestBody List<Long> animeId) {
        return  userService.unFollowAnimeByList(animeId);
    }

    @GetMapping("/updateFollowProcess")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> updateFollowProcess(@RequestParam Long animeId, @RequestParam int process) {
        return userService.updateFollowProcess(animeId,process);
    }

    @GetMapping("/finishFollow")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> finishFollow(@RequestParam Long animeId) {
        return userService.finishFollow(animeId);
    }

    @GetMapping("/score")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> score(@RequestParam Long animeId, @RequestParam BigDecimal score) {
        return userService.score(animeId,score);
    }

    @GetMapping("/favorite")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> favorite(@RequestParam Long animeId) {
        return userService.favorite(animeId);
    }

    @GetMapping("/unFavorite")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> unFavorite(@RequestParam Long animeId) {
        return userService.unFavorite(animeId);
    }

    @GetMapping("/getFavoriteStatus")
    @PreAuthorize("hasAuthority('user')")
    public Result<Boolean> getFavoriteStatus(@RequestParam Long animeId) {
        return userService.getFavoriteStatus(animeId);
    }

    @GetMapping("/getFavoriteAnimes")
    @PreAuthorize("hasAuthority('user')")
    public Result<List<AnimeCardVO>> getFavoriteAnimes(@RequestParam String userId) {
        return userService.getFavoriteAnimes(userId);
    }

    @PostMapping("/getFavoriteAnimesByCondition")
    @PreAuthorize("hasAuthority('user')")
    public Result<List<AnimeFavoriteCardVO>> getFavoriteAnimesByCondition(
            @RequestParam int offset,
            @RequestParam int pageSize,
            @RequestBody FavoriteAnimeConditionDTO condition) {
        return userService.getFavoriteAnimesByCondition( offset, pageSize, condition);
    }

    @GetMapping("/updateWatchStatus")
    @PreAuthorize("hasAuthority('user')")
    public Result<String> updateWatchStatus(@RequestParam Long animeId,@RequestParam int status) {
        return userService.updateWatchStatus(animeId,status);
    }
}
