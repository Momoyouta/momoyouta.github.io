<template>
  <div class="main">
    <div class="base">
      <div class="class">
        日漫
      </div>
      <div class="cover" @click="gotoDetail" style="overflow: hidden">
        <div class="state">
          {{anime.ep==anime.totalEps? anime.ep+"集全":"更新至第"+anime.ep+"集"}}
        </div>
        <img :src="anime.image">
        <div style="position: absolute;bottom: 0;height: 3rem;width: 100%;background: linear-gradient(to top,#4c4b4b,rgba(0,0,0,0));"></div>
      </div>
      <div class="info">
        <span class="title" @click="gotoDetail">{{anime.name}}</span>
        <span class="font-sm">{{tag}}</span>
        <span class="font-sm">{{description}}</span>
      </div>
      <div class="footer">
        <div class="btn">
          <button class="follow ">
            <span class="iconfont icon-paobu"></span>
          </button>
          <button class="detail" @click="gotoDetail">详情</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>

import {onMounted, ref} from "vue";
import {router} from "@/route/config.js";

const description=ref('');
const tag=ref('');
const props = defineProps({
  anime:{
    type: Object,
    required: true
  }
})
onMounted(()=>{
  description.value=props.anime.description;
  tag.value=props.anime.date+" / ";
  tag.value+=props.anime.tags.slice(0,props.anime.tags.length).join(',');
})
function gotoDetail(){
  router.push({
    name:'getAnimeDetail',
    params:{
      animeid:props.anime.animeId,
    },
  });
}
</script>

<style scoped>
.main{
  width: 20rem;
  height: 10.5rem;
  padding-top: 1.5rem;
}
.base{
  height: 100%;
  width: 100%;
  background-color: var(--side-bg);
  border-radius: 0.4rem;
  padding: 0 0.7rem;
  position: relative;
  box-shadow: 0 0 2px rgba(48, 46, 46, 0.2);
}
.class{
  position: absolute;
  top: -1.2rem;
  right: 0;
  z-index: -1;
  font-size: 0.7rem;
  color: #404040;
  font-weight: bold;
  background: #dac6c6;
  padding: 0.15rem 0.3rem 0.6rem 0.3rem;
  border-radius: 0.4rem 0.4rem 0 0;
}
.class::after{
  position: absolute;
  content: "";
  background: #dac6c6;
  width: 80%;
  height: 100%;
  left: -0.2rem;
  z-index: -1;
  transform: skew(-15deg);
}
.cover{
  position: absolute;
  top:-1.4rem;
  display: flex;
  justify-content: center;
  z-index: 2;
  cursor: pointer;
  border-radius: 0.4rem;
}
.state{
  position: absolute;
  left: auto;
  bottom: 1rem;
  z-index: 2;
  color: white;
  font-size: 0.7rem;
  text-shadow: 0 0 3px #404040;
}
img{
  width: 6.5rem;
  aspect-ratio: var(--anime-cover-ratio);
}
.info{
  margin-left: 7rem;
  height: 7rem;
}
.info span{
  width: 100%;
  display: inline-block;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.info .font-sm{
  font-size: 0.75rem;
  color: var(--font-sm);
}
.info .title{
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
  margin-top: 0.7rem;
  cursor: pointer;
}
.title:hover{
  color: var(--btn-login-hover);
}
.footer{
  width: 100%;
  height: 1.6rem;
  background: var(--base-deep-bg);
  border-radius: 0.4rem;
  position: relative;
  z-index: 0;
}
.btn{
  position: absolute;
  left: 7rem;
  top: -1rem;
  background-color: var(--side-bg);
  padding:  0.2rem;
  border-radius: 1rem;
}
.btn button{
  border: var(--btn-login) 1px solid;
  cursor: pointer;
}
.follow{
  height: 1.7rem;
  width: 1.7rem;
  margin-right: 0.2rem;
  border-radius: 0.8rem 0.2rem 0.2rem 0.8rem ;
  vertical-align: top;
  line-height: 1.7rem;
  color: #fff;
  background-color: var(--btn-login);
}
.iconfont{
  font-size: 0.95rem;
  font-weight: bold;
}
.detail{
  height: 1.7rem;
  width: 2.8rem;
  font-size: 0.7rem;
  border-radius: 0.2rem 0.8rem 0.8rem 0.2rem ;
  line-height: 1.7rem;
  color: var(--btn-login);
}
.follow:hover{
  background-color: var(--btn-login-hover);
}
.detail:hover{
  background-color: var(--btn-login);
  color: #fff;
}
</style>