<template>
  <div>
    <el-button type="primary" @click="console.log(commonStore.seasonAnime)">pinia测试</el-button>
    <el-button type="primary" @click="testJWT">jwt测试</el-button>
  </div>
</template>

<script setup>
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import {useCartStore} from '@/store/shoppingCar.js'
import {onMounted, ref} from "vue";
import {useCommonStore} from "@/store/commonStore.js";
import request from "@/utils/request.js";
function testJWT(){
  request({
    method:"get",
    url:`${apiUtils.BASIC}/user/testJWT`,
    params:{
      jwt:localStorage.getItem("accessToken"),
    }
  }).then(res => {
    console.log(res);
  }).catch(err => {
    console.log(err)
  })
}
const store=ref(0);
const commonStore=useCommonStore()
onMounted(()=>{

})

</script>

<style scoped>
.divide{
  width: 100%;
  height: 0.3rem;
  border-bottom: var(--input-base) solid 2px;
  margin-bottom: 0.5rem;
}
</style>