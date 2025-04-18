<template>
  <div class="wapperr">
    <div v-show="cartVisiable" class="cart-list">
      <ul class="cart-ul">
        <li v-for="(ani,index) in aniCart" :key="ani.id">
          <span>{{ani.name}}</span>
          <a class="icon-cart-close" @click="delAnifromCart(index)">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-cuowu"></use>
            </svg>
          </a>
        </li>
      </ul>
    </div>
    <div class="container">
      <a class="icon-close" @click="close">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-chacha"></use>
        </svg>
      </a>
      <div class="cart">
        <span>{{ aniCart.length }}</span>
        <a class="icon-cart" @click="cartVisiable = cartVisiable ^ 1">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-download"></use>
          </svg>
        </a>
      </div>
      <div class="sbox">
        <SearchBox style="border-radius: 1rem;height: 2rem" class="search-box" @getSearch="search"></SearchBox>
      </div>
      <div class="list">
        <ul class="ani-uul">
          <li @click.capture.stop.prevent="handlerCart(ani.data,index)" v-for="(ani,index) in aniList" :key="index">
            <div :class="{'border-helper':aniChoose[index]}">
              <AnimationCard  style="width: 100%;font-size: 12px" :ani="ani.data" :show-con="ani.show"></AnimationCard>
            </div>
          </li>
        </ul>
      </div>
      <button class="submit-btn" @click="submit">确认</button>
    </div>

  </div>
</template>

<script setup>
import SearchBox from "@/components/common/SearchBox.vue";
import {onMounted, reactive, ref} from "vue";
import AnimationCard from "@/components/common/AnimationCard.vue";
import {apiUtils} from "@/common/apiUtils.js";
import axios from "axios";
import {tranToCard} from "@/hooks/animeCard.js";
const emit=defineEmits(['floatClose','getAniCart']);
const aniList=reactive([]);
const aniCart=reactive([])
const cartVisiable=ref(false);
const aniChoose=reactive([]);
const axios_instance=axios.create({
  baseURL: `${apiUtils.BASIC}`,
})
onMounted(()=>{

})
function close(){
  emit("floatClose")
}
function addTocart(ani){
  // console.log(JSON.stringify(ani));
  aniCart.push(ani);
}
function delAnifromCart(index){
  aniCart.splice(index,1);
}
function handlerCart(ani,index){
  if(!aniChoose[index]){
    addTocart(ani);
    aniChoose[index]=true;
  }else{
    delAnifromCart(index);
    aniChoose[index]=false;
  }
}
function submit(){
  emit('getAniCart',aniCart);
  close();
}
function search(name){
  aniList.length=0;
  aniChoose.length=0;
  axios_instance.get(`/admin${apiUtils.SEARCH_ANI}/bynamelike`,{
    params:{
      name: name,
      page: 1,
      pageSize: 999999,
      useDscp: false,
    }
  })
      .then(res => {
          const newData=tranToCard(res.data.data);
          aniList.push(...newData);
          aniChoose.push(0);
      })
      .catch(err=>{console.log(err)})
}

</script>

<style scoped>
.wapperr{
  position: fixed;
  z-index: 99;
  top: 3rem;
  right: 9%;
  width: 75%;
  height: 32rem;
  display: flex;
  justify-content: center;
  border-radius: 10px;
  overflow: hidden;

}
.list{
  height: 73%;
  overflow-y: scroll;
}
.cart-list{
  width: 20%;
  height: 100%;
  background-color: #c3d6be;
  margin-right: 2%;
  border-radius: 10px;
  padding: 2% 2%;
}

.cart-ul li{
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  width: 100%;
  overflow: hidden;
}
.cart-ul li span{
  max-width: 85%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.cart-ul li:nth-child(2n){

}
.icon-cart-close{
  font-size: 12px;
  color: #5f5b5b;
}


.ani-uul{
  position: relative;
  display: flex;
  flex-wrap: wrap;
}
.ani-uul li{
  width: 15%;
  margin-right: 2%;
  box-sizing: border-box;
  cursor: pointer;
  margin-bottom: 1%;

}
.ani-uul li:nth-child(6n){
  margin-right: 0;
}
.border-helper{
  border: #ea8f8f solid 2px;
  border-radius: 2px;
  overflow: hidden;
}

.container{
  position: relative;
  height: 100%;
  width: 73%;
  background-color: #c3d6be;
  padding: 1rem 3rem;
  flex-wrap: wrap;
  border-radius: 10px;
}
.sbox{
  height: 2rem;
  display: flex;
  justify-content: center;
  overflow: hidden;
  margin-bottom: 1rem;
}
button{
  position: absolute;
  bottom: 2rem;
  left: 40%;
  width: 20%;
  height: 2rem;
}
.icon{
  cursor: pointer;
}
.icon-close{
  position: absolute;
  left: 95%;
  top:2%;
}
.cart{
  text-align: center;
  position: absolute;
  z-index: 211;
  top: 50%;
  left: 1%;
  height: 2rem;
  width: 2rem;
  font-size: 1.5rem;
}
.cart span{
  position: absolute;
  top:-3px;
  right: -3px;
  font-size: 8px;
  color: white;
  background-color: #ed6363;
  border-radius: 4px;
  padding: 1px 2px;
}
</style>