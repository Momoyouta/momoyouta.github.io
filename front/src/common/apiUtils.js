export const apiUtils ={
    BASIC:import.meta.env.VITE_URL,
    ADD_ANI:'/admin/ani/add',
    SEARCH_ANI:'/ani/search',
    KEYWORD_SEARCH:'/ani/search/keyword',
    SEARCH_PAGE_ANI:'/ani/search/page',
    RECENT_UPDATE_ANI:'/ani/update/recently',
    UPDATE_ANI:'/admin/ani/update',
    LOCK_ANI:'/admin/ani/update/lock',
    DEL_ANI:'/admin/ani/update/del',
    HOT_TAG:'/tag/hot',

    DIR_SELECT_ANI:'/ani/dir/select',
    FOLLOW_ANI_INFO:'/user/follow',

    WEEKLIST_DAY_RQ:'/weeklist/request',
    WEEKLIST_DAY_UPDATE:'/admin/weeklist/update',
    WEEKLIST_QUARTER_GET:'/admin/weeklist/request/quarter',

    BANGUMI_BASE_ADD:'/admin/bangumi/baseadd',
    BANGUMI_UPDATE_EPS:'/admin/bangumi/updateEps',
    USER_GET_ANIME_DETAIL:'/user/ani/getdetail',

    login:'/user/login',
    register:'/user/register',
    forgotPassword:'/user/forgotPassword',
    getCaptcha:'/user/captcha',

    user:{
        updateUserInfo:         `/user/updateUserInfo`,
        followAnime:         `/user/followAnime`,
        unFollowAnime:         `/user/unFollowAnime`,
        updateFollowProcess:         `/user/updateFollowProcess`,
        finishFollow:         `/user/finishFollow`,
        score:         `/user/score`,
        favorite:         `/user/favorite`,
        unFavorite:         `/user/unFavorite`,
        getFavoriteStatus:         `/user/getFavoriteStatus`,
        unfavorite:         `/user/unFavorite`,
        getFavoriteAnimes:         `/user/getFavoriteAnimes`,
        getFavoriteAnimesByCondition: `/user/getFavoriteAnimesByCondition`,
        updateWatchStatus:  `/user/updateWatchStatus`,
    },
    weeklist:{
        getWeekListStore:         `/user/weeklist/getWeekListStore`,
    }
}