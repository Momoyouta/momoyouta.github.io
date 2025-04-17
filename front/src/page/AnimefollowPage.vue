<template>
  <div class="divide">
    <span class="head">晚上好！user_name！</span>
  </div>
  <div class="follow-box bd">
    <div class="top-bar">
      <button class="to-detail icon" @click="scheduleType=1">
        <span class="iconfont icon-xuanxiang1"></span>
      </button>
      <button class="to-omit icon" @click="scheduleType=2">
        <span class="iconfont icon-xuanxiang"></span>
      </button>
      <button class="import-cart">
        从购物车导入
      </button>
    </div>
    <div class="bottom">
      <div class="left">
        <ul class="anime-list" >
          <li v-for="(anime,index) in doingAnimes" :style="animelistStyle">
            <img @click="chooseItem(index)" :src="anime.image">
            <div v-if="scheduleType===1" class="list-info" @mouseenter="anime.isHover = true" @mouseleave="anime.isHover = false">
              <span @click="chooseItem(index)" class="name">{{anime.name}}</span>
              <div class="progress-bar" :style="{background: 'linear-gradient(to right, #ed5f5f '
              +anime.schedule/anime.ep*100+'%,transparent '+anime.schedule/anime.ep*100+'%)'}"></div>
              <div class="option">
                <span class="schedule">[{{anime.schedule}}/{{anime.ep}}]</span>
                <span class="update-ep" @click="updateSchedule(anime.id,anime.schedule+1)"
                      v-show="anime.isHover">👀</span>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div class="right">
        <div class="main-box">
          <div class="info-panel">
            <img :src="activeAnime.image">
            <div class="info-box">
              <div class="title" @click="gotoDetail">{{activeAnime.name}}</div>
              <div class="font-sm">{{activeAnime.doing}} 人在追</div>
              <div class="bar">
                <div class="progress-bar" :style="{background: 'linear-gradient(to right, #ed5f5f '
                +activeAnime.schedule/activeAnime.ep*100+'%,transparent '+activeAnime.schedule/activeAnime.ep*100+'%)'}"></div>
                <input v-model="changeSchedule" type="text" :placeholder="activeAnime.schedule"
                       class="progress-bar-input">  /{{activeAnime.ep}}
              </div>
              <div class="option">
                <a href="#" class="write-ccomment">写评论</a>
              </div>
            </div>
          </div>
          <div class="contro-panel">
            <ul>
              <li><button>弃置</button></li>
              <li>
                <button v-if="isOneUpdate" :disabled="activeAnime.schedule === activeAnime.ep"
                        @click="updateSchedule(activeAnime.id,activeAnime.schedule + 1)">
                  {{ activeAnime.schedule === activeAnime.ep ? '完成' : `ep${activeAnime.schedule + 1} 看过` }}
              </button>
                <button v-if="!isOneUpdate" @click="updateSchedule(activeAnime.id,changeSchedule)">更新进度</button>
              </li>
            </ul>

          </div>
        </div>
        <div class="ep-rec">
          <ul class="ep-list">
            <li v-for="item in animeTp.ep">{{item}}</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import {computed, onMounted, reactive, ref, watch} from "vue";
import axios from "axios";

const animeTp=[
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
    id:2106085755,
    name:'深夜重拳',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
    ep:13,
    doing: 8086,
    schedule: 8
  },
]
const activeIndex=ref(0);
const doingAnimes=reactive([]);
const activeAnime=computed(()=>{
  return {
    ...doingAnimes[activeIndex.value],
  }
})
const changeSchedule=ref();
const scheduleType=ref(1)
const animelistStyle=computed(()=>{
  if(scheduleType.value===2){
    return {
      'margin-bottom': '0.2rem',
      'margin-right': '0.65rem',
    }
  }else{
    return {
    }
  }

})
onMounted(()=>{
  doingAnimes.push(...animeTp)
  //getData()
})
async function getData(){
  await axios.get('http://127.0.0.1:4523/m2/5510024-5186282-default/276673351')
      .then(res=>{
        doingAnimes.push(...res.data.map(anime=>({
          ...anime,
          isHover:false
        })));
      })
}
function updateSchedule(animeId,ep){
  for(let i =0; i<doingAnimes.length;i++){
    if(doingAnimes[i].id===animeId){
      doingAnimes[i].schedule=Number(ep)
      break;
    }
  }
  isOneUpdate=true;
}
function chooseItem(index){
  activeIndex.value=index;
}
let isOneUpdate=true;
let lockone=false
watch(changeSchedule,(newIndex)=>{
  if(!lockone)
    isOneUpdate=false;
  else
    lockone=true;
})
</script>

<style scoped>
.divide{
  width: 100%;
  border-bottom: var(--input-base) solid 0.3rem;
  margin-bottom: 0.5rem;
  cursor: default;
}
.head{
  font-size: 2.5rem;
  font-weight: bold;
  padding-top: 0.5rem;
  border-bottom: var(--banner-active) solid 0.3rem;
}
.bd{
  border-radius: 0.6rem;
}
.ep-rec{
  display: flex;
  flex-wrap: wrap;
  margin-top: 1rem;
  margin-left: 7rem;
}
.ep-list{
  display: flex;

}
.ep-list li{
  width: 1.5rem;
  height: 1.5rem;
  text-align: center;
  background-color: var(--input-base);
  margin-right: 0.3rem;
  border-radius: 0.2rem;
  cursor: pointer;
  line-height: 1.5rem;
}
.follow-box{
  width: 100%;
  height: 27rem;
  background: var(--base-deep-bg);
  overflow: hidden;
}
.bottom{
  display: flex;
}

.left{
  height: 25rem;
  width: 20rem;
  overflow-y: auto;
  border-right: black solid 0.1rem;
}
.anime-list{
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  background-color: var(--base-deep-bg);
  padding: 0.25rem 0.5rem;

}
.anime-list li{
  display: flex;
  width: fit-content;
  height: fit-content;
  margin-bottom: 0.4rem;
  transition: transform 0.2s ease;
}
.anime-list li:hover{
  transform: scale(1.03);
}
.anime-list li img{
  height: 5.7rem;
  border-radius: 0.6rem;
  aspect-ratio: var(--anime-cover-ratio);
  cursor: pointer;
}
.list-info .progress-bar{
  width: 10rem;
  height: 0.6rem;
  border-radius: 1.2rem;
  background: linear-gradient(to right, #ed5f5f 60%,transparent 60%);
  border: var(--input-base) solid 0.1rem;
}
.list-info  {
  margin-left: 1rem;
  padding-top: 0.8rem;
}
.list-info .name{
  display: inline-block;
  cursor: pointer;
  margin-bottom: 0.9rem;
}
.list-info .name:hover{
  color: var(--btn-login-hover);
}
.list-info .option{
  margin-top: 0.2rem;
}
.list-info .update-ep{
  margin-left: 2rem;
  cursor: pointer;
}

.main-box{
  display: flex;
  margin-left: 4rem;
  margin-top: 2.7rem;
}
.info-panel{
  width: 30rem;
  display: flex;
  background: #76b89a;
  padding: 1rem;
  border-radius: 1rem;

}
.info-panel img{
  width: 10rem;
  aspect-ratio: var(--anime-cover-ratio);
  border-radius: 1rem;
}
.info-box {
  margin-left: 1rem;
  padding-top: 2.3rem;
}
.info-box .title{
  font-size: 1.2rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 0.7rem;
}
.info-box .font-sm{
  font-size: 0.85rem;
  margin-bottom: 1.7rem;
}
.info-box .write-ccomment{
  padding-left: 1rem;
}
.bar{
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}
.bar .progress-bar{
  width: 10rem;
  height: 1.2rem;
  border: var(--btn-login-hover) solid 0.1rem;
  border-radius: 1.2rem;

}
.progress-bar-input{
  width: 1.7rem;
  text-align: center;
  height: 1.5rem;
  margin-left: 1rem;
  margin-right: 0.5rem;
}
.contro-panel{
  width: 6rem;
  flex: 1;
  background-color: #76b89a;
  display: flex;
  align-items: center;
  margin-left:1rem;
  border-radius: 1rem;
  padding: 1rem;
}
.contro-panel ul{
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}
.contro-panel button{
  width: 3rem;
  height: 3rem;
  margin-bottom: 1rem;
  background-color: var(--btn-login);
  color: #272424;
}
.top-bar{
  height: 2rem;
  padding: 0.2rem 0;
  background-color: #86a972;
  position: relative;
}
.top-bar button{
  height:1.6rem;
  font-size: 0.7rem;
  vertical-align: top;
  padding: 0.2rem;
  border-radius: 0.2rem;
  background-color: #55a532;
  border: #498d2c dashed 2px;
  cursor: pointer;
}
.top-bar button:hover{background-color: #468b29
}
.icon-xuanxiang1{
  font-size: 1rem;
}
.top-bar button.icon{
  width:1.6rem;
}
.to-detail{
  margin-left: 16.7rem;
  margin-right: 0.1rem;
}
.import-cart{
  position: absolute;
  right: 0.6rem;
}
</style>