<template>
  <div class="wrapper" style="display: flex; justify-content: space-between;width: 100%;">
    <div class="content" style="flex: 3;min-height: 90vh;margin-right: 30px">
      <div class="profile" style="display: flex; justify-content: flex-start;">
        <el-image src="https://s1.imagehub.cc/images/2025/05/13/2db4ba2782f81ee45ff4e3b68d6a8f66.md.jpg"
                  lazy
                  style="width: 180px; height: 180px;"/>
        <div style="margin-left: 20px">
          <p style="font-size: 1.5rem">{{user.name}}</p>
          <p style="font-size: 0.9rem;display: flex;align-items: center">Birthday：{{userBirthday}}
            <el-icon style="vertical-align: center"><Female v-if="user.sex===0"/><Male v-else/></el-icon></p>
          <el-divider style="border-color: #2c2c2c;margin: 10px 0"/>
          <el-text :line-clamp="4">
            {{user.signature}}
          </el-text>
        </div>
      </div>

      <el-divider style="border-color: #2c2c2c"/>

      <div style="display: flex; justify-content: space-between;min-height: 200px;margin-bottom: 30px;">
        <el-tabs class="recoment"
                 tab-position="right"
                 style="flex: 15;margin-right: 10px;position: relative;">
          <el-tab-pane label="本季" style="height: 100%;display: flex;justify-content: space-around;background-color: var(--side-bg);padding:0 10px;align-items: center">
              <AnimeCard style="width: 18%;font-size: 0.9rem" :ani="ani.data" :show-con="ani.show" v-for="ani in animesRecoment.season" :key="ani.id"></AnimeCard>
          </el-tab-pane>
          <el-tab-pane label="生涯" style="height: 100%;display: flex;justify-content: space-around;background-color: var(--side-bg);padding:0 10px;align-items: center">
            <AnimeCard style="width: 18%;font-size: 0.9rem" :ani="ani.data" :show-con="ani.show" v-for="ani in animesRecoment.all" :key="ani.id"></AnimeCard>
          </el-tab-pane>
          .
        </el-tabs>
        <div style="color: white;flex: 1;background-color: var(--label);display: flex; align-items: center;border-radius: 4px">
          <div style="margin:auto ;writing-mode: vertical-rl;text-align: center;font-size: 1.5rem">个  人  推  荐</div>
        </div>
      </div>

      <div style="display: flex; justify-content: space-between;min-height: 200px;max-height:440px;margin-bottom: 30px;">
        <el-tabs class="recoment"
                 @tab-click="dialogVisible = true"
                 tab-position="right"
                 style="flex: 15;margin-right: 10px;position: relative;">
          <el-tab-pane :label="userId===user.id?'编辑':'个人'"
                       style="height: 100%;display: flex;background-color: var(--side-bg);align-items: center">
            <el-scrollbar style="padding:10px 10px;">
            <List :items="animeTp2" :n="6" :item-width="15">
              <template #default="{ item: anime }">
                <div style="display: flex;width: 100%;flex-wrap: wrap;padding: 5px;border-radius: 10px;background-color: rgba(227,102,68,0.3)" >
                  <div class="followCover" style="position: relative;" @click="handleGotoAnimeDetail(anime.id)">
                    <el-tooltip placement="top" effect="light" :content="anime.name">
                      <img :src="anime.image" alt="" style="object-fit: contain;width: 100%;height: 100%;"/>
                    </el-tooltip>
                    <div class="imgMask">
                      [{{anime.schedule}}/{{anime.ep}}]
                    </div>
                  </div>
                  <div style="height: 0.35rem;border-radius: 0.35rem;background-color: white;width: 100%;">
                    <div :style="{width:`${anime.schedule/anime.ep*100}%`}" class="slider"></div>
                  </div>
                </div>
              </template>
            </List>
            </el-scrollbar>
          </el-tab-pane>
          <el-tab-pane></el-tab-pane>
        </el-tabs>
        <el-dialog v-model="dialogVisible" width="60rem">
          <FollowBox :animeTp="animeTp2" style="height: 25rem"/>
        </el-dialog>
        <div style="color: white;flex: 1;background-color: var(--label);display: flex; align-items: center;border-radius: 4px">
          <div style="margin:auto ;writing-mode: vertical-rl;text-align: center;font-size: 1.5rem">正 在 观 看</div>
        </div>
      </div>
    </div>
    <div  ref="sideNavBG" style="flex:1;min-height:90vh;position: relative">
      <el-card class="sideNav"
               :style="sideNavStyle"
               body-style=""
               shadow="never">
        <div style="width: 100%;display: flex;justify-content: center;">
          <h2 class="favoriteBoxTitle" style="text-align: center;width: fit-content;position: relative">收藏列表</h2>
        </div>
        <div>
        <el-table :data="animesFavorite" style="width: 100%;height: 100%;margin-top: 10px;background-color: #fdfdfd"
                  show-overflow-tooltip
                  :row-style="favoriteRowStyle"
                  :header-cell-style="favoriteHeaderRowStyle"
                  >
          <el-table-column align="center" type="index" label="序号" min-width="40"/>
          <el-table-column align="center" prop="name" label="名称">
          </el-table-column>
          <el-table-column align="center" prop="score" label="评分" min-width="40">
            <template #default="{row}">
              <el-tag type="danger">{{row.score}}</el-tag>
            </template>
          </el-table-column>

        </el-table>
        </div>
      </el-card>
    </div>

  </div>
</template>

<script setup>
import {computed, onBeforeUnmount, onMounted, reactive, ref, render} from "vue";
import {Female, Male} from "@element-plus/icons-vue";
import AnimeCard from "@/components/common/AnimeCard.vue";
import {router} from "@/route/config.js";
import List from "@/components/common/List.vue";
import FollowBox from "@/components/common/FollowBox.vue";
import {useCommonStore} from "@/store/commonStore.js";
import {apiUtils} from "@/common/apiUtils.js";
import {ElMessage} from "element-plus";
import axios from "axios";
import request from "@/utils/request.js";
import {tranToCard} from "@/utils/animeCard.js";
const props=defineProps({
  userId: { type: String, required: true },
})

const user=reactive({
  id: null,
  name: null,
  email: null,
  signature:null,
  birthday: null,
  sex: null,
})
const animeTp={
  show: {
    score: true,
    state: true,
    date: false,
    name: true,
  },
  data:{
    id:506922,
    name:'深夜重拳',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
    ep:13,
    score:9.7,
    updateTime:1,
  }
}
const animeTp2=[
  {
    id:2106085945,
    name:'BanG Dream! Ave Mujica',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/77/c3/454684_ZH5tU.jpg',
    ep:13,
    doing: 10086,
    schedule: 10
  },
  {
    id:2106085705,
    name:'噗妮露是可爱史莱姆',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/fe/ab/470045_zA9BA.jpg',
    ep:12,
    doing: 61086,
    schedule: 6
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },

  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  }
]
const animes=reactive([animeTp,animeTp,animeTp,animeTp,animeTp])
const animesFavorite=reactive([]);
const animesRecoment=reactive({season:[],all:[]});
const tabPosition=ref('personal');
const axs=axios.create({
  baseURL: `${apiUtils.BASIC}`,
  timeout:5000,
})
//初始样式
const sideNavWidth=ref()
const sideNavBG=ref(null);
onMounted(async ()=>{
  const userInfo = JSON.parse(localStorage.getItem('userInfo')) || {}
  Object.assign(user,userInfo);
  window.addEventListener('scroll',handleScroll);
  sideNavWidth.value=sideNavBG.value.offsetWidth;
  await getFavoriteAnimes();
});
onBeforeUnmount(()=>{
  window.removeEventListener('scroll',handleScroll);
})
const userBirthday = computed(()=>{
  const date = new Date(Number(user.birthday)*1000);
  const month=date.getMonth()+1;
  const day=date.getDay();
  return month+'月'+day+'日';
})
function handleGotoAnimeDetail(id){
  router.push({
    name:'getAnimeDetail',
    params:{
      animeid:id,
    },
  });
}

//追番编辑器
const dialogVisible = ref(false)

