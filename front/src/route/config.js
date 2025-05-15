import {createRouter, createWebHistory, useRoute} from "vue-router";
import {default as adminSideBar} from "../layout/admin/SideBar.vue";
import {default as adminTopBar} from "../layout/admin/TopBar.vue";
import {default as indexSidebar} from "../layout/index/SideBar.vue";
import {default as indexTopbar} from "../layout/index/TopBar.vue";
import {default as userTopbar} from "../layout/user/TopBar.vue";
import {default as userSidebar} from "../layout/user/SideBar.vue";
import {AUTH_PATH, ROUTE_PATH} from "./pathset.js";
import AddAni from "../components/admin/anime/AddAni.vue";
import UpdateAni from "@/components/admin/anime/UpdateAni.vue";
import AnimeCard from "@/components/common/AnimeCard.vue";
import test from "@/components/test/AnicardTest.vue";
import WeekList from "@/components/home/WeekList.vue";
import WeeklistTest from "@/components/test/WeeklistTest.vue";
import WeeklistMG from "@/components/admin/WeeklistMG.vue";
import {apiUtils} from "@/common/apiUtils.js";
import HomePage from "@/page/index/HomePage.vue";
import AnimefollowPage from "@/page/index/AnimefollowPage.vue";
import DirectoryPage from "@/page/index/DirectoryPage.vue";
import RecentUpdatePage from "@/page/index/RecentUpdatePage.vue";
import UserTest from "@/page/UserTest.vue";
import AnimeDetail from "@/page/index/AnimeDetailPage.vue";
import AnimeSearchPage from "@/page/index/AnimeSearchPage.vue";
import RecgroundPage from "@/page/index/RecgroundPage.vue";
import Login from "@/page/user/Login.vue";
import LayOut from "@/layout/LayOut.vue";
import EmptyComponent from "@/components/EmptyComponent.vue";
import UserProfile from "@/page/user/UserProfile.vue";
import UserFavorites from "@/page/user/UserFavorites.vue";
import UserUpdateInfo from "@/page/user/UserUpdateInfo.vue";
import {resultPopMsg} from "@/utils/resultPopMsg.js";

const routes =[
    {path:'/login',component:Login},
    {
        path:'/',
        component:LayOut,
        children:[
            {
                path: 'admin',
                components:{SideNavbar:adminSideBar, TopNavbar:adminTopBar, main:EmptyComponent},
                children:[
                    {
                        path:ROUTE_PATH.ADMIN_ANI,
                        children:[
                            {path:ROUTE_PATH.ADMIN_ANI_ADD,component:AddAni},
                            {path:ROUTE_PATH.ADMIN_ANI_UPDATE,component:UpdateAni},
                        ]
                    },
                    {path:ROUTE_PATH.ADMIN_WEEKLIST, component:WeeklistMG},
                    {
                        path:'test',
                        children:[
                            {path:'anicard',component:AnimeDetail},
                            {path:'weeklist',component:WeeklistTest},
                        ]
                    },
                ]
            },
            //index
            {
                path:'',
                components:{SideNavbar:indexSidebar, TopNavbar:indexTopbar, main:EmptyComponent},
                children:[
                    {path:`recmground`,component:RecgroundPage},
                    {path:`animefollow`,component:AnimefollowPage},
                    {path:`directory`,component:DirectoryPage},
                    {path:`recentupdate`,component:RecentUpdatePage},
                    {name:'getAnimeDetail' ,path:`ani/:animeid`,component:AnimeDetail,props: true},
                    {name:'searchAnime' ,path:`search/:keyword`,component:AnimeSearchPage,props: true},
                    {path:'test',component:UserTest},
                    {path:'home',component:HomePage},
                ]
            },
            //用户主页相关
            {
                path:'user',
                components:{SideNavbar:userSidebar,main:EmptyComponent,TopNavbar:userTopbar},
                children:[
                    {path:ROUTE_PATH.USER_PROFILE,component:UserProfile},
                    {path:ROUTE_PATH.USER_FAVORITE,component:UserFavorites},
                    {path:ROUTE_PATH.USER_UPDATE_INFO,component:UserUpdateInfo},
                ]
            },
        ]
    },


]

export  const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    if(AUTH_PATH.some(item=>item===to.path)){
        const token=localStorage.getItem('accessToken');
        if(!token){
            resultPopMsg(0,'请先登录');
            next('/login');
        }
    }
    next();
})