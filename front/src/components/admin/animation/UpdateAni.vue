<template>
  <div class="main-list" v-if="!detail">
    <div class="wapper">
      <div class="table">
        <div class="search">
          <SearchBox class="search-box"  @getSearch="search"/>
        </div>
        <div class="title">
          <div class="name">番名</div>
          <div class="update-time">更新时间</div>
          <div class="action">操作</div>
        </div>
        <div class="list">
          <ul class="ul-list">
            <li class="list-item" v-for="ani in aniList" :key="ani.id">
              <div class="name">{{ani.name}}</div>
              <div class="update-time li-item-update-time">{{ani.updateTime}}</div>
              <div class="action">
                <button class="set" @click="setAni(ani.name)">编辑</button>
                <button class="lock" @click="lockAni(ani.name,ani.ban)" >{{ani.ban===1?'锁定':'解锁'}}</button>
                <button class="delete" @click="delAni($event,ani.name)" >删除</button>
              </div>
            </li>
          </ul>
        </div>
        <div class="page">
          <ul class="ul-page">
            <li class="page-item" v-for="(item,index) in pageList" :key="index" >
              <button class="page-btn" @click="pageChange(item.name)" :style="{backgroundColor: index === activePage ? '#e2e0e0' : '' }">{{item.name}}</button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <button class="update-tag" @click="updateTagCount">更新tag计数</button>
  <div class="main-detail" v-if="detail">
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
            <button class="submit" @click="submitt">提交</button>
            <button class="submit" @click="back">返回</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>

import {computed, reactive, ref, toRef, toRefs, watch} from "vue";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import SearchBox from "@/components/common/SearchBox.vue";
const imgDefaultSrc="/src/assets/img/ani_add_error.png";
const imgSrcPreview = ref(imgDefaultSrc);
let detail=ref(false);

let imgExist=false;
const aniList=reactive([]);
const pageList=reactive([])
const aniDTO=reactive({
  name: '',
  image: '',
  videoList:'',
  description:'',
  cvList:'',
  views:'',
  score:'',
  ep:'',
  end: '',
});
const checkbox=computed({
  get() {
    return aniDTO.end == 1 ? true : false;
  },
  set(newValue){
    aniDTO.end=newValue?1:0
  }
})
let totPage=0;
let currentPage=1;
const activePage=ref(1);
let pageSize=4; ////
let searchName;
const axios_instance = axios.create({
  baseURL: `${apiUtils.BASIC}`,
})
function updateTagCount(){
  axios_instance.put('/admin/tag/updatecount',{})
      .then(res=>{
        console.log(res.data);
      })
      .catch(err=>{console.log(err)})
}

function search(name){
  if(name === undefined)
    return;
  currentPage=1;
  searchName=name;
  axios_instance.get(`/admin${apiUtils.SEARCH_PAGE_ANI}`,{
    params:{
      name: name
    }
  })
      .then(res => {
        console.log(res.data.data)
        pageList.length=0;
        totPage=Math.ceil(res.data.data/pageSize)
        pageList.push({name:"上一页"});
        console.log("tot:"+totPage)
        if(totPage<=5){
          let i=1;
          for(i=1;i<=totPage;i++){
            pageList.push({name:""+i});
          }
        }
        else{
          pageList.push({name:"1"});
          pageList.push({name:"2"});
          pageList.push({name:"3"});
          pageList.push({name:"4"});
          pageList.push({name:"5"});
        }
        pageList.push({name:"下一页"});
      })
      .catch(err=>{console.log(err)})
  searchPage(name,currentPage);
}

function searchPage(name,page){
  axios_instance.get(`/admin${apiUtils.SEARCH_ANI}/bynamelike`,{
    params:{
      name: name,
      page: page,
      pageSize: pageSize,
    }
  })
      .then(res => {
        console.log(res.data)
        aniList.splice(0, aniList.length, ...res.data.data);
        console.log(aniList)
      })
      .catch(err=>{console.log(err)})
}

