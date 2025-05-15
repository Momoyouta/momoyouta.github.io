<template>
  <div class="addmore-box" :style="{maxHeight:boxHeight}">
    <div class="tit">
      <h2>Bangumi API</h2>
      <a href="#"  @click="bangumiApi^=1" :style="pointStyle">👇</a>
    </div>
    <h3>番剧检索</h3>
    <ul>
      <li>
        年：
        <input type="text" v-model="year" class="bangumi-search-sub-year">
      </li>
      <li>
        月：
        <input type="text" v-model="month" class="bangumi-search-sub-month">
      </li>
      <li>
        索引：
        <input type="text" v-model="offset" class="bangumi-search-sub-offset">
      </li>
      <li>
        页大小：
        <input type="text" v-model="limit" class="bangumi-search-sub-pagesize">
      </li>
      <li>
        <el-button @click="getAnime" type="primary" size="small" v-if="!running">检索</el-button>
        <el-button @click="running=false" type="primary" size="small" v-else>停止检索</el-button>
      </li>
    </ul>
    <div class="res-box">
      {{result}}
    </div>
  </div>
</template>

<script setup>


import {computed, ref} from "vue";
import axios from "axios";
import {KEY} from "../../../../private/key.js";
import {apiUtils} from "@/common/apiUtils.js";
import {formatDate} from "@/utils/commonHook.js";
import request from "@/utils/request.js";

