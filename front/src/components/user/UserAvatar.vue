<template>
  <div style="height: 100%;display: flex; justify-content: center;align-items: center">
    <button class="login-btn" v-if="!isLogin" @click="handleLogin">
      <span class="iconfont icon-geren"></span>
      登录
    </button>
    <el-dropdown v-if="isLogin">
      <el-avatar fit="contain" :src="userInfo.avatarUrl">
      </el-avatar>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item v-for="item in dropDownItems" @click="handleJump(item.path)">{{item.label}}</el-dropdown-item>
          <el-dropdown-item @click="outLogin">退出登录</el-dropdown-item>

        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>


import {computed, onMounted, reactive, ref} from "vue";
import {router} from "@/route/config.js";
import {ROUTE_PATH} from "@/route/pathset.js";
const userInfo=ref({});

onMounted(()=>{
  const baseInfo=JSON.parse(localStorage.getItem("userInfo"));
  isLogin.value=baseInfo!==null;
  userInfo.value=baseInfo;
})

//下拉项
const dropDownItems=computed(() =>[
  {path:`${ROUTE_PATH.USER_PROFILE}/${userInfo.value.id}`,label:'个人主页'},
  {path:ROUTE_PATH.USER_FAVORITE,label:'番剧收藏'},
  {path:ROUTE_PATH.USER_UPDATE_INFO,label:'个人信息修改'},
]);
function handleJump(path){
  router.push(`/user/${path}`);
}

//登录相关
const isLogin=ref(false);
function handleLogin(){
  router.push('/login');
}
function outLogin(){
  localStorage.removeItem("userInfo");
  localStorage.removeItem("roles");
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  location.reload();
}

</script>

<style scoped>
.iconfont{
  font-size: 1.2rem;
  margin-right: 0.2rem;
  vertical-align: middle;
}
.login-btn{
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  line-height: 1rem;
  height: 2rem;
  width: fit-content;
  border: none;
  margin: auto;
  font-size: 1rem;
  color: white;
  box-shadow: 2px 4px 2px var(--btn-login-shdow);
  background-color: var(--btn-login);
}
.login-btn:hover{
  box-shadow: 2px 4px 2px var(--btn-login-shdow-hover);
  background-color: var(--btn-login-hover);
}
</style>