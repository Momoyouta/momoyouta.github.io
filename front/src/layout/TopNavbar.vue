<template>
  <div class="wapper">
    <div class="img">
      <img src="../assets/img/web_icon.png" alt="" class="webicon">
    </div>
    <div class="main">
      <router-view name="TopNavbar"/>
    </div>
  </div>
</template>

<script setup>


</script>

<style scoped>
.wapper{
  display: flex;
  height: 100%;
  background-color: var(--base-bg);
}
.img{
  background-color: var(--side-bg);
}
.webicon{
  height: 3.6rem;
  width: 10rem;
}
.main{
  flex: 1;
}
</style>