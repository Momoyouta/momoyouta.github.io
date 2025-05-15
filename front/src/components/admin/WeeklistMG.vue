<template>
  <div class="main-weeklist">
      <div class="info-box">
        <div class="info-box-left">
          <div class="graph box">
            <div class="histogram">
              <ul class="rectangle">
                <li v-for="num in aniCount">
                  <div class="count">
                    {{ num }}
                  </div>
                  <div class="rectangleli" :style="{height:num/mostDay*75+'%'}"></div>
                </li>
              </ul>
            </div>
            <ul class="his-title">
              <li v-for="item in ['周一','周二','周三','周四','周五','周六','周日']">{{item}}</li>
            </ul>
          </div>
          <div class="nav box">
            <ul class="nav-ul ">
              <li v-for="(day, index) in ['周一', '周二', '周三', '周四', '周五', '周六', '周日']"
                  :key="index"
                  :class="{ active: activeDay-1 === index}"
                  @click="activeDay=index+1">
                <a href="#">{{ day }}</a>
              </li>
            </ul>
          </div>
        </div>
        <div class="info-box-right">
          <ul class="ul-info-right-box">
            <li class="info box">
              <h2>季共 {{ totAni }} 部</h2>
            </li>
            <li class="contro box">
              <div class="btn-box">
                <button class="edit" @click="isEditing^=true">{{ isEditing?'开启编辑':'关闭编辑' }}</button>
                <button :disabled="isEditing" class="save" @click="updateAll">保存</button>
              </div>
              <div class="btn-box load" style="display: flex; align-items: center;">
                <el-date-picker v-model="quarter" type="month" style="margin-right: 20px"/>
                <button :disabled="isEditing" class="load" @click="load(selectYear,selectMonth)">读取</button>
              </div>
              <div class="btn-box">
                <button :disabled="isEditing" class="redo">撤回所有</button>
                <button :disabled="isEditing" class="redo">撤回</button>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <div class="weeklist box">
        <ul class="ani-ul">
          <li v-if="isUpdate==false" v-for="(ani,index) in aniList[activeDay]" :key="index">
            <a class="icon" @click.stop="del(index)">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-chacha"></use>
              </svg>
            </a>
            <AnimeCard class="aniCard"  style="width: 100%;font-size: 1rem" :ani="ani.data" :show-con="ani.show"></AnimeCard>
          </li>
          <li><div class="add" @click="showModal=true"></div></li>
        </ul>
      </div>
      <FloatSearchbox v-if="showModal" @floatClose="showModal=false" @getAniCart="setDaylist"></FloatSearchbox>

  </div>
</template>

<script setup>
import AnimeCard from "@/components/common/AnimeCard.vue";
import {onMounted, reactive, ref, watch, watchEffect} from "vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import FloatSearchbox from "@/components/common/FloatSearchbox.vue";
import {tranToCard} from "@/utils/animeCard.js";
import request from "@/utils/request.js";

const aniList=reactive([[],[],[],[],[],[],[],[]]);
const activeDay=ref(1);
const totAni=ref(0);
const aniCount=reactive([0,0,0,0,0,0,0]);
const showModal=ref(false)
const axios_is=axios.create({
  baseURL: `${apiUtils.BASIC}`,
})
const quarter=ref(new Date());
const isUpdate=ref(true);
const isEditing=ref(true);
const mostDay=ref(0);
onMounted(()=>{
  getData();
});
async function getData(){
  for(let day=1;day<=7;day++){
    await axios_is.get(`/user${apiUtils.WEEKLIST_DAY_RQ}`,{
      params:{
        day: day,
      }
    })
        .then(res=>{
          aniList[day] = tranToCard(res.data.data);
        })
        .catch(err=>{console.log(err)});
  }
  isUpdate.value=false;
}
function clean(){
  aniList[activeDay.value-1].length=0;
}

function del(index){
  aniList[activeDay.value].splice(index,1);
}
function setDaylist(aniCart){
  for(let item of aniCart){
    let anttp={
      show:{
        score: false,
        state: true,
        date: true,
        name: true
      },
      data: {
        id:item.id,
        name:item.name,
        image:item.image,
        ep:item.ep,
        end:item.end,
        updateTime:item.updateTime,
      }
    }
    aniList[activeDay.value].push(anttp);
  }
  aniCount[activeDay.value]=aniList[activeDay.value].length;
  totAni.value+=aniCart.length;
}
function updateDay(day){
  let dayList=[];
  for(let item of aniList[day]){
    let wk={
      animeId:item.data.id,
      name:item.data.name,
      days: day,
      updateTime:'',
    }
    dayList.push(wk);
  }
  request({
    url:`${apiUtils.WEEKLIST_DAY_UPDATE}/${day}`,
    method:"PUT",
    data:dayList
  })
  .then(res=>{
  })
  .catch(err=>{console.log(err)});
}
function updateAll(){
  for(let i=1;i<=7;i++){
    updateDay(i);
  }
}
function load(){
  isUpdate.value=true;
  for(let i=1;i<=7;i++){
    aniList[i].length=0;
  }
  request({
    url:apiUtils.WEEKLIST_QUARTER_GET,
    method:"GET",
    params:{
      quarter:new Date(quarter.value).getTime()/1000,
    }
  })
  .then(res=>{
    for(let day=1;day<=7;day++){
      const relaylist=tranToCard(res.data.data[day]);
      aniList[day]=relaylist;
    }
  })
  .catch(err=>{
    console.log(err)
  })
  isUpdate.value=false;
}

watch(aniList,(newValue,oldValue)=>{
  totAni.value=0;
  for(let day=1;day<=7;day++){
    aniCount[day-1]=newValue[day].length;
    totAni.value+=aniCount[day-1];
  }
  aniCount.forEach((x)=>{
    mostDay.value=x>mostDay.value?x:mostDay.value;
  })
})

</script>

<style scoped>
.main-weeklist{
  width: 100%;
}
ul{
  display: flex;
}
.box{
  border-radius: 1rem;
  overflow: hidden;
  background-color: var(--admin-base-box-bg);
}
button{
  padding: 0.2rem 0.4rem;
  border-radius: 0.6rem;
  font-size: 1rem;
  border: none;
  background-color: var(--admin-base-btn);
  color: #fff;
  margin-right: 1rem;
  cursor: pointer;
}
input{
  vertical-align: middle;
  width: 3rem;
  text-align: center;
  font-size: 1rem;
  border-radius: 0.5rem;
  border: #5f5b5b 1px solid;
  height: 1.5rem;
}

.info-box{
  margin-bottom: 1rem;
  display: flex;
}

.info-box-left{
  width: 60%;
  margin-right: 1rem;
}
.graph{
  height: 10rem;
  margin-bottom: 1rem;
  padding: 1.5rem 3rem;
}
.histogram{
  width: 100%;
  height: 80%;
  border-bottom: black solid 3px;
  border-left: black solid 3px;
}
.rectangle{
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: space-around;

}
.rectangle li{
  width: 1.5rem;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  gap: 0;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;
}
.count{
  width: 100%;
  text-align: center;
  margin: 0;
  font-size: 18px;
}
.rectangleli{
  width: 100%;
  border-radius: 8px 8px 0 0;
  background-color: var(--admin-rectangle-bg);
}
.his-title{
  height: 20%;
  width: 100%;
  display: flex;
  justify-content: space-around;
}


.info-box-right{
  flex: 1;
}
.ul-info-right-box{
  width: 100%;
  height: 100%;
  flex-wrap: wrap;
}
.ul-info-right-box li{width: 100%;}
.info{
  height: 30%;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
.contro{
  height: fit-content;
  padding: 0.7rem 1rem;
}
.btn-box{
  display: flex;
  align-content: center;
  text-align: center;
  margin-bottom: 0.7rem;
  margin-left: 30%;
}
.btn-box:nth-child(2){
  margin-left: 17%;
}
.btn-box span{
  display: flex;
  align-items: center;
  margin-right: 1rem;
}
button:disabled{
  cursor: not-allowed;
  background-color: var(--admin-base-btn-disable);
  color: #dbd1d1;
}


.nav{
  padding: 0.6rem 1rem;
  border-radius: 0.9rem;
}
.nav-ul li{
  font-size: 1rem;
  width: 20%;
  margin-right: 10px;
  padding: 5px 10px;
  text-align: center;
  border-radius: 6px;
}
.active{
  background: var(--admin-base-li-active);
}
.nav-ul li:last-child{
  margin-right: 0;
}
.nav-ul a{
  text-decoration: none;
  color: black;
}

.ani-ul{
  flex-wrap: wrap;
}
.ani-ul li{
  width: 15%;
  margin-right: 2%;
  box-sizing: border-box;
  cursor: pointer;
  position: relative;
  margin-bottom: 0.4rem;
}
.icon{
  position: absolute;
  z-index: 2;
  font-size: 20px;
}

.aniCard{
  z-index: 1;
}
.ani-ul li:nth-child(6n){
  margin-right: 0;
}
.add{
  width: 100%;
  background: url("/src/assets/img/weeklistmg_add.png");
  background-size: cover;
  background-position: center;
  border-radius: 5%;
  aspect-ratio: 190.5/266.7;
}

.weeklist{
  padding: 1rem 1rem;
  background-color: var(--admin-base-box-bg);
}
</style>