function pageChange(name){
  const tp=currentPage;
  if(name==="下一页"){
    if(currentPage!=totPage){
      currentPage++;
      if(currentPage%5==1&&currentPage!=1){
        pageList.length=1;
        let pagetp=currentPage;
        for(let i=0;i<5&&pagetp<=totPage;i++,pagetp++){
          pageList.push({name:""+pagetp});
        }
        pageList.push({name:"下一页"});
      }
    }
  }else if(name==="上一页"){
    if(currentPage>1){
      if(currentPage%5==1){
        pageList.length=1;
        let pagetp=currentPage-5;
        for(let i=0;i<5;i++,pagetp++){
          pageList.push({name:""+pagetp});
        }
        pageList.push({name:"下一页"});
      }
      currentPage--;
    }
  }else{
    currentPage=Number(name);
    console.log(currentPage);
  }
  if(currentPage!=tp){
    searchPage(searchName,currentPage);
    if(currentPage%5!=0){
      activePage.value=currentPage%5;
    }else{
      activePage.value=5;
    }
  }
}



function setAni(name){
  console.log("set",name)
  detail.value=true;
  Object.assign(aniDTO,aniList.find(ani=> ani.name === name));
  // axios_instance.get(`/admin${apiUtils.SEARCH_ANI}/byname`,{
  //   params:{
  //     name: name,
  //   }
  // })
  //     .then(res => {
  //       console.log(res.data);
  //       Object.assign(aniDTO,res.data.data);
  //     })
  //     .catch(err=>{console.log(err)});
}

function back(){
  detail.value=false;
}

function lockAni(name,ban){
  const ani_tp={name:name,ban:ban^1}
  console.log("lock",ani_tp)
  axios_instance.put(`${apiUtils.UPDATE_ANI}`,ani_tp)
      .then(res=>{
        console.log(res.data)
        const targetAni = aniList.find(ani => ani.name === name);
        if (targetAni) {
          targetAni.ban = ban^1; // 切换锁定状态
        }
      })
      .catch(err=>{console.log(err)})
}

function delAni(event,name){

  console.log("del",name)
  const reconf=confirm("确定要删除《"+name+"》吗？");
  if(!reconf){
    return ;
  }
  const liElement = event.currentTarget.closest('li');
  if (liElement) {
    liElement.style.display = 'none'; // 将该 li 设置为隐藏
  }
  axios_instance.put(`${apiUtils.DEL_ANI}`+"/"+name,name)
      .then(res=>{
        console.log(res.data)

      })
      .catch(err=>{console.log(err)})
}

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
  axios_instance.put(`${apiUtils.UPDATE_ANI}`,aniDTO)
      .then(res => {
        if(res.data.code==1){
          alert("修改成功")
          Object.assign(aniList.find(ani=> aniDTO.id === ani.id),aniDTO);
        }
        else{alert("修改失败");}})
      .catch(error=>{console.log(error);alert("修改失败")});
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
  padding: 1.5rem 2rem;
}

/* table*/
.table{
  width: 100%;
  border: var(--table-border) solid 1px ;
}

.search{
  display: flex;
  padding: 0.5rem 0;
  justify-content: center;
  align-items: center;
  border-bottom: var(--table-border) 1px solid;
}

.search-box{
  border: var(--table-border) solid 1px;
  border-radius: 3rem;
}

.title{
  display: flex;
  border-bottom: var(--table-border) solid 1px;
}
.name,.action,.update-time{
  height: 2rem;
  line-height: 2rem;
  text-align: center;
  font-size: 1.2rem;
  border-right: var(--table-border) solid 1px;
}

.li-item-update-time{
  font-size: 0.7rem;
}

.list-item{
  display: flex;
  border-right: var(--table-border) solid 1px;
  border-bottom: var(--table-border) solid 1px;
}

.name{flex:5;}
.update-time{flex:2;}
.action{flex:3;border-right: none;}

.list-item .action{
  display: flex;
  align-items: center;
  justify-content: center;
}

.action button{
  padding: 0 0.5rem;
  margin-right:0.6rem;
}

.page{
  width: 100%;
  display: flex;
  padding: 0.5rem 0;
  justify-content: center;
}

.ul-page{
  display: flex;

}

.page-item{
  min-width: 1.5rem;
  margin-right: 0.4rem;
  font-size: 0.75rem;
  text-align: center;


}

.page-btn{
  width: 100%;
  height: 100%;
  padding: 0.2rem;
  border: var(--table-border) solid 1px;
  border-radius: 0.2rem;
  cursor: pointer;
}

.page-btn:hover{
  background-color: #e6e3e3;
}

.page-item:nth-child(1),.page-item:nth-last-child(2){
  margin-right: 0.8rem;
}

/* detil*/
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

.submit{
  width: 4rem;
  font-size: 1.1rem;
  margin-right: 2rem;
}
</style>