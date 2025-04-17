<template>
  <div class="divide"></div>
  <SlideBanner class="slidebanner" ></SlideBanner>
  <AdCard class="notice"></AdCard>
  <WeekList class="weeklist" style="width: 100%;"></WeekList>
  <HotAnime class="hotanime"></HotAnime>
</template>

<script setup>

import SlideBanner from "@/components/home/SlideBanner.vue";
import WeekList from "@/components/home/WeekList.vue";
import HotAnime from "@/components/home/HotAnime.vue";
import AdCard from "@/components/common/AdCard.vue";
</script>

<style scoped>
.divide{
  width: 100%;
  height: 0.3rem;
  border-bottom: var(--input-base) solid 2px;
  margin-bottom: 0.5rem;
}
.notice{
  margin-bottom: 1rem;
}
.slidebanner{
  margin-bottom: 1rem;
}
.weeklist{
  margin-bottom: 1rem;
}

</style>