<template>
  <div class="wapper">
    <div class="main">
      <SearchBox style="width: 31rem" class="searchbox" />
    </div>
    <div class="head-box">
      <div class="history">
        <span class="iconfont icon-jilu"></span>
        <span class="his-span">历史记录</span>
      </div>
      <button class="login-btn" v-if="!isLogin">
        <span class="iconfont icon-geren"></span>登录</button>
      <a class="profile" v-if="isLogin">
        <img class="user-image" :src="user.image" alt="">
      </a>
    </div>
  </div>

</template>

<script setup>
import SearchBox from "@/components/common/SearchBox.vue";
import {reactive, ref} from "vue";

const isLogin=ref(0);
const user=reactive({
  id: 1,
  image: 'src/assets/img/user_image_test.png',
})
</script>

<style scoped>
.wapper{
  display: flex;
}

.main{
  flex: 1;
  display: flex;
  height: 3.6rem;
  align-items: center;
  background-color: var(--base-bg);
}
.searchbox{
  margin-left: 2.5rem;
  border-radius: 0.5rem;
}
.head-box{
  display: flex;
  align-items: center;
  height: 3.6rem;
  margin-right: 2.5rem;
  font-size: 1rem;
}
.history{
  display: flex;
  align-items: center;
  vertical-align: middle;
  margin-right: 1.5rem;
  height: 2rem;
}
.his-span{
  cursor: default;
  vertical-align: middle;
  line-height: 3.6rem;
}
.iconfont{
  font-size: 1.2rem;
  margin-right: 0.2rem;
  vertical-align: middle;
}
.user-image{
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 100%;
  object-fit: cover;
  border: var(--side-bg) solid 3px;
}
.login-btn{
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  line-height: 1rem;
  height: 2rem;
  border: none;
  font-size: 1rem;
  color: white;
  box-shadow: 2px 10px 2px var(--btn-login-shdow);
  background-color: var(--btn-login);
}
.login-btn:hover{
  box-shadow: 2px 10px 2px var(--btn-login-shdow-hover);
  background-color: var(--btn-login-hover);
}

</style>