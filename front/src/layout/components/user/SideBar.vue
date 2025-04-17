<template>
  <ul class="set">
    <li class="setli" :class="{active: activeLi===index, option: isOption(index)}" v-for="(item,index) in ulItem" :key="item.id"
        @click.stop="routeChange(item,index)">
      <a href="#" :class="{active:activeLi===index}" v-if="isOption(index)">
        <span :class="['iconfont',`icon-${item.icon}`]"></span>
        {{ item.name }}
      </a>
      <div class="hr" v-if="!isOption(index)">--------------</div>
    </li>
  </ul>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";


const router=useRouter()
const ulItem=reactive([
  {id:1,name:'首页',path:'/home',icon:'home-g'},
  {id:0,name:'---',path:'#',icon:''},
  {id:2,name:'目录',path:'/directory',icon:'mulu'},
  {id:3,name:'安利广场',path:'/recmground',icon:'tuijian'},
  {id:4,name:'追番计划',path:'/animefollow',icon:'jihua_plan'},
  {id:0,name:'---',path:'#'},
  {id:5,name:'最近更新',path:'/recentupdate',icon:'gengxin'},
  {id:6,name:'TEST',path:'/test',icon:'shebeixiangqing'},
])
const activeLi=ref(0);
onMounted(()=>{
  init()
})
function init(){
  const cpath=router.currentRoute.value.path;
  for(let i=0;i<ulItem.length;i++){
    const item=ulItem[i];
    if(item.path===cpath){
      if(item.id===1)
        activeLi.value=0;
      else if(item.id>=5)
        activeLi.value=item.id+1;
      else
        activeLi.value=item.id;
      break;
    }
  }
}
function routeChange(item,index){
  if(item.path==='#') return;
  let path=item.path;
  router.push(path);
  activeLi.value=index;
}

const isOption=computed(()=>(index)=>{
  return index!==1&&index!==5;
})
</script>

<style scoped>
li{
  color: var(--base-font);
}

li.option{
  position: relative;
  height: 2.1rem;
  line-height: 2.1rem;
  cursor:pointer;
  padding-left: 1.5rem;
}

li.option.active::after{
  position: absolute;
  z-index: 2;
  right: 0;
  bottom: -12px;
  background-color: var(--side-bg);
  width: 12px;
  height: 12px;
  content: "";
  border-radius: 0 12px 0 0;
}

li.option.active::before{
  position: absolute;
  z-index: 2;
  right: 0;
  top: -12px;
  background-color: var(--side-bg);
  width: 12px;
  height: 12px;
  content: "";
  border-radius: 0 0 12px 0;
}

a.active::after{
  position: absolute;
  right: 0;
  bottom: -12px;
  background-color: var(--base-bg);
  width: 12px;
  height: 12px;
  content: "";
}
a.active::before{
  position: absolute;
  right: 0;
  top: -12px;
  background-color: var(--base-bg);
  width: 12px;
  height: 12px;
  content: "";
}
a{
  font-size:1rem;
  text-decoration: none;
  color: var(--base-font);
}
a.active{
  color:var(--side-font-active);
  font-weight: bold;
}
li .hr{
  min-height: 1.5rem;
  line-height: 1.5rem;
  text-align: center;
  color: var(--base-bg);
  cursor: default;
}
li:nth-child(3) a .iconfont,li:nth-child(7) a .iconfont{
  font-size: 120%;
  margin-right: 9px;
}
.set{
  background-color: var(--side-bg);
}
.setli.active.option {
  background: var(--base-bg);
}

.iconfont{
  font-size: 1rem;
  margin-right: 12px;
}

</style>