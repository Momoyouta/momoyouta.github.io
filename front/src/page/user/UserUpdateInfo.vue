<template>
  <div style="min-height: 95vh" class="card">
    <el-dialog  v-model="avatarDialogVisible" title="上传头像链接">
      <el-input v-model="uploadAvatarUrl" clearable></el-input>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="avatarDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="uploadAvatar">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
    <div class="avatar" style="display:flex;align-items:flex-end;margin-bottom: 20px">
      <div class="imgBox">
        <el-image :src="userInfo.avatarUrl" fit="contain" style="width: 200px;aspect-ratio: 1;border-radius: 4px" ></el-image>
        <div style="line-height: 16px;font-size: 16px">200px</div>
      </div>
      <div class="imgBox">
        <el-image :src="userInfo.avatarUrl" fit="contain" style="width: 100px;aspect-ratio: 1;border-radius: 4px" ></el-image>
        <div style="line-height: 16px;font-size: 16px">100px</div>
      </div>
      <div class="imgBox">
        <el-image :src="userInfo.avatarUrl" fit="contain" style="width: 64px;aspect-ratio: 1;border-radius: 4px" ></el-image>
        <div style="line-height: 16px;font-size: 16px">64px</div>
      </div>
      <div class="imgBox" style="margin-right: 100px">
        <el-image :src="userInfo.avatarUrl" fit="contain" style="width: 40px;aspect-ratio: 1;border-radius: 4px" ></el-image>
        <div style="line-height: 16px;font-size: 16px">40px</div>
      </div>
      <div style="display: flex;align-items:center;">
        <div style="width: 200px;margin-bottom: 30px">
          <el-button type="primary" style="width: 100%" @click="handleUploadAvatarUrl">上传头像</el-button>
          <p style="font-size: 12px">请上传一个图片链接。这意味着您需要先将图片上传到某一个图仓上。</p>
        </div>
      </div>
    </div>
    <div class="form">
      <el-form style="max-width:1200px;margin-bottom: 20px" :model="userInfo"
               label-position="top"
               inline
               :rules="formRule">
        <el-form-item style="min-width: 560px" prop="name" label="姓名">
          <el-input v-model="userInfo.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item style="min-width: 560px" prop="email" label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item style="min-width: 560px" prop="birthday" label="生日">
          <el-date-picker style="min-width: 560px" v-model="userInfo.birthday" type="date"/>
        </el-form-item>
        <el-form-item style="min-width: 560px" prop="sex" label="性别">
          <el-radio-group v-model="userInfo.sex">
            <el-radio :value="0" label="女"/>
            <el-radio :value="1" label="男"/>
          </el-radio-group>
        </el-form-item>
        <el-form-item style="min-width: 760px;" prop="signature" label="个人简介">
          <el-input class="signature" type="textarea" v-model="userInfo.signature" placeholder="这个人很懒什么也没有留下~"></el-input>
        </el-form-item>
      </el-form>
      <div style="width: 100%;">
        <el-button type="primary" style="margin-left: 10%;width: 30%" @click="updateUserInfo">更新个人信息</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>

import {onMounted, reactive, ref} from "vue";
import request from "@/utils/request.js";
import {apiUtils} from "@/common/apiUtils.js";
import {resultPopMsg} from "@/utils/resultPopMsg.js";
import {router} from "@/route/config.js";

const userInfo=ref({
  name: null,
  email: null,
  birthday: null,
  sex: 0,
  avatarUrl: null,
  signature: null,
})
const formRule=reactive({
})
const avatarDialogVisible = ref(false);
onMounted(()=>{
  loadUserInfo();
});

function loadUserInfo(){
  const info=JSON.parse(localStorage.getItem('userInfo'));
  userInfo.value={...info};
}
async function updateUserInfo(){
  await request({
    method:"POST",
    url:apiUtils.user.updateUserInfo,
    data:{
      ...userInfo.value,
      birthday:userInfo.value.birthday.getTime(),
    },
  })
  .then(res=>{
    if(res.data.code===1){
      localStorage.setItem('userInfo',JSON.stringify(userInfo.value));
      resultPopMsg(1,'修改成功');
    }else{
      resultPopMsg(0,'修改失败',res.data.code,res.data.msg);
    }
  }).catch(err=>{
    resultPopMsg(0,err);
  })
}

const uploadAvatarUrl=ref(null);
function handleUploadAvatarUrl(){
  uploadAvatarUrl.value=null;
  avatarDialogVisible.value = true;
}
function uploadAvatar(){
  userInfo.value.avatarUrl=uploadAvatarUrl.value;
  avatarDialogVisible.value = false;
}
</script>

<style scoped>
.imgBox{
  aspect-ratio: 1;
  margin-right: 30px;

}
.card{
  background-color:var(--side-bg);
  border-radius: 10px;
  padding:20px;
}
.signature:deep(.el-textarea__inner){
  min-height: 120px !important;
}
:deep(.el-dialog){
  background-color: var(--side-bg) !important;
}
</style>