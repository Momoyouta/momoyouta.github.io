<template>
  <div class="conta">
    <div class="divide"></div>
    <InfoCard v-if="anime && Object.keys(anime).length" :anime="anime" class="info cp"></InfoCard>
    <AdCard class="ad cp"></AdCard>
    <VedioList class="vedio cp"></VedioList>
  </div>
</template>

<script setup>

import InfoCard from "@/components/animedetail/InfoCard.vue";
import AdCard from "@/components/common/AdCard.vue";
import VedioList from "@/components/animedetail/VedioList.vue";
import {onMounted, reactive, ref} from "vue";
import {useRoute} from "vue-router";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
const route = useRoute();
const anime=ref({});
const props=defineProps({
  animeid: { type: String, required: true },
})
const axs=axios.create({
  baseURL:`${apiUtils.BASIC}`,
})
onMounted(()=>{
  init();
})
async function init(){
  const res=await axs.get(`${apiUtils.USER_GET_ANIME_DETAIL}/${props.animeid}`)
      .then(res=>res.data.data)
      .catch(err=>{console.log(err)});
  anime.value = res;
}
</script>

<style scoped>
.divide{
  width: 100%;
  height: 0.3rem;
  border-bottom: var(--input-base) solid 2px;
  margin-bottom: 0.5rem;
}
.cp{
  margin-bottom: 1rem;
}
</style>