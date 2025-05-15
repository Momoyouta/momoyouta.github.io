<template>
  <div class="divide">
    <span class="head">动漫库</span>
  </div>
  <div class="tag-choose" v-for="(items,index) in tags" :key="index">
    <div class="tag-list-box">
      <div class="title">
        {{ titles[index] }}
        <span class="iconfont icon-you"></span>
      </div>
    </div>
    <TagList @propSelect="changeCondition(index,$event)" class="taglist-box" :isChoose="true" :items="items"></TagList>
  </div>
  <div class="aniList">
    <ul class="animes">
      <li class="anime" v-for="(ani,index) in aniList">
        <AnimeCard style="width: 100%;font-size: 1rem" :ani="ani.data" :show-con="ani.show"></AnimeCard>
      </li>
    </ul>
  </div>
</template>

<script setup>

import TagList from "@/components/common/TagList.vue";
import AnimeCard from "@/components/common/AnimeCard.vue";
import {onMounted, onUnmounted, reactive, ref, watch} from "vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
const titles=['类型','年份','排序']
const tags=reactive([[],['全部',2024,2025],['时间排序','评分排序']])
import {tranToCard} from '@/utils/animeCard.js'
import {throttle,checkScroll} from '@/utils/commonHook.js'
let isAll=0
const abortController = ref(null);
const condition=ref({
  tag:'全部',
  year: '全部',
  orderr: 0, //0：时间 1：评分
  offset: 0,
  pageSize: 12,
  tagId: null
})
let aniCount=0;
const isUpdate=ref(true);
const aniList=reactive([]);
const axios_instance=axios.create({
  baseURL: `${apiUtils.BASIC}`,
})
onMounted(()=>{
  getHotTag();
  selectAnimes();
  window.addEventListener("scroll",throttle(()=>checkScroll(handlerScroll)))
});
onUnmounted(()=>{
  window.removeEventListener("scroll",throttle(()=>checkScroll(handlerScroll)))
})
function handlerScroll(value){
  condition.value.offset+=value;
}
function selectAnimes(){
  isUpdate.value=true;
  if(isAll===1) return;
  if (abortController.value) {
    abortController.value.abort();
  }
  abortController.value = new AbortController();
  axios_instance.post(`/user${apiUtils.DIR_SELECT_ANI}`,condition.value,{
    signal: abortController.value.signal
  })
      .then(res=>{
        const newData = tranToCard(res.data.data);
        aniList.push(...newData);
        isAll = newData.length < condition.value.pageSize ? 1 : 0;
      })
      .catch(err=>{console.log(err)})
  isUpdate.value=false;
}
function getHotTag(){
  axios_instance.get(`/user${apiUtils.HOT_TAG}`)
      .then(res=>{
        tags[0]=res.data.data.map(item=>{return item.name;});
        tags[0].unshift("全部");
      })
      .catch(err=>{console.log(err)})
}
function changeCondition(type,condi){
  type++;
  const newCondition = { ...condition.value }; // 创建新对象
  if (type === 1) {
    newCondition.tag = condi;
  } else if (type === 2) {
    newCondition.year = condi;
  } else {
    newCondition.orderr = condi==="时间排序"?0:1;
  }
  condition.value = newCondition; // 替
}

watch(condition,(newCon,oldCon)=>{
  if(newCon.year!==oldCon.year||newCon.tag!==oldCon.tag||newCon.orderr!==oldCon.orderr){
    condition.value={
      ...newCon,
      offset: 0
    }
    aniList.length=0;
    isAll=0;
    console.log("changeTag");
  }
  selectAnimes();
},{deep:true});
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
.tag-choose {
  width: 100%;
  display: flex;
  border-bottom: var(--input-base) solid 2px;
}
.tag-list-box {
  width: 7%;
  padding-top: 0.5rem;
}
.taglist-box{
  flex: 1;
  padding-top: 0.5rem;
}
.title{
  padding-top: 0.2rem;
  font-weight: bold;
}
.iconfont{
  color: var(--btn-login);
}

.aniList{
  margin-top: 1rem;
}
.animes{
  display: flex;
  flex-wrap: wrap;
}
.anime{
  width: 15%;
  margin-right: 2%;
  margin-bottom: 2%;
  box-sizing: border-box;
  cursor: pointer;
}

.anime:nth-child(6n){
  margin-right: 0;
}
</style>