//收藏栏&推荐
const commonStore=useCommonStore()
const isFixed = ref(true);
function handleScroll(){
  const scrollTop = window.scrollY || document.documentElement.scrollTop;
  isFixed.value = scrollTop < 300;
}
async function getFavoriteAnimes(){
  request({
    url: apiUtils.user.getFavoriteAnimes,
    method: 'get',
    params: {
      userId: props.userId,
    }
  }).then(res => {
    if (res.data.code === 1) {
      animesFavorite.push(...res.data.data.map(anime=>{
        return {
          ...anime.anime,
          ep:anime.ep,
          end:anime.ep===anime.totalEps?1:0,
        }
      }));
      console.log(animesFavorite);
      animesFavorite.sort((a,b)=>{
        return a.score-b.score;
      });
      recomentInit();
    } else {
      ElMessage({type: 'error', message: '获取收藏列表失败'});
    }
  }).catch(err => {
    console.log(err);
    ElMessage({type: 'error', message: '获取收藏列表失败'});
  })
}
async function recomentInit(){
  if(commonStore.seasonAnime.length===0) {
    await axs.get(apiUtils.weeklist.getWeekListStore)
        .then(res => {
          if (res.data.code !== 1) {
            ElMessage({type: 'error', message: '获取本季追番失败'});
          } else {
            commonStore.seasonAnime = res.data.data;
          }
        })
        .catch(err => {
          console.log(err);
          ElMessage({type: 'error', message: '获取本季追番失败'});
        });
  }
  let seasonCount=0;
  let allCount=0;
  animesFavorite.some(item=>{
    if(seasonCount<6){
      const index=commonStore.seasonAnime.findIndex(i=>i.animeId===item.id);
      if(index!==-1){
        animesRecoment.season.push({
          data: {...item},
          show: {
            score: true,
            state: true,
            date: false,
            name: true,
          }
        });
        seasonCount++;
      }
    }
    if(allCount<6){
      animesRecoment.all.push({
        data: {...item},
        show: {
          score: true,
          state: true,
          date: false,
          name: true,
        }
      });
      allCount++;
    }
    if(allCount>=6&&seasonCount>=6){
      return true;
    }
  })
}
const sideNavStyle=computed(()=>{
  let res={
    position: isFixed.value ? 'fixed' : 'absolute',
    right: isFixed.value ? '2.5rem' : '0',
    width: sideNavWidth.value+'px'
  }
  if(isFixed.value){
    res['top']='3.6rem';
  }else{
    res['top']=`${window.scrollY}px`;
    res['flex']='1';
  }
  return res;
})
const favoriteRowStyle=({rowIndex })=>{
  return {
    'background-color': rowIndex%2==0?'#efe2ca':'#e6d8bf',
  }
}
const favoriteHeaderRowStyle=()=>{
  return {
    'background-color': '#e6d8bf !important',
  }
}
</script>

<style scoped>
.profile{
  background: linear-gradient(to top,var(--side-bg) 0%, var(--base-bg) 15%);
}
.recoment:deep(.el-tabs__nav-scroll){
  padding-top: 40px !important;
}
.slider{
  background-color: #8686f8;
  border-radius: 0.35rem;
  height: 100%;
}
.followCover{
  border-radius: 10px;
  aspect-ratio: var(--anime-cover-ratio);
  overflow: hidden;
  margin-bottom: 5px;
  cursor: pointer;
}
.imgMask{
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 35%;
  background: linear-gradient(to top, #302f2f 0%,rgba(0,0,0,0) 100%);
  text-align: center;
  color: white;
  padding-top: 0.5rem;
  cursor: pointer;
  z-index: 99;
}
.sideNav{
  min-height:90vh;
  z-index:99;
  transition: none;
  background-color: var(--side-bg);
}
.favoriteBoxTitle::after{
  content: '';
  position: absolute;
  width: 0.5rem;
  height: 0.2rem;
  background-color: var(--btn-login);
  bottom: 40%;
  right: -1rem;
}
.favoriteBoxTitle::before{
  content: '';
  position: absolute;
  width: 0.5rem;
  height: 0.2rem;
  background-color: var(--btn-login);
  bottom: 40%;
  left: -1rem;
}
</style>