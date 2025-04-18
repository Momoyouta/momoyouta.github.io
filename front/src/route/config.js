import {createRouter, createWebHistory, useRoute} from "vue-router";
import {default as adminSideBar} from "../layout/components/admin/SideBar.vue";
import {default as userSideBar} from "../layout/components/user/SideBar.vue";
import {default as adminTopBar} from "../layout/components/admin/TopBar.vue";
import {default as userTopBar} from "../layout/components/user/TopBar.vue";
import {ROUTE_PATH} from "./pathset.js";
import AddAni from "../components/admin/animation/AddAni.vue";
import UpdateAni from "@/components/admin/animation/UpdateAni.vue";
import AnimationCard from "@/components/common/AnimationCard.vue";
import test from "@/components/test/AnicardTest.vue";
import WeekList from "@/components/home/WeekList.vue";
import WeeklistTest from "@/components/test/WeeklistTest.vue";
import WeeklistMG from "@/components/admin/WeeklistMG.vue";
import {apiUtils} from "@/common/apiUtils.js";
import HomePage from "@/page/HomePage.vue";
import AnimefollowPage from "@/page/AnimefollowPage.vue";
import DirectoryPage from "@/page/DirectoryPage.vue";
import RecentUpdatePage from "@/page/RecentUpdatePage.vue";
import UserTest from "@/page/UserTest.vue";
import AnimeDetail from "@/page/AnimeDetailPage.vue";
import AnimeSearchPage from "@/page/AnimeSearchPage.vue";
import RecgroundPage from "@/page/RecgroundPage.vue";
import Login from "@/page/Login.vue";
import LayOut from "@/layout/LayOut.vue";

const routes =[
    {path:'/login',component:Login},
    {
        path:'/',
        component:LayOut,
        children:[
            {path:'admin/:pathMatch(.*)*', components:{SideNavbar:adminSideBar,TopNavbar:adminTopBar}},
            {path:`admin/${ROUTE_PATH.ADMIN_ANI}/${ROUTE_PATH.ADMIN_ANI_ADD}`,components:{SideNavbar:adminSideBar,main:AddAni,TopNavbar:adminTopBar}},
            {path:`admin/${ROUTE_PATH.ADMIN_ANI}/${ROUTE_PATH.ADMIN_ANI_UPDATE}`,components:{SideNavbar:adminSideBar,main:UpdateAni,TopNavbar:adminTopBar}},
            {path:`admin/${ROUTE_PATH.ADMIN_WEEKLIST}`,components:{SideNavbar:adminSideBar,main:WeeklistMG,TopNavbar:adminTopBar}},
            {path:`admin/test/anicard`,components:{SideNavbar:adminSideBar,main:test,TopNavbar:adminTopBar}},
            {path:`admin/test/weeklist`,components:{SideNavbar:adminSideBar,main:WeeklistTest,TopNavbar:adminTopBar}},

            {path:`recmground`,components:{SideNavbar:userSideBar,main:RecgroundPage,TopNavbar:userTopBar}},
            {path:`animefollow`,components:{SideNavbar:userSideBar,main:AnimefollowPage,TopNavbar:userTopBar}},
            {path:`directory`,components:{SideNavbar:userSideBar,main:DirectoryPage,TopNavbar:userTopBar}},
            {path:`recentupdate`,components:{SideNavbar:userSideBar,main:RecentUpdatePage,TopNavbar:userTopBar}},
            {name:'getAnimeDetail' ,path:`ani/:animeid`,components:{SideNavbar:userSideBar,main:AnimeDetail,TopNavbar:userTopBar}
                ,props: {
                    SideNavbar: false,
                    main: true,
                    TopNavbar: false,
                }
            },
            {name:'searchAnime' ,path:`search/:keyword`,components:{SideNavbar:userSideBar,main:AnimeSearchPage,TopNavbar:userTopBar}
                ,props: {
                    SideNavbar: false,
                    main: true,
                    TopNavbar: false,
                }
            },
            {path:`test`,components:{SideNavbar:userSideBar,main:UserTest,TopNavbar:userTopBar}},
            {path:'home',components:{SideNavbar:userSideBar,main:HomePage,TopNavbar:userTopBar}},
        ]
    },


]

export  const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    next();
})