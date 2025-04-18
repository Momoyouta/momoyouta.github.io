package com.momoyouta.web_ani_server.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_common.utils.JwtUtil;
import com.momoyouta.web_ani_pojo.dto.ForgotPasswordDTO;
import com.momoyouta.web_ani_pojo.dto.RegisterDTO;
import com.momoyouta.web_ani_pojo.dto.UserTokenDTO;
import com.momoyouta.web_ani_pojo.entity.User;
import com.momoyouta.web_ani_server.service.UserService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

}