const result=ref();
const axsBangumi=axios.create({
  baseURL:"https://api.bgm.tv",
  headers:{
    'Authorization': KEY.Auth,
  }
})
const animeApiUrl="/v0/subjects"
const epsApiUrl="/v0/episodes"
const bangumiApi=ref(1)
const year=ref(2025);
const month=ref(4);
const limit=ref(2);
const offset=ref(0);
const running=ref(false);
let isErr=0;
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
async function getAnimeByBGMApi(){
  await axsBangumi.get(animeApiUrl, {
    params: {
      type: 2,
      cat: 1,
      sort: 'date',
      year: year.value,
      month: month.value,
      limit: limit.value,
      offset: offset.value*limit.value,
    }
  })
      .then(res => {
        result.value = res.data.data
      })
      .catch(err => {
        console.log(err);
        isErr = 1;
      });
}
async function getEpsByBGMApi(animeId){
  return await axsBangumi.get(epsApiUrl, {
    params: {
      subject_id: animeId,
      type: 0,
      limit: 50,
      offset: 0,
    }
  })
  .then(res => {
   return res.data
  })
  .catch(err => {
    console.log(err);
    isErr = 1;
  });
}
async function getAnime(){
  running.value=true;
  while(running.value) {
    await getAnimeByBGMApi();
    if (isErr === 1) {
      console.log("页出错:" + offset.value)
      break;
    }
    const filterTags = ['日本', '中国']
    let count=0;
    for (let item of result.value) {
      count++;
      if (item.meta_tags.filter(it => filterTags.includes(it)).length === 0) continue;
      if (item.rating.rank === 0) continue;
      let animeDto = {
        anime: {
          id:item.id,
          name: item.name_cn === '' ? item.name : item.name_cn,
          image: item.images.common,           // 封面图URL
          description: item.summary,     // 动画描述
          updateTime: '',      // 更新时间（ISO格式字符串）
          score: item.rating.score,           // 综合评分（0-10）
          ban: 0,             // 封禁状态（0=正常，1=封禁）
        },
        animeInfo: {
          startDate: item.date,       // 开播日期（YYYY-MM-DD）
          weekday: '',         // 周几更新（如"星期四"）
          company: '', // 制作公司
          officialWeb: '',
          videolistId: ''      // 关联视频列表ID
        },
        animeRating: {
          rank: '',            // 排名
          scoreTotal: item.rating.total,      // 总评分人数
          score: item.rating.score,           // 综合得分（加权计算）
          collect: item.collection.collect,         // 收藏数
          doing: item.collection.doing,           // 追番数
          views: item.rating.total + item.collection.collect           // 播放量
        },
        episode: {
          animeId:item.id,
          ep:0,
          totalEps:0,
        },
        tags: []
      };
      for (let it of item.infobox) {
        if (it.key === '官方网站') {
          if(it.value instanceof Array){
            animeDto.animeInfo.officialWeb = it.value[0].v;
          }else{
            animeDto.animeInfo.officialWeb = it.value;
          }
        } else if (it.key === '放送星期') {
          let day;
          if (it.value[2] === '一') day = 1;
          else if (it.value[2] === '二') day = 2;
          else if (it.value[2] === '三') day = 3;
          else if (it.value[2] === '四') day = 4;
          else if (it.value[2] === '五') day = 5;
          else if (it.value[2] === '六') day = 6;
          else if (it.value[2] === '日') day = 7;
          animeDto.animeInfo.weekday = day;
        } else if (it.key === '动画制作') {
          animeDto.animeInfo.company = it.value;
        }
      }
      for (let it of item.tags) {
        if (it.name.length >= 4) continue;
        animeDto.tags.push(it.name)
      }
      //日期处理
      const startDate=animeDto.animeInfo.startDate;
      animeDto.animeInfo.startDate=new Date(startDate).getTime()/1000;
      const eps=await getEpsByBGMApi(item.id);
      let epdata=eps.data;
      let countEp=0;
      for(countEp=0;countEp<epdata.length;countEp++){
        if(new Date(epdata[countEp].airdate)>new Date()){
          break;
        }
      }
      animeDto.episode.ep=countEp
      animeDto.episode.totalEps=eps.total;
      console.log("插入："+animeDto.anime.name,"总集数："+animeDto.episode.totalEps,"当前集数："+animeDto.episode.ep);
      const che = await writeToServer(animeDto)
      if (che === 0) {

        console.log("页:"+offset.value+"--项出错：" + count);
        console.log("动画："+JSON.stringify(animeDto))
        isErr=1;
        break;
      }
    }
    console.log("完成页："+offset.value)
    offset.value++;
    console.log("等待 5 秒...");
    await sleep(15000);
    if(!running.value){
      console.log("终止检索");
      break;
    }
    console.log("继续执行下一页");
  }
}//40
async function writeEpToServer(animeEps){
  return await request({
    method: 'post',
    url:apiUtils.BANGUMI_UPDATE_EPS,
    data:animeEps
  }).then(res=>{
    return res.data.data;
  }).catch(err=>{
    console.log(err);
    return 0;
  })
}
async function writeToServer(animeDto){
  return await request({
    method: 'PUT',
    url:apiUtils.BANGUMI_BASE_ADD,
    data:animeDto
  })
      .then(res=>{
        return res.data.data;
      })
      .catch(err=>{console.log(err);return 0});

}
const boxHeight=computed(()=>{
  return bangumiApi.value===1? '30rem':'3.8rem'
})
const pointStyle=computed(()=>{
  return {
    transform: bangumiApi.value===1? 'rotate(180deg)':'rotate(0deg)',
  }
})
</script>

<style scoped>
.addmore-box{
  width: 100%;
  background: #f6f4f4;
  padding: 1rem 2rem;
  margin-bottom: 1rem;
  overflow: hidden;
  transition: max-height 0.5s ease;
}
.addmore-box ul{
  display: flex;
  flex-wrap: wrap;
  font-size: 1rem;
}
.addmore-box input{
  width: 3rem;
  vertical-align: middle;
}
.addmore-box li{
  margin-right: 0.5rem;
  height: 2rem;
  line-height: 2rem;
}
button{
  padding: 0.1rem 0.3rem;
}
.res-box{
  width: 100%;
  padding: 1rem 1rem;
  background-color: #3d3b3b;
  border-radius: 1rem;
  box-shadow: 0 0 5px black;
  color: white;
  height: 15rem;
  overflow-y: scroll;
}
.tit{
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 2.5rem;
  margin-bottom: 5px;
}
a{
  text-decoration: none;
  transition: transform 0.3s ease;
}
</style>