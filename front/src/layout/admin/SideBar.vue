<template>
  <ul class="set">
    <li class="setli" :class="{active:item.onAir}" v-for="item in ulItem" :key="item.id" @click.stop="routeChange(item,'',null)">
      <a href="#">{{ item.name }}</a>
        <ul class="child" v-if="item.onAir&&'childs' in item">
          <li :class="{active:itemc.onAir}" v-for="itemc in item.childs" :key="itemc.id" @click.stop="routeChange(itemc,item.path,item)">
            <a href="#">{{ itemc.name }}</a>
          </li>
        </ul>
    </li>
  </ul>
</template>

<script setup>
import {reactive} from "vue";
import {useRoute, useRouter} from "vue-router";
import {ROUTE_PATH} from "@/route/pathset.js";


const router=useRouter()
const ulItem=reactive([
  {id:1,name:'番剧管理',path:`/${ROUTE_PATH.ADMIN_ANI}`,onAir:false,childs:[
      {id:1,name:'新增番剧',path:'/add',onAir:false},
      {id:2,name:'修改番剧',path:'/update',onAir:false,},
    ]},
  {id:2,name:'周表管理',path:'/weeklistmg',onAir:false},
  {id:3,name:'公告管理',path:'/announcement',onAir:false,childs:[]},
  {id:4,name:'用户管理',path:'/usermanage',onAir:false},
  {id:100,name:'测试',path:'/test',onAir:false,childs:[
      {id:100,name:'番卡测试',path:'/anicard',onAir:false},
      {id:101,name:'周表测试',path:'/weeklist',onAir:false},
    ]},

])


function routeChange(item,lpath,fitem){
  const hasChildren='childs' in item;
  if(!hasChildren){
    let path='/admin'+lpath+item.path;
    router.push(path);
  }
  if(fitem==null){
    for(const i of ulItem){i.onAir=false}
    if(item.onAir==false&&hasChildren){
      for(const i of item.childs){i.onAir=false}
    }
  }
  else if('childs' in fitem){
    for(const i of fitem.childs){i.onAir=false}
  }
  item.onAir=true;
}
</script>

<style scoped>

li{
  min-height: 40px;
  line-height: 40px;
  text-align: center;
  cursor:pointer;
}
.set{
  background-color: var(--side-nav-li-bg);
}
.setli:hover,.setli.active{
  background-color: var(--side-nav-li-bg-hover);
}

.child{
  background-color: var(--side-nav-cul-bg);
}

.child li:hover,ul.child li.active{
  background-color: var(--side-nav-li-c-bg-hover);
}

a{
  font-size:22px;
  text-align: center;
  text-decoration: none;
  color: var(--side-nav-li-font-color);
}
.child a{
  font-size:18px;
}
</style>