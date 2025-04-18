package com.momoyouta.web_ani_server.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.momoyouta.web_ani_common.enumm.KeyProperties;
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
import org.springframework.beans.factory.annotation.Autowired;
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
        if(token == null||token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (ExpiredJwtException e) {
            String errorInfo = "accessToken已经过期";
            WebUtil.renderString(response, errorInfo);
            return;
        } catch (Exception e) {
            String errorInfo = "accessToken解析失败";
            WebUtil.renderString(response, errorInfo);
            return;
        }
        UserCacheVO userCacheVO = objectMapper.readValue(claims.getSubject(), UserCacheVO.class);
        String userId=userCacheVO.getId();
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String role: userCacheVO.getIdentities()){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
