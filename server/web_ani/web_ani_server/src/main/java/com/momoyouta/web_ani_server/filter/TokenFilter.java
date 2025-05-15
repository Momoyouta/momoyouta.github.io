package com.momoyouta.web_ani_server.filter;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.momoyouta.web_ani_common.enumm.KeyProperties;
import com.momoyouta.web_ani_common.enumm.ResponseCode;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_common.utils.JwtUtil;
import com.momoyouta.web_ani_common.utils.WebUtil;
import com.momoyouta.web_ani_pojo.VO.UserCacheVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader(KeyProperties.TOKEN_HEADER);
        if(token == null||token.isEmpty()) { //无token
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (ExpiredJwtException e) { //异常处理
            String errorInfo = "登录认证已经过期";
            WebUtil.renderString(response, JSONObject.toJSONString(Result.error(ResponseCode.TOKEN_EXPIRED,errorInfo))); //立即返回请求工具类，直接返回错误信息
            return;
        } catch (Exception e) { //异常处理
            String errorInfo = "accessToken解析失败";
            WebUtil.renderString(response, JSONObject.toJSONString(Result.error(ResponseCode.TOKEN_INVALID,errorInfo)));
            return;
        }
        //需注意JSON解析，存入JWT时存入JSON，方便解析
        UserCacheVO userCacheVO = objectMapper.readValue(claims.getSubject(), UserCacheVO.class);
        String userId=userCacheVO.getId();
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String role: userCacheVO.getIdentities()){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        UsernamePasswordAuthenticationToken authenticationToken = //封装Authentication实现类并存入上下文
                new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
