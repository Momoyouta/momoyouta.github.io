<template>
  <div class="main">
    <div class="top">
     <span class="title">
      正在热映
     </span>
    </div>
    <div class="content">
      <ul class="ani-ul">
        <li v-for="(ani,index) in aniList" :key="index">
          <AnimeCard style="width: 100%;font-size: 1rem" :ani="ani.data" :show-con="ani.show"></AnimeCard>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import AnimeCard from "@/components/common/AnimeCard.vue";
import {onMounted, onUnmounted, reactive, ref} from "vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import {tranToCard} from "@/utils/animeCard.js";
const aniList=reactive([]);
const axios_instance=axios.create({
  baseURL: `${apiUtils.BASIC}`,
})
let isAll=0;
let aniCount=1;
onMounted(()=>{
  getData();
  aniCount=1;
  window.addEventListener("scroll",throttle(checkScroll))
});
onUnmounted(()=>{
  window.removeEventListener("scroll",throttle(checkScroll))
})
function getData(){
  axios_instance.get(`/user${apiUtils.SEARCH_ANI}/bynamelike`,{
    params:{
      name: '',
      page: aniCount,
      pageSize: 18,
      useDscp:false
    }
  })
      .then(res => {
        const relayList=tranToCard(res.data.data);
        console.log(relayList);
        aniList.push(...relayList);
        if(res.data.data.length<18){
          isAll=1;
        }
      })
      .catch(err=>{console.log(err)})
}
function checkScroll(){
  const scrollTop=window.scrollY;
  const clientHeight=window.innerHeight;
  const scrollHeight =document.documentElement.scrollHeight;
  if(Math.ceil(scrollTop+clientHeight)>=scrollHeight&&isAll===0){
    aniCount++;
    getData();
  }
}
function throttle(fn,delay=200){
  let timer = null;
  return (...args)=>{
    if(!timer){
      timer=setTimeout(()=>{
        fn.apply(this,args);
        timer = null;
      },delay)
    }
  }
}


</script>

<style scoped>


.top{
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;

}
ul{
  display: flex;
}

.title{
  font-weight: bold;
  font-size: 1.2rem;
}

.nav-ul li:last-child{
  margin-right: 0;
}

.ani-ul{
  flex-wrap: wrap;
}

.ani-ul li{
  width: 15%;
  margin-right: 2%;
  margin-bottom: 2%;
  box-sizing: border-box;
  cursor: pointer;
}

.ani-ul li:nth-child(6n){
  margin-right: 0;
}
</style>