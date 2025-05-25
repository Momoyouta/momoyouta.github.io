<template>
  <div class="divide">
    <span class="head">晚上好！user_name！</span>
  </div>
  <FollowBox :animeTp="animeTp" style="height: 25rem"/>
</template>

<script setup>

import {computed, onMounted, reactive, ref, watch} from "vue";
import axios from "axios";
import FollowBox from "@/components/common/FollowBox.vue";

const animeTp=[
    {
      id:2106085945,
      name:'BanG Dream! Ave Mujica',
      image:'https://lain.bgm.tv/r/400/pic/cover/l/77/c3/454684_ZH5tU.jpg',
      ep:13,
      doing: 10086,
      schedule: 10
    },
  {
    id:2106085705,
    name:'噗妮露是可爱史莱姆',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/fe/ab/470045_zA9BA.jpg',
    ep:12,
    doing: 61086,
    schedule: 6
  },
  {
    id:2106085720,
    name:'在地下城寻求邂逅是否搞错了什么 第五季 丰饶的女神篇',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/bc/42/463778_AEsHA.jpg',
    ep:24,
    doing: 1086,
    schedule: 3
  },
  {
    id:2106085755,
    name:'深夜重拳',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
    ep:13,
    doing: 8086,
    schedule: 8
  },
  {
    id:2106085755,
    name:'深夜重拳',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
    ep:13,
    doing: 8086,
    schedule: 8
  },
  {
    id:2106085755,
    name:'深夜重拳',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
    ep:13,
    doing: 8086,
    schedule: 8
  },
  {
    id:2106085755,
    name:'深夜重拳',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
    ep:13,
    doing: 8086,
    schedule: 8
  },
  {
    id:2106085755,
    name:'深夜重拳',
    image:'https://lain.bgm.tv/r/400/pic/cover/l/0c/cf/477207_0lA1U.jpg',
    ep:13,
    doing: 8086,
    schedule: 8
  },
]

onMounted(()=>{
})

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
}

</style>
