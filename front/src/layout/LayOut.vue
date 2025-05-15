<template>
  <link rel="stylesheet" href="/assets/icon/fonticon/iconfont.css">
  <div class="layout">
    <div class="top">
      <TopNavbar />
    </div>
    <div class="side">
      <SideNavbar />
    </div>
    <div class="main">
      <router-view name="main"/>
    </div>
    <FooterBar></FooterBar>
  </div>
</template>

<script setup>

import SideNavbar from "./SideNavbar.vue";
import TopNavbar from './TopNavbar.vue';
import FooterBar from "@/layout/FooterBar.vue";

</script>

<style scoped>
*{
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

.layout{
  width: 100%;
  min-height: 100vh;
}
.side{
  width: 10rem;
  background-color: var(--side-bg);
  position: fixed;
  top: 3.6rem;
  left: 0;
  bottom: 0;
  z-index: 900;
}

.top{
  width: 100%;
  height: 3.6rem;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}
.main {
  margin-top: 3.6rem;
  margin-left: 10rem;
  padding: 0 2.5rem;
  flex: 1;
  margin-bottom: 3.6rem;
}

.content {
  display: flex;
  height: calc(100vh - 3.6rem);
}
</style>