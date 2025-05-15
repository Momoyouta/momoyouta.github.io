<template>
  <div class="wapper">
    <div class="main">
      <SearchBox @getSearch="search($event)" style="width: 31rem" class="searchbox" />
    </div>
    <div class="head-box">
      <div class="history">
        <span class="iconfont icon-jilu"></span>
        <span class="his-span">历史记录</span>
      </div>
      <div class="userAvatar" style="height: 100%;">
        <UserAvatar></UserAvatar>
      </div>

    </div>
  </div>

</template>

<script setup>
import SearchBox from "@/components/common/SearchBox.vue";
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import UserAvatar from "@/components/user/UserAvatar.vue";
const routre=useRouter();
function search(keyword){
  routre.push({
    name:'searchAnime',
    params:{keyword},
  })
}
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


</style>