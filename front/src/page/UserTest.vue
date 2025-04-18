<template>
  <div class="divide"></div>
  <el-button @click="testJWT">解析jwt</el-button>
</template>

<script setup>

import AnimeDetail from "@/page/AnimeDetailPage.vue";
import AnimeSearchPage from "@/page/AnimeSearchPage.vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
function testJWT(){
  axios.get(`${apiUtils.BASIC}/user/testJWT`, {
    headers:{
      'token': localStorage.getItem('accessToken'),
    },
    params: {
      jwt:localStorage.getItem("accessToken"),
    }
  }).then(res => {
    console.log(res.data);
  }).catch(err => {
    console.log(err)
  })
}

</script>

<style scoped>
.divide{
  width: 100%;
  height: 0.3rem;
  border-bottom: var(--input-base) solid 2px;
  margin-bottom: 0.5rem;
}
</style>