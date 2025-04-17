<template>
  <div class="main-search-page">
    <AnimeSearchResult :keyword="keyword"></AnimeSearchResult>
  </div>
</template>

<script setup>

import AnimeSearchResult from "@/components/search/AnimeSearchResult.vue";
import {onMounted} from "vue";
onMounted(()=>{

})
const props = defineProps({
  keyword:{
    type: String,
    required: true,
  }
})
</script>

<style scoped>

</style>