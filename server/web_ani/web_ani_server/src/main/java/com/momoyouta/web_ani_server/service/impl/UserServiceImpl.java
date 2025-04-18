package com.momoyouta.web_ani_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_common.utils.JwtUtil;
import com.momoyouta.web_ani_pojo.VO.UserCacheVO;
import com.momoyouta.web_ani_pojo.dto.RegisterDTO;
import com.momoyouta.web_ani_pojo.dto.UserBaseInfoDTO;
import com.momoyouta.web_ani_pojo.dto.UserTokenDTO;
import com.momoyouta.web_ani_pojo.entity.Role;
import com.momoyouta.web_ani_pojo.entity.User;
import com.momoyouta.web_ani_pojo.entity.UserRole;
import com.momoyouta.web_ani_server.mapper.RoleMapper;
import com.momoyouta.web_ani_server.mapper.UserMapper;
import com.momoyouta.web_ani_server.mapper.UserRoleMapper;
import com.momoyouta.web_ani_server.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> registerByAccount(RegisterDTO registerDTO) {
        LambdaQueryWrapper<User> qw=new LambdaQueryWrapper<>();
        qw.select(User::getAccount).eq(User::getAccount, registerDTO.getAccount());
        if(userMapper.selectOne(qw)!=null){
            return Result.error(0,"账号已存在");
        }
        User user=User.builder()
                .id(UUID.randomUUID().toString())
                .account(registerDTO.getAccount())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .name(registerDTO.getName())
                .build();
        userMapper.insert(user);
        UserRole userRole=UserRole.builder()
                .id(UUID.randomUUID().toString())
                .roleId(2)
                .userId(user.getId())
                .build();
        userRoleMapper.insert(userRole);

        return Result.success("注册成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserTokenDTO> loginByAccount(String account, String password) throws JsonProcessingException {
        LambdaQueryWrapper<User> qw=new LambdaQueryWrapper<>();
        qw.select(User::getId,User::getAccount,User::getPassword).eq(User::getAccount, account);
        User user=userMapper.selectOne(qw);
        if(user==null){
            return Result.error(2,"账号未注册");
        }
        if(!passwordEncoder.matches(password,user.getPassword())){
            return Result.error(2,"密码错误");
        }
        LambdaQueryWrapper<UserRole> qw1=new LambdaQueryWrapper<>();
        qw1.select(UserRole::getRoleId,UserRole::getUserId).eq(UserRole::getUserId,user.getId());
        List<UserRole> userRoles=userRoleMapper.selectList(qw1);
        List<String> roles=new ArrayList<>();
        for(UserRole userRole:userRoles){
            int roleId=userRole.getRoleId();
            LambdaQueryWrapper<Role> roleQw=new LambdaQueryWrapper<>();
            roleQw.select(Role::getRoleNameEn).eq(Role::getId,roleId);
            roles.add(roleMapper.selectOne(roleQw).getRoleNameEn());
        }
        UserBaseInfoDTO baseInfo=getBaseInfo(user.getId());
        UserCacheVO userCacheVO=UserCacheVO.builder().id(user.getId()).identities(roles).build();
        String jsonUserCache=objectMapper.writeValueAsString(userCacheVO);
        log.info(jsonUserCache);
        String accessToken= JwtUtil.generateToken(jsonUserCache);
        UserTokenDTO tokenDTO=UserTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken("234")
                .userBaseInfo(baseInfo)
                .roles(roles)
                .build();
        return Result.success(tokenDTO);
    }

    @Override
    public UserBaseInfoDTO getBaseInfo(String userId) {
        User user=userMapper.selectById(userId);
        return UserBaseInfoDTO.builder()
                .name(user.getName())
                .birthday(user.getBirthday())
                .account(user.getAccount())
                .sex(user.getSex())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }


}
