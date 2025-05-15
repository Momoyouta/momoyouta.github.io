<template>
  <div class="divide">
    <span class="head">新番上线</span>
  </div>
  <div class="aniList">
    <ul class="animes">
      <li v-if="animes.length!==0" class="anime" v-for="(ani,index) in animes">
        <AnimeCard2 :anime="ani" class="anime-item"></AnimeCard2>
      </li>
    </ul>
  </div>
</template>


<script setup>
import {onMounted, reactive} from "vue";
import AnimeCard2 from "@/components/recentupdate/AnimeCard2.vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
const animes=reactive([]);
const anime_tp={
  name: "史莱姆尼普鲁",
  image:"https://lain.bgm.tv/r/400/pic/cover/l/fe/ab/470045_zA9BA.jpg",
  id: 2106085705,
  date: 2025,
  description: "asdoasdp5646456456456456oaspd",
  tags: ['奇幻','日常','搞笑'],
  ep: 12
}
const axs=axios.create({
  baseURL:apiUtils.BASIC
})
onMounted(()=>{
  getData();
})
function getData(){
  axs.get(`/user${apiUtils.RECENT_UPDATE_ANI}`)
      .then((res)=>{
        console.log(res.data.data);
        animes.push(...res.data.data);
      })
      .catch(err=>{console.log(err)})
}
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
  margin-bottom: 1.5rem;
}
.animes{
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
.anime{
  margin-bottom: 1rem;
}
</style>