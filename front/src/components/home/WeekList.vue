<template>
 <div class="main-weeklist">
   <div class="top">
     <span class="title">
      番剧周表
     </span>
     <div class="nav">
       <ul class="nav-ul">
         <li @click="changeList(index)" :class="{active:activeDay==index}" v-for="(day, index) in ['周一', '周二', '周三', '周四', '周五', '周六', '周日']">
           <div class="title">
             {{day}}
           </div>
         </li>
       </ul>
     </div>
   </div>
   <div class="content">
    <ul class="ani-ul">
      <li v-for="(ani,index) in aniList[activeDay]" :key="index">
        <AnimeCard style="width: 100%;font-size: 1rem" :ani="ani.data" :show-con="ani.show"></AnimeCard>
      </li>
    </ul>
   </div>
 </div>
</template>

<script setup>
import AnimeCard from "@/components/common/AnimeCard.vue";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import {useCommonStore} from "@/store/commonStore.js";
import {tranToCard} from "@/utils/animeCard.js";
const aniList=reactive([[],[],[],[],[],[],[]]);
const activeDay=ref(0);
const axios_is=axios.create({
  baseURL: `${apiUtils.BASIC}`,
})
const commonStore=useCommonStore();
onMounted(()=>{
  for(let i=0;i<7;i++){
    changeList(i);
  }
});
function changeList(day){
  activeDay.value=day
  if(aniList[activeDay.value].length==0){
    axios_is.get(`/user${apiUtils.WEEKLIST_DAY_RQ}`,{
      params:{
        day: day+1,
      }
    })
    .then(res=>{
      const relaylist=tranToCard(res.data.data);
      aniList[day]=relaylist;
      commonStore.seasonAnime[day].push(relaylist.map(item=>{
        return {
          id:item.id,
          name:item.name,
        }
      }));
    })
    .catch(err=>{console.log(err)});
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
.nav-ul a{
  text-decoration: none;
  color: black;
}
.title{
  font-weight: bold;
  font-size: 1.2rem;
}
.nav{
  padding: 3px 3px;
  border-radius: 6px;
  background-color: var(--nav-ul-bg);
}
.nav-ul li{
  font-size: 12px;
  width: 20%;
  margin-right: 10px;
  padding: 5px 10px;
  text-align: center;
  border-radius: 6px;
  cursor: pointer;
}
.active{
  background: white;
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
  box-sizing: border-box;
  cursor: pointer;
}

.ani-ul li:nth-child(6n){
  margin-right: 0;
}
</style>