<template>
  <a href="#" @click="intoDetail(ani.id)" class="container-card">
    <div class="err" :style="{background: 'url('+errImgUrl+') no-repeat center center / cover'}">
        <img :src="ani.image" loading="lazy" >
        <span class="date" v-if="showCon.score">{{ani.score}}</span>
        <span class="date" v-if="showCon.date">{{ani.updateTime}}</span>
        <span class="date" v-if="showCon.score">{{ani.score}}</span>
        <span class="title" style="z-index: 2" v-if="showCon.state">{{ aniState }}</span>
      <div style="position: absolute;bottom: 0;height: 4rem;width: 100%;background: linear-gradient(to top,#4c4b4b,rgba(0,0,0,0));"></div>
    </div>
    <div class="content" v-if="showCon.name">
      {{ ani.name }}
    </div>
  </a>
</template>

<script setup>
import {computed, ref} from "vue";
import errImgUrl from "/src/assets/img/ani_add_error.png";
import {router} from "@/route/config.js";
const props=defineProps({
  showCon: {
    type: Object,
    required: true
  },
  ani: {
    type: Object,
    required: true
  }
});
const aniState=computed(()=> {
  return props.ani.end==1? props.ani.ep+"集全":"更新至第"+props.ani.ep+"集"
})
function intoDetail(id){
  router.push({
    name:'getAnimeDetail',
    params:{
      animeid:id,
    },
  });
}
</script>

<style scoped>
a{
  text-decoration:none;
}

.container-card{
  display: block;
  width: 100%;
  aspect-ratio: 190/280;

}
.err{
  background-size: cover;
  border-radius: 5%;
  display: flex;
  justify-content: center;
  position: relative;
  overflow: hidden;
  aspect-ratio: 190.5/266.7;
}
img{
  width: 100%;
  height: 100%;
}
.date{
  position: absolute;
  top:0;
  right:0;
  border-radius: 20%;
  padding: 1%;
  align-items: center;
  font-size: 70%;
  color: white;
  background-color: #e50914;
}
.title{
  position: absolute;
  bottom: 10%;
  color: white;
  font-size: 70%;
  text-shadow: 0 0 3px #404040;
}
.content{
  padding: 1% 0;
  text-align: center;
  font-size: 85%;
  color: black;
  height: 1.5rem;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap
}
</style>