<template>
  <div class="box">
    <div class="left">
      <img :src="anime.animation.image" alt="" referrerpolicy="no-referrer">
      <a :href="anime.animeInfo.officialWeb">👉官方网站👈</a>
    </div>
    <div class="right">
      <div class="rtop-box">
        <div class="name-box">
          <h2>{{ anime.animation.name }}</h2>
          <div class="subtitle">
            <span>{{anime.animeInfo.startDate}}开播</span>
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
              <div class="state-data">{{anime.animation.ep}}集</div>
            </li>
          </ul>
        </div>
        <div class="score">
          <div class="text">{{anime.animeRating.score}}</div>
          <div class="star-box">
            <span :class="['iconfont',item,'star']" v-for="item in starIcon"></span>
            <p>{{anime.animeRating.scoreTotal}}人评</p>
          </div>
        </div>
      </div>
      <div class="rbottom-box">
        <div class="profile">
          <el-text line-clamp="4" style="color: black">
            <strong>简介：</strong>{{anime.animation.description}}
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
          <button class="btn">
            <span class="iconfont icon-aixin"></span>
            收藏
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive,} from 'vue';
const props = defineProps({
  anime:{
    type: Object,
    required: true
  }
})
onMounted(()=>{
  starInit();
})
const starIcon=reactive([]);
function starInit(){
  let num=props.anime.animeRating.score*1.0/10*5;
  if(num%1===0){
    for(let i=0;i<num;i++){
      starIcon.push('icon-star-full');
    }
    for(let i=0;i<5-num;i++){
      starIcon.push('icon-star');
    }
  }else{
    num=Math.floor(num);
    for(let i=0;i<num;i++){
      starIcon.push('icon-star-full');
    }
    starIcon.push('icon-star-half');
    for(let i=0;i<5-num-1;i++){
      starIcon.push('icon-star');
    }
  }

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
  border-radius: 1rem;
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
  position: absolute;
  right: 1.5rem;
  bottom: 1.5rem;
}
.btn{
  padding: 0.5rem 1rem;
  border-radius: 1.2rem;
  margin-left: 1rem;
  border: none;
  background-color: var(--btn-login);
  color: #fff;
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
.score .text{
  text-align: center;
  font-size: 2rem;
}
.star-box{
  text-align: center;
  margin-left: 0.2rem;
}
.star{
  position: relative;
  color: #f4c859;
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