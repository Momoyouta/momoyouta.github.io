package com.momoyouta.web_ani_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momoyouta.web_ani_common.enumm.ResponseCode;
import com.momoyouta.web_ani_common.result.Result;
import com.momoyouta.web_ani_common.utils.JwtUtil;
import com.momoyouta.web_ani_pojo.VO.AnimeCardVO;
import com.momoyouta.web_ani_pojo.VO.AnimeFavoriteCardVO;
import com.momoyouta.web_ani_pojo.VO.UserCacheVO;
import com.momoyouta.web_ani_pojo.dto.FavoriteAnimeConditionDTO;
import com.momoyouta.web_ani_pojo.dto.RegisterDTO;
import com.momoyouta.web_ani_pojo.dto.UserBaseInfoDTO;
import com.momoyouta.web_ani_pojo.dto.UserTokenDTO;
import com.momoyouta.web_ani_pojo.entity.*;
import com.momoyouta.web_ani_server.mapper.*;
import com.momoyouta.web_ani_server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AnimeMapper animeMapper;
    @Autowired
    private FollowAnimeMapper followAnimeMapper;
    @Autowired
    private FavoriteAnimeMapper favoriteAnimeMapper;
    @Autowired
    private EpisodeMapper episodeMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> registerByAccount(RegisterDTO registerDTO) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.select(User::getAccount).eq(User::getAccount, registerDTO.getAccount());
        if (userMapper.selectOne(qw) != null) {
            return Result.error(ResponseCode.ERROR, "账号已存在");
        }
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .account(registerDTO.getAccount())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .name(registerDTO.getName())
                .build();
        userMapper.insert(user);
        UserRole userRole = UserRole.builder()
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
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.select(User::getId, User::getAccount, User::getPassword).eq(User::getAccount, account);
        User user = userMapper.selectOne(qw);
        if (user == null) {
            return Result.error(ResponseCode.ERROR, "账号未注册");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error(ResponseCode.ERROR, "密码错误");
        }
        LambdaQueryWrapper<UserRole> qw1 = new LambdaQueryWrapper<>();
        qw1.select(UserRole::getRoleId, UserRole::getUserId).eq(UserRole::getUserId, user.getId());
        List<UserRole> userRoles = userRoleMapper.selectList(qw1);
        List<String> roles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            int roleId = userRole.getRoleId();
            LambdaQueryWrapper<Role> roleQw = new LambdaQueryWrapper<>();
            roleQw.select(Role::getRoleNameEn).eq(Role::getId, roleId);
            roles.add(roleMapper.selectOne(roleQw).getRoleNameEn());
        }
        UserBaseInfoDTO baseInfo = getBaseInfo(user.getId());
        UserCacheVO userCacheVO = UserCacheVO.builder().id(user.getId()).identities(roles).build();
        String jsonUserCache = objectMapper.writeValueAsString(userCacheVO);
        log.info(jsonUserCache);
        String accessToken = JwtUtil.generateToken(jsonUserCache);
        UserTokenDTO tokenDTO = UserTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken("234")
                .userBaseInfo(baseInfo)
                .roles(roles)
                .build();
        return Result.success(tokenDTO);
    }

    @Override
    public UserBaseInfoDTO getBaseInfo(String userId) {
        User user = userMapper.selectById(userId);
        return UserBaseInfoDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .birthday(user.getBirthday())
                .account(user.getAccount())
                .sex(user.getSex())
                .avatarUrl(user.getAvatarUrl())
                .email(user.getEmail())
                .signature(user.getSignature())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateUserInfo(UserBaseInfoDTO info) {
        User updateUser = new User();
        BeanUtils.copyProperties(info, updateUser);
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        updateUser.setId(userId);
        userMapper.updateById(updateUser);
        return Result.success("修改个人信息成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> unFollowAnimeByList(List<Long> animeIds) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        LambdaQueryWrapper<FollowAnime> qw = new LambdaQueryWrapper<>();
        if (animeIds.isEmpty()) {
            return Result.error(ResponseCode.ERROR, "空操作ID");
        }
        qw.eq(FollowAnime::getUserId, userId).in(FollowAnime::getAnimeId, animeIds);
        List<FollowAnime> followAnimeList = followAnimeMapper.selectList(qw);
        if (followAnimeList.size() != animeIds.size()) {
            return Result.error(ResponseCode.ERROR, "非法操作,存在未追番剧");
        }
        List<String> followAnimeIds = new ArrayList<>();
        for (FollowAnime followAnime : followAnimeList) {
            followAnimeIds.add(followAnime.getId());
        }
        changeFavoriteStatus(animeIds, 2);
        followAnimeMapper.deleteBatchIds(followAnimeIds);
        return Result.success("取消追番成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> followAnimeByList(List<Long> animeIds) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<Anime> animes = animeMapper.selectList(new LambdaQueryWrapper<Anime>().in(Anime::getId, animeIds));
        if (animes.size() != animeIds.size()) {
            return Result.error(ResponseCode.ERROR, "非法操作,含不存在番剧");
        }
        List<FollowAnime> followAnimeList = new ArrayList<>();
        for (Long animeId : animeIds) {
            followAnimeList.add(FollowAnime.builder()
                    .userId(userId)
                    .animeId(animeId)
                    .process(0)
                    .build());
        }
        changeFavoriteStatus(animeIds, 1);
        followAnimeMapper.insert(followAnimeList);
        return Result.success("追番成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateFollowProcess(Long animeId, int process) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        LambdaQueryWrapper<FollowAnime> qw = new LambdaQueryWrapper<>();
        qw.eq(FollowAnime::getUserId, userId).eq(FollowAnime::getAnimeId, animeId);
        FollowAnime followAnime = FollowAnime.builder().process(process).build();
        followAnimeMapper.update(followAnime, qw);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> finishFollow(Long animeId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        changeFavoriteStatus(List.of(animeId), 3);
        LambdaQueryWrapper<FollowAnime> qw = new LambdaQueryWrapper<>();
        qw.eq(FollowAnime::getUserId, userId).eq(FollowAnime::getAnimeId, animeId);
        followAnimeMapper.delete(qw);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> score(Long animeId, BigDecimal score) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        LambdaQueryWrapper<FavoriteAnime> qw = new LambdaQueryWrapper<>();
        qw.eq(FavoriteAnime::getAnimeId, animeId).eq(FavoriteAnime::getUserId, userId);
        FavoriteAnime favoriteAnime = favoriteAnimeMapper.selectOne(qw);
        if (favoriteAnime == null) {
            FavoriteAnime favoriteTp = FavoriteAnime.builder()
                    .animeId(animeId)
                    .userId(userId)
                    .score(score)
                    .favoriteStatus(0)
                    .status(1)
                    .build();
            favoriteAnimeMapper.insert(favoriteTp);
        } else {
            favoriteAnime.setScore(score);
            favoriteAnimeMapper.updateById(favoriteAnime);
        }
        return Result.success();
    }

    @Override
    public Result<Boolean> getFavoriteStatus(Long animeId) {
        //查找favorite表看用户是否收藏，favorite_status=1代表收藏
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        LambdaQueryWrapper<FavoriteAnime> qw = new LambdaQueryWrapper<>();
        qw.eq(FavoriteAnime::getUserId, userId).eq(FavoriteAnime::getAnimeId, animeId);
        FavoriteAnime favoriteAnime = favoriteAnimeMapper.selectOne(qw);
        if (favoriteAnime == null) {
            return Result.success(false);
        }
        if (favoriteAnime.getFavoriteStatus() == 1) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    @Override
    public Result<List<AnimeCardVO>> getFavoriteAnimes(String userId) {
        log.info(userId);
        //先从favorite中获取用户收藏的番剧,从anime和episodes表中获取信息填入AinimeCardVO
        LambdaQueryWrapper<FavoriteAnime> qw = new LambdaQueryWrapper<>();
        qw.eq(FavoriteAnime::getUserId, userId).eq(FavoriteAnime::getFavoriteStatus, 1);
        List<FavoriteAnime> favoriteAnimes = favoriteAnimeMapper.selectList(qw);
        List<AnimeCardVO> animeCardVOS = new ArrayList<>();
        for (FavoriteAnime favoriteAnime : favoriteAnimes) {
            Anime anime = animeMapper.selectById(favoriteAnime.getAnimeId());
            if (anime == null) {
                continue;
            }
            if (favoriteAnime.getScore().intValue() != 0) {
                anime.setScore(favoriteAnime.getScore());
            }
            LambdaQueryWrapper<Episode> qwEp = new LambdaQueryWrapper<>();
            qwEp.select(Episode::getEp, Episode::getTotalEps).eq(Episode::getAnimeId, anime.getId());
            Episode episode = episodeMapper.selectOne(qwEp);
            AnimeCardVO animeCardVO = AnimeCardVO.builder()
                    .anime(anime)
                    .ep(episode.getEp())
                    .totalEps(episode.getTotalEps())
                    .build();
            animeCardVOS.add(animeCardVO);
        }
        return Result.success(animeCardVOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> favorite(Long animeId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Anime anime = animeMapper.selectById(animeId);
        if (anime == null) {
            return Result.error(ResponseCode.ERROR, "番剧不存在");
        }
        FavoriteAnime favoriteAnime = FavoriteAnime.builder()
                .userId(userId)
                .animeId(animeId)
                .score(new BigDecimal(0))
                .favoriteStatus(1)
                .favoriteTime(System.currentTimeMillis() / 1000)
                .status(0).build();
        LambdaQueryWrapper<FavoriteAnime> qw = new LambdaQueryWrapper<>();
        qw.eq(FavoriteAnime::getAnimeId, animeId).eq(FavoriteAnime::getUserId, userId);
        FavoriteAnime faAnime = favoriteAnimeMapper.selectOne(qw);
        if (faAnime == null) {
            favoriteAnimeMapper.insert(favoriteAnime);
        } else if (faAnime.getFavoriteStatus() == 0) {
            faAnime.setFavoriteStatus(1);
            favoriteAnimeMapper.updateById(faAnime);
        } else {
            return Result.error(ResponseCode.ERROR, "番剧已收藏");
        }
        return Result.success();
    }

    @Override
    public Result<String> unFavorite(Long animeId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        LambdaUpdateWrapper<FavoriteAnime> qw = new LambdaUpdateWrapper<>();
        qw.eq(FavoriteAnime::getAnimeId, animeId).eq(FavoriteAnime::getUserId, userId).set(FavoriteAnime::getFavoriteStatus, 0);
        favoriteAnimeMapper.update(qw);
        return Result.success();
    }

    @Override
    public Result<List<AnimeFavoriteCardVO>> getFavoriteAnimesByCondition( int offset, int pageSize, FavoriteAnimeConditionDTO condition) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<AnimeFavoriteCardVO> list = animeMapper.selectFavoriteAnimesByCondition(userId, offset, pageSize, condition);
        return Result.success(list);
    }

    @Override
    public Result<String> updateWatchStatus(Long animeId, int status) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        LambdaUpdateWrapper<FavoriteAnime> qw = new LambdaUpdateWrapper<>();
        qw.eq(FavoriteAnime::getUserId, userId).eq(FavoriteAnime::getAnimeId, animeId).set(FavoriteAnime::getStatus, status);
        favoriteAnimeMapper.update(qw);
        return Result.success("成功修改状态");
    }

    //修复报错

    private boolean changeFavoriteStatus(List<Long> animeIds, int status) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<FavoriteAnime> favoriteAnimes = new ArrayList<>();
        for (Long animeId : animeIds) {
            favoriteAnimes.add(FavoriteAnime.builder()
                    .status(status)
                    .userId(userId)
                    .animeId(animeId)
                    .build());
        }
        try {
            favoriteAnimeMapper.insertOrUpdateBatch(favoriteAnimes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

