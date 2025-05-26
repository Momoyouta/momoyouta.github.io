<template>
  <div style="border-bottom: var(--input-base) solid 0.3rem;margin-bottom: 0.5rem">
    <span style="font-size: 2.5rem;font-weight: bold;padding-top: 0.5rem;border-bottom: var(--banner-active) solid 0.3rem">个人收藏</span>
  </div>
  <div style="display: flex;border-bottom: var(--input-base) solid 4px;margin-bottom: 0.25rem">
    <div style="width: 8%;padding-top: 0.5rem">
      <div style="padding-top: 0.2rem;font-weight: bold">
        状态
        <span class="iconfont icon-you" style="color: var(--btn-login)"></span>
      </div>
    </div>
    <TagList @propSelect="changeCondition(1,$event)" style="flex: 1;padding-top: 0.5rem" :isChoose="true" :items="statusItems"></TagList>
  </div>
  <div style="display: flex;border-bottom: var(--input-base) solid 4px;margin-bottom: 1rem">
    <div style="width: 8%;padding-top: 0.5rem">
      <div style="padding-top: 0.2rem;font-weight: bold">
        排序
        <span class="iconfont icon-you" style="color: var(--btn-login)"></span>
      </div>
    </div>
    <TagList @propSelect="changeCondition(2,$event)" style="flex: 1;padding-top: 0.5rem" :isChoose="true" :items="sortItems"></TagList>
  </div>
  <div>
    <ListUl :items="favoriteAnimes" :n="3" :itemWidth="32" style="width: 100%;height: 90vh">
      <template #default="{ item: anime }">
        <div style="display: flex;width: 100%;position: relative">
          <AnimeCard :ani="anime" :showCon="showCon" style="width: 40%;"/>
          <div style="position: relative;padding-left: 10px;width: 60%;">
            <div style="font-size: 16px">{{anime.name}}</div>
            <el-text style="font-size: 14px;margin-top: 10px;width: 100%" :line-clamp="3">{{anime.description}}</el-text>
              <div style="flex: 1;font-size: 14px;color: #606266;">
                <div style="margin-top: 8px">番剧·日本</div>
                <div style="margin-top: 4px">{{formatDate(anime.favoriteTime*1000)}}收藏</div>
                <div style="margin-top: 4px">{{statuses[anime.status]}}</div>
                <div style="margin-top: 4px">个人评分·{{anime.userScore}}</div>
              </div>
          </div>
          <el-dropdown placement="top-start" style="position: absolute;bottom: 4%;right: 10px;">
            <el-tag type="danger" size="small"><el-icon><More /></el-icon></el-tag>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="changeStatus(anime.id,2)">标记为看过</el-dropdown-item>
                <el-dropdown-item @click="changeStatus(anime.id,1)">标记为在看</el-dropdown-item>
                <el-dropdown-item>加入追番</el-dropdown-item>
                <el-dropdown-item @click="unfavorite(anime.id)">取消收藏</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </template>
    </ListUl>
  </div>
</template>

<script setup>

import {onMounted, onUnmounted, reactive, ref} from "vue";
import request from "@/utils/request.js";
import {apiUtils} from "@/common/apiUtils.js";
import {ElMessage} from "element-plus";
import AnimeCard from "@/components/common/AnimeCard.vue";
import ListUl from "@/components/common/ListUl.vue";
import {formatDate} from "../../utils/commonHook.js";
import {More} from "@element-plus/icons-vue";
import TagList from "@/components/common/TagList.vue";
import {throttle} from "@/utils/throttle.js";
import {resultPopMsg} from "@/utils/resultPopMsg.js";
const showCon={
  score: true,
  state: true,
  date: false,
  name: false
}
const animeTp={
  id:506922,
  name:'深夜重拳',
  image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
  description:'深夜重拳是一个关于拳击的故事，讲述了主人公在拳击场上的奋斗与成长。',
  score:9.7,
  updateTime:1,
  ep: 13,
  totalEps: 13,
  end: 1, //是否完结
  status:1,//观看状态
  userScore: 8.1, //用户评分
  favoriteTime: 1238127330, //收藏时间
}
const favoriteAnimes = reactive([]);
onMounted(async ()=>{
  window.addEventListener("scroll",hadnleWindowScroll)
  getFavoriteAnimes();
})
onUnmounted(()=>{
  window.removeEventListener("scroll",hadnleWindowScroll)
})
const sortItems=reactive(['收藏时间','大众评分','个人评分']);
const statusItems=reactive(['全部','在看','看过','弃置']);
const pageNow = ref(0);
const pageSize = ref(18);
let isAll=0;
const statuses=['未看','在看','看过','弃置']
const condition=reactive({
  status: 0,//'在看','看过','弃置'
  orderr: 0, //0：时间 1：评分
})
const hadnleWindowScroll=throttle(()=>{
  const scrollTop=window.scrollY;
  const clientHeight=window.innerHeight;
  const scrollHeight =document.documentElement.scrollHeight;
  if(Math.ceil(scrollTop+clientHeight)>=scrollHeight&&isAll===0){
    pageNow.value++;
    getFavoriteAnimes();
    ElMessage({
      type:"success",
      msg:"触发"
    });
  }
},100);
function clearAry(){
  pageNow.value=0;
  favoriteAnimes.length=0;
  isAll=0;
}
function getFavoriteAnimes(){
  request({
    url: apiUtils.user.getFavoriteAnimesByCondition,
    method: 'post',
    params: {
      offset: pageNow.value*pageSize.value,
      pageSize: pageSize.value,
    },
    data:{
      ...condition,
    }
  }).then(res => {
    if (res.data.code === 1) {
      const data=res.data.data;
      if(data.length<pageSize.value){
        isAll=1;
      }
      favoriteAnimes.push(...data);
    } else {
      ElMessage({type: 'error', message: '获取收藏列表失败'});
    }
  }).catch(err => {
    console.log(err);
    ElMessage({type: 'error', message: '获取收藏列表失败'});
  })
}
function changeCondition(select,condi){
  if(select===1){
    condition.status=statusItems.findIndex(item=>item===condi);
  }else{
    condition.orderr=sortItems.findIndex(item=>item===condi);
  }
  clearAry();
  getFavoriteAnimes();
}
function unfavorite(animeId){
  request({
    url:apiUtils.user.unfavorite,
    method:'get',
    params:{
      animeId:animeId,
    }
  }).then((res)=>{
    if(res.data.code===1){
      clearAry();
      getFavoriteAnimes();
      resultPopMsg(1,'取消收藏成功');
    }else{
      resultPopMsg(0,res.data.msg);
    }
  }).catch((err)=>{
    console.log(err);
    resultPopMsg(0,res.data.msg);
  });
}
function changeStatus(animeId,status){
  request({
    method:'get',
    url:apiUtils.user.updateWatchStatus,
    params:{
      animeId,
      status,
    }
  }).then((res)=>{
    if(res.data.code===1){
      const ret=favoriteAnimes.find(item=>item.id===animeId);
      ret.status=status;
      resultPopMsg(1,'修改状态成功');
    }else{
      resultPopMsg(0,res.data.msg);
    }
  }).catch((err)=>{
    console.log(err);
    resultPopMsg(0,res.data.msg);
  });
}
</script>

<style scoped>

</style>