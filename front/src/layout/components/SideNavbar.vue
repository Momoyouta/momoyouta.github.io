<template>
  <div class="wapper">
    <router-view name="SideNavbar"/>
    <RouterLink to="/admin" v-if="isDev">Admin   </RouterLink>
    <RouterLink to="/home" v-if="isDev">User</RouterLink>
  </div>
</template>

<script setup>
import {ref} from "vue";

const isDev=ref(import.meta.env.DEV);
</script>

<style scoped>

.wapper{
  margin-top:20px;
}
</style>