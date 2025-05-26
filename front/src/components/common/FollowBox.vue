<template>
  <div class="bd" style="background: var(--base-deep-bg);overflow: hidden;">
    <div class="top-bar" style="height: 2rem;padding: 0.2rem 0;background-color: #86a972;position: relative;">
      <button style="position: absolute;right: 0.6rem;">
        从购物车导入
      </button>
    </div>
    <div style="display: flex;height: calc(100% - 2rem)">
      <div class="left" style="width: 40%;border-right: black solid 0.1rem;">
        <el-scrollbar wrap-style="padding: 0.5rem">
          <ListUl :items="animeTp" :n="4" :item-width="24">
            <template #default="{ item: anime ,index}">
              <div style="display: flex;width: 100%;flex-wrap: wrap;padding: 5px;border-radius: 10px;background-color: rgba(227,102,68,0.3)" >
                <div class="followCover" style="position: relative;"@click="chooseItem(index)">
                  <el-tooltip placement="top" effect="light" :content="anime.name">
                    <img :src="anime.image" alt="" style="object-fit: contain;width: 100%;height: 100%;"/>
                  </el-tooltip>
                  <div class="imgMask">
                    [{{anime.schedule}}/{{anime.ep}}]
                  </div>
                </div>
                <div style="height: 0.35rem;border-radius: 0.35rem;background-color: white;width: 100%;">
                  <div :style="{width:`${anime.schedule/anime.ep*100}%`}" class="slider"></div>
                </div>
              </div>
            </template>
          </ListUl>
        </el-scrollbar>
      </div>
      <div class="right" style="width: calc(100% - 0.1rem)">
        <div class="main-box" style="display: flex;margin: 3% auto;width: 80%">
          <div class="info-panel" style="flex:5 ;display: flex;background: #76b89a;padding: 3%;border-radius: 0.5rem;overflow: hidden">
            <img :src="activeAnime.image" style="width: 30%;aspect-ratio: var(--anime-cover-ratio);border-radius: 0.5rem;">
            <div class="info-box" style="margin-left: 4%;padding-top: 6%;width: 100%">
              <el-text style="color: black;font-size: 1rem;cursor: default" :line-clamp="2" >
               {{ activeAnime.name }}</el-text>
              <div style="font-size: 0.85rem;margin-bottom: 6%;">{{ activeAnime.doing }} 人在追</div>
              <div class="bar" style="display: flex;align-items: center;margin-bottom: 3%;width: 100%">
                <div class="progress-bar" :style="{background: 'linear-gradient(to right, #ed5f5f '
                +activeAnime.schedule/activeAnime.ep*100+'%,transparent '+activeAnime.schedule/activeAnime.ep*100+'%)'}"></div>
                <input v-model="changeSchedule" type="text" :placeholder="activeAnime.schedule" class="progress-bar-input">
                <span style="line-height: 1rem;font-size: 1rem">/{{ activeAnime.ep }}</span>
              </div>
              <div class="option">
                <a href="#" style="padding-left: 1rem;">写评论</a>
              </div>
            </div>
          </div>
          <div class="contro-panel" style="">
            <ul>
              <li><button>弃置</button></li>
              <li>
                <button v-if="isOneUpdate" :disabled="activeAnime.schedule === activeAnime.ep" @click="updateSchedule(activeAnime.id, activeAnime.schedule + 1)">
                  {{ activeAnime.schedule === activeAnime.ep ? '完成' : `ep${activeAnime.schedule + 1} 看过` }}
                </button>
                <button v-if="!isOneUpdate" @click="updateSchedule(activeAnime.id, changeSchedule)">更新进度</button>
              </li>
              <li><button @click="gotoDetail(activeAnime.id)">前往详情页</button></li>
            </ul>
          </div>
        </div>
        <div style="display: flex;flex-wrap: wrap;margin-top: 1rem;margin-left: 18%;">
          <ul class="ep-list" style="display: flex;flex-wrap: wrap;max-width: 70%;">
            <li v-for="item in activeAnime.ep" :style="{'background-color': item<=activeAnime.schedule?'#68cbe1':''}">{{ item }}</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, watch, onMounted } from 'vue'
import ListUl from "@/components/common/ListUl.vue";
import {router} from "@/route/config.js";
const props = defineProps({
  animeTp: {
    type: Array,
    required: true
  }
})

const doingAnimes = reactive([])
const activeIndex = ref(0)
const scheduleType = ref(1)
const changeSchedule = ref()
const isOneUpdate = ref(true)
let lockone = false

onMounted(() => {
  doingAnimes.splice(0, doingAnimes.length, ...props.animeTp.map(a => ({ ...a, isHover: false })))
})

const activeAnime = computed(() => doingAnimes[activeIndex.value] || {})

const animelistStyle = computed(() => {
  if (scheduleType.value === 2) {
    return {
      'margin-bottom': '0.2rem',
      'margin-right': '0.65rem',
    }
  } else {
    return {}
  }
})

function updateSchedule(animeId, ep) {
  for (let i = 0; i < doingAnimes.length; i++) {
    if (doingAnimes[i].id === animeId) {
      doingAnimes[i].schedule = Number(ep)
      break
    }
  }
  isOneUpdate.value = true
}
function chooseItem(index) {
  activeIndex.value = index
}
function gotoDetail(id) {
  router.push({
    name:'getAnimeDetail',
    params: { animeid:id}
  })
}

watch(changeSchedule, () => {
  if (!lockone)
    isOneUpdate.value = false
  else
    lockone = true
})
</script>

<style scoped>
.bd{
  border-radius: 0.5rem;
}
.followCover{
  border-radius: 10px;
  aspect-ratio: var(--anime-cover-ratio);
  overflow: hidden;
  margin-bottom: 5px;
  cursor: pointer;
}
.imgMask{
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 35%;
  background: linear-gradient(to top, #302f2f 0%,rgba(0,0,0,0) 100%);
  text-align: center;
  color: white;
  padding-top: 0.5rem;
  cursor: pointer;
  z-index: 99;
  font-size: 0.6rem;
}
.slider{
  background-color: #8686f8;
  border-radius: 0.35rem;
  height: 100%;
}

.bar .progress-bar{
  display: flex;
  align-items: center;
  width: 70%;
  height: 1.2rem;
  border: var(--btn-login-hover) solid 0.1rem;
  border-radius: 1.2rem;

}
.progress-bar-input{
  width: 1.7rem;
  text-align: center;
  height: 1.2rem;
  margin-left: 1rem;
  margin-right: 0.5rem;
  border-radius: 0.5rem;
  border: none;
}
.contro-panel{
  flex: 1;
  background-color: #76b89a;
  display: flex;
  align-items: center;
  margin-left:1rem;
  border-radius: 1rem;
  padding: 1rem;
}
.contro-panel ul{
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}
.contro-panel button{
  width: 2.5rem;
  height: 2.5rem;
  margin-bottom: 1rem;
  background-color: var(--btn-login);
  color: #272424;
  border-radius: 0.35rem;
}

.top-bar button{
  height:1.6rem;
  font-size: 0.7rem;
  vertical-align: top;
  padding: 0.2rem;
  border-radius: 0.2rem;
  background-color: #55a532;
  border: #498d2c dashed 2px;
  cursor: pointer;
}
.top-bar button:hover{
  background-color: #468b29
}

.ep-list li{
  width: 1.5rem;
  height: 1.5rem;
  text-align: center;
  background-color: var(--input-base);
  margin-right: 0.3rem;
  margin-bottom: 0.5rem;
  border-radius: 0.2rem;
  cursor: pointer;
  line-height: 1.5rem;
}
</style>


