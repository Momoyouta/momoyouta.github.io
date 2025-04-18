<template>
  <div class="main-result-container">
    <div class="title">
      <span>搜索到与“{{ keyword }}”相关的{{animes.length}}条结果</span>
    </div>
    <ul class="result">
      <li class="anime-item" v-if="animes.length!==0" v-for="(ani,index) in animes">
        <div class="info-box">
          <div class="img" :style="{background: 'url('+errImgUrl+') no-repeat center center / cover'}">
            <img :src="ani.image" loading="lazy" >
          </div>
          <div class="right">
            <div class="name">
              {{ani.name}}
            </div>
            <div class="date">
              {{ani.date}} 首播
            </div>
            <ul class="info">
              <li>集数：<span>{{ani.ep}}</span></li>
              <li>制作：<span>{{ani.company}}</span></li>
              <li>简介：<span>{{ani.description}}</span></li>
            </ul>
          </div>
        </div>
        <div class="divide">
          <ul class="dash">
            <li v-for="i in 10"></li>
          </ul>
        </div>
        <div class="tag-box">
          <ul class="tags">
            <li v-for="tag in ani.tags">{{tag}}</li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>

import {onMounted, onUnmounted, reactive, watch} from "vue";
import errImgUrl from "/src/assets/img/ani_add_error.png";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import {throttle,checkScroll} from '@/hooks/commonHook.js'
const animes=reactive([]);
const props = defineProps(['keyword'])
const axs=axios.create({
  baseURL:apiUtils.BASIC
})
let isAll=0;
let offset=0;
onMounted(()=>{
  getData();
  window.addEventListener("scroll",throttle(()=>checkScroll(handlerScroll)));
})
onUnmounted(()=>{
  window.removeEventListener("scroll",throttle(()=>checkScroll(handlerScroll)))
})
function handlerScroll(value){
  offset += value;
}
function getData(){
  if(isAll===1) return;
  axs.get(`/user${apiUtils.KEYWORD_SEARCH}`,{
    params:{
      keyword:props.keyword,
      offset: offset,
      pageSize: 12
    }
  })
      .then(res=>{
        animes.push(...res.data.data)
        if(res.data.data.length<12){isAll=1;}
      })
      .catch(err=>{console.log(err)})
}
watch(() => props.keyword, (newVal, oldVal) => {
  animes.length = 0;
  offset = 0;
  isAll = 0;
  getData();
});
</script>

<style scoped>
.main-result-container{
  padding-left: 1rem;
}
.result{
  width: 100%;
}
.result .anime-item{
  width: 80%;
  display: flex;
  background-color: var(--side-bg);
  border-radius: 1rem;
  margin-bottom: 1rem;
  transition: transform 0.2s ease;
}
.result .anime-item:hover{
  transform: scale(1.02);
}
.result .anime-item::after{
  position: absolute;
  right: -1rem;
  z-index: 2;
  content: "";
  width: 0.5rem;
  height: 100%;
  background-color: var(--banner-active);
  border-radius: 1rem;
}
.title{
  border-bottom: 2px solid var(--dash-base);
  margin: 1.5rem 0;
}
.title span{
  font-size: 1.7rem;
  height: 100%;
  border-bottom: 2px solid var(--banner-active);
}

.info-box{
  width: 85%;
  display: flex;
  border-radius: 1rem;
}
.divide{
  flex: 1;
}
.dash{
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;
  justify-content: center;
  overflow: hidden;
  text-align: center;

}
.dash li{
  width: 60%;
  aspect-ratio: 1/1;
  background-color: var(--base-bg);
  border-radius: 100%;
  margin-bottom: 52%;
}
.dash li:last-child{
  margin-bottom: 0;
}
.dash::before{
  position: absolute;
  top:-2%;
  content: "";
  width: 60%;
  border-radius: 100%;
  aspect-ratio: 1/1;
  background-color: var(--base-bg);
  z-index: 2;
}
.dash::after{
  position: absolute;
  bottom:-2%;
  content: "";
  width: 60%;
  border-radius: 100%;
  aspect-ratio: 1/1;
  background-color: var(--base-bg);
  z-index: 2;
}

.tag-box{
  flex: 5;

}
.tags{
  margin: auto;
  height: 100%;
  display: flex;
  width: fit-content;
  flex-direction: column;
  justify-content: space-evenly;
}
.tags li{
  border-radius: 0.5rem;
  padding: 0.2rem 0.5rem;
  background-color: var(--tag-bg);
  text-align: center;
}
.img{
  width: 22%;
  aspect-ratio: 190/260;
  border-radius: 1rem;
  overflow: hidden;
}
img{
  width: 100%;
  height: 100%;
}
.right{
  display: flex;
  width: 71%;
  flex-direction: column;
  justify-content: end;
}
.info{
  margin-left: 1rem;
  margin-bottom: 2.2rem;
  font-size: 1rem;
}
.info li{
  width: 100%;
  margin-top: 0.5rem;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.info li:first-child{
  margin-top: 0;
}

.info li span{
  font-size: 0.85rem;
}
.name{
  font-size: 1.5rem;
  font-weight: bold;
  margin-left: 1rem;
  margin-bottom: 1rem;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.date{
  margin-left: 1rem;
  margin-bottom: 1.3rem;
  font-size: 0.8rem;
  color: #464649;
}

</style>