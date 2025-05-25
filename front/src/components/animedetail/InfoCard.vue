<template>
  <div class="box">
    <div class="left">
      <img :src="anime.anime.image" alt="" referrerpolicy="no-referrer">
      <a :href="anime.animeInfo.officialWeb">👉官方网站👈</a>
    </div>
    <div class="right">
      <div class="rtop-box">
        <div class="name-box">
          <h2>{{ anime.anime.name }}</h2>
          <div class="subtitle">
            <span>{{startDate}}开播</span>
            <span>{{anime.animeInfo.company}}</span>
          </div>
          <ul class="state ">
            <li>
              <div class="state-title">views</div>
              <div class="state-data">{{anime.animeRating.views}}次</div>
            </li>
            <li>
              <div class="state-title">收藏数</div>
              <div class="state-data">{{anime.animeRating.collect}}人</div>
            </li>
            <li>
              <div class="state-title">状态</div>
              <div class="state-data">{{anime.ep}}集</div>
            </li>
          </ul>
        </div>
        <div class="score" style="display:flex;align-items:center;">
          <div style="text-align: center;font-size: 2rem;line-height: 2rem">{{anime.animeRating.score}}</div>
          <div style="text-align: center;margin-left: 0.2rem">
            <el-rate v-model="score" @change="handleScore" allow-half />
            <p>{{anime.animeRating.scoreTotal}}人评</p>
          </div>
        </div>
      </div>
      <div class="rbottom-box">
        <div class="profile">
          <el-text line-clamp="4" style="color: black">
            <strong>简介：</strong>{{anime.anime.description}}
          </el-text>
        </div>
        <div class="tags">
          <ul class="tag-list">
            <li v-for="(tag,index) in anime.tags">{{tag}}</li>
          </ul>
        </div>
        <div class="btn-box">
          <button class="btn">
            <span class="iconfont icon-aixin"></span>
            加入追番
          </button>
          <button v-if="!favoriteStatus" class="btn" @click="favorite">
            <el-icon><Star/></el-icon>
            收藏
          </button>
          <button v-else class="btn" @click="unfavorite">
            <el-icon style="vertical-align: center"><StarFilled/></el-icon>
            取消收藏
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref,} from 'vue';
import {formatDate} from "@/utils/commonHook.js";
import axios from "axios";
import request from "@/utils/request.js";
import {resultPopMsg} from "@/utils/resultPopMsg.js";
import {apiUtils} from "@/common/apiUtils.js";
import {Star, StarFilled} from "@element-plus/icons-vue";
const props = defineProps({
  anime:{
    type: Object,
    required: true
  }
})
onMounted(()=>{
  getFavoriteStatus();
})
const startDate=computed(()=>{
  return formatDate(props.anime.animeInfo.startDate*1000);
})
const score=ref(props.anime.animeRating.score/2)
const handleScore=(val)=>{
  score.value=props.anime.animeRating.score/2;
  request({
    url:apiUtils.user.score,
    method:'get',
    params:{
      animeId:props.anime.anime.id,
      score:val*2
    }
  }).then((res)=>{
    if(res.data.code===1){
      resultPopMsg(1,'评分成功');
    }else{
      resultPopMsg(0,res.data.msg);
    }
  }).catch((err)=>{
    console.log(err);
    resultPopMsg(0,res.data.msg);
  });
}

const favoriteStatus=ref(false);
function favorite(){
 request({
   url:apiUtils.user.favorite,
   method:'get',
   params:{
      animeId:props.anime.anime.id,
   }
 }).then((res)=>{
   if(res.data.code===1){
     favoriteStatus.value=true;
     resultPopMsg(1,'收藏成功');
   }else{
      resultPopMsg(0,res.data.msg);
   }
}).catch((err)=>{
   console.log(err);
   resultPopMsg(0,res.data.msg);
 });
}
function unfavorite(){
  request({
    url:apiUtils.user.unfavorite,
    method:'get',
    params:{
      animeId:props.anime.anime.id,
    }
  }).then((res)=>{
    if(res.data.code===1){
      favoriteStatus.value=false;
      resultPopMsg(1,'取消收藏成功');
    }else{
      resultPopMsg(0,res.data.msg);
    }
  }).catch((err)=>{
    console.log(err);
    resultPopMsg(0,res.data.msg);
  });
}
function getFavoriteStatus(){
  request({
    url:apiUtils.user.getFavoriteStatus,
    method:'get',
    params:{
      animeId:props.anime.anime.id,
    }
  }).then((res)=>{
    if(res.data.code===1){
      favoriteStatus.value=res.data.data;
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
.box{
  display: flex;
  height: 24rem;
  border-radius: 1rem;
  overflow: hidden;
}
.left{
  width: 20%;
  aspect-ratio: 190/320;
  background-color: var(--side-bg);
  text-align: center;
  border-radius: 1rem 1rem 0 1rem;
  overflow: hidden;
}
.left img{
  width: 100%;
  border-radius: 1rem;
  margin-bottom: 0.5rem;
  border: #f6eedd solid 1px;
}
.left a{
  text-decoration: none;
  color: #e16d6d;
}

.right{
  flex:1;
}
.rtop-box{
  display: flex;
  position: relative;
  height: 40%;
  align-items: end;
}
.subtitle{
  margin-left: 1.5rem;
  font-size: 0.8rem;
  margin-bottom: 1rem;
  color: #464649;
}
.subtitle span{
  margin-right: 0.5rem;
}
h2{
  margin-left: 1.5rem;
  margin-bottom: 0.5rem;
}
.state{
  display: flex;
  width: fit-content;
  margin-left: 1rem;
  margin-bottom: 0.5rem;
}
.state li{
  padding: 0.1rem 0.5rem;
  border-right: var(--btn-login) solid 0.25rem;
}
.state li:last-child{
  border-right: none;
}
.state-data{
  text-align: center;
  font-size: 0.75rem;
}

.rbottom-box{
  height: 60%;
  position: relative;
  padding: 1rem 1rem;
  border-radius: 0 1rem 1rem 0;
  background-color: var(--side-bg);
}
.profile{
  border-bottom: var(--btn-login) solid 2px;
  font-size: 0.8rem;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
}
.btn-box{
  display: flex;
  flex-direction: row;
  position: absolute;
  right: 1.5rem;
  bottom: 1.5rem;
}
.btn{
  display: flex;
  align-items: center;
  padding: 0.5rem 1rem;
  border-radius: 1.2rem;
  margin-left: 1rem;
  border: none;
  background-color: var(--btn-login);
  color: #fff;
  cursor: pointer;
}
.btn:hover{
  background-color: var(--btn-login-hover);
}
.score{
  display: flex;
  position: absolute;
  right: 0;
  bottom: 30%;
  border-right: var(--btn-login) solid 0.25rem;
}


.tags{
  width: 75%;
}
.tag-list{
  display: flex;
  flex-wrap: wrap;
}
.tag-list li{
  padding: 0.2rem 0.5rem;
  font-size: 0.8rem;
  margin-right: 0.2rem;
  margin-bottom: 0.2rem;
  border-radius: 0.8rem;
  background-color: var(--tag-bg);
  box-shadow: 0 0 1px #5f5b5b;
  cursor: pointer;
}
</style>