<template>
  <BangumiApi></BangumiApi>
  <div class="wapper">
    <div class="img-upload">
      <img :src="imgSrcPreview" alt="">
      <div class="input-img-info" @error="setDefaultImg">
        <div class="fuzhu">
          <label class="file-btn" for="upload" @click="handleImgupload">文件上传</label>
          <input id="upload" type="file" class="file-input" accept="image/*">
          <div class="input-item-textarea">
            <span class="img-text">图片链接:</span>
            <textarea class="img-src" v-model="aniDTO.image" rows="4" placeholder="请输入图片链接..."></textarea>
          </div>
        </div>
      </div>
    </div>
    <form class="main-form" onsubmit="return false">
      <div class="left">
        <div class="input-item">
          <span class="input-item-lab">番名：</span>
          <input v-model="aniDTO.name" class="input-name" type="text" style="width: 20rem" placeholder="请输入番名">
        </div>
        <div class="input-item">
          <span class="input-item-lab">播放地址：</span>
          <input v-model="aniDTO.videoList" class="input-video-addr" type="text" style="width: 20rem" placeholder="请输入播放地址">
        </div>
        <div class="input-item-textarea2">
          <span class="input-item-lab">简介：</span>
          <textarea v-model="aniDTO.description" class="input-description"  rows="4" placeholder="请输入内容..."></textarea>
        </div>
        <div class="input-item-textarea2">
          <span class="input-item-lab">CV表：</span>
          <textarea v-model="aniDTO.cvList" class="input-cvlist"  rows="4" placeholder="请输入内容..."></textarea>
        </div>
      </div>
      <div class="right">
        <div class="input-item">
          <span class="input-item-lab">评分：</span>
          <input v-model.number="aniDTO.score" class="input-name" type="text" style="width: 1.6rem" placeholder="请输入番名">
        </div>
        <div class="input-item">
          <span class="input-item-lab">观看次数：</span>
          <input v-model.number="aniDTO.views" class="input-name" type="text" style="width: 6rem" placeholder="请输入番名">
        </div>
        <div class="input-item">
          <span class="input-item-lab">是否完结：</span>
          <input v-model="checkbox" class="input-name" type="checkbox" style="width: 6rem">
        </div>
        <div class="input-item">
          <span class="input-item-lab">集数：</span>
          <input v-model.number="aniDTO.ep" class="input-name" type="text" style="width: 6rem" placeholder="">
        </div>
        <div class="btn">
          <button class="submitt" @click="submitt">提交</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup>

import {computed, reactive, ref, watch} from "vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import LayOut from "@/App.vue";
import BangumiApi from "@/components/admin/animation/BangumiApi.vue";
const imgDefaultSrc="/src/assets/img/ani_add_error.png";
const imgSrcPreview = ref(imgDefaultSrc);
let imgExist=false;
const aniDTO=reactive({
  name: '',
  image: '',
  videoList:'',
  description:'',
  cvList:'',
  views:0,
  score:0,
  ep:0,
  end: 0,
});
const checkbox=computed({
  get() {
    return aniDTO.end == 1 ? 1 : 0;
  },
  set(newValue){
    aniDTO.end=newValue?1:0
  }
})
const axios_instance = axios.create({
  baseURL: `${apiUtils.BASIC}`,
})

function handleImgupload(event){
  aniDTO.image="/src/assets/img/ani_test.webp";
}

function setDefaultImg(event){
  event.target.src = imgDefaultSrc;
}

function submitt() {
  if (aniDTO.name == '') {
    alert("番名不能为空");
    return
  }
  if (aniDTO.image == '') {
    alert("图片不能为空");
    return
  }
  if(!imgExist){alert("图片不存在");return;}
  console.log(aniDTO.ep)
  axios_instance.put(`${apiUtils.ADD_ANI}`,aniDTO)
  .then(res => {
    if(res.data.code==1){alert("添加成功")}
    else{alert("番剧已存在，添加失败");}})
      .catch(error=>{console.log(error);alert("添加失败")});
}
// 检查图片是否存在
const checkImageExistence = (url) => {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.onload = () => resolve(true);
    img.onerror = () => reject(false);
    img.src = url;
  });
};

// 监听 imgSrc 变化并检查图片是否存在
watch(()=>aniDTO.image, async (newUrl) => {
  try{
    const exists = await checkImageExistence(newUrl);
    imgSrcPreview.value = newUrl;
    imgExist=true;
  }
  catch(error){
    imgSrcPreview.value = imgDefaultSrc;
    imgExist=false;
  }

});


</script>

<style scoped>
.main{
  width: 100%;
  height: 100%;
  padding: 0 30px;
}
.wapper{
  width: 100%;
  height: 100%;
  background: #f6f4f4;
  padding: 0 2rem;
  padding-top: 1rem;
}

.img-upload{
  display: flex;

}
img{
  width: 11.5rem;
  height: 16.25rem;
  margin-right: 1rem;
}
.input-img-info{
  position: relative;
  width: 25rem;
}
.fuzhu{
  position: absolute;
  bottom: 0;
}
.file-btn{
  display: inline-block;
  margin-bottom: 1rem;
  padding: 0.2rem 0.4rem;
  cursor: pointer;
  border: #404040 solid 1px;
  border-radius: 2px;
}
.file-input{
  display: none;
}
.input-item-textarea,.input-item-textarea2{
  display: flex;
  margin-bottom: 1rem;
}
span{
  white-space: nowrap;
  margin-right: 0.5rem;
}
textarea {
  padding: 0.25rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}
.img-src{
  width: 100%;
}
.input-description,.input-cvlist{
  width: 20rem;
}
.input-item input{
  display: inline-block;
  border: 1px solid #ccc;
  height: 80%;
  vertical-align: middle;
}
.input-item{
  display: flex;
  align-items: center;
  height: 1.6rem;
  margin-bottom: 1rem;
}

.input-item span,.input-item-textarea2 span{
  display: inline-block;
  height: 100%;
  width: 5rem;
  text-align: right;
  line-height: 1.4rem;
}

.main-form{
  display: flex;
  margin-top: 1.5rem;
}

.right{
  margin-left: 2rem;
}


</style>