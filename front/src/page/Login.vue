<template>
<el-container style="height: 100%">
  <el-main style="text-align: center;padding-top: 100px">
    <div v-if="activeMenu===1">
      <el-card class="card" body-style="height: 100%;">
        <h1 style="font-size: 32px;margin-bottom: 16px">用户登录</h1>
        <el-form :model="user" label-position="right" style="width: 270px;margin: auto"
                 :rules="loginRule"
                 size="large"
                 ref="loginForm">
          <el-form-item prop="account" class="formItem">
            <el-input  v-model="user.account" placeholder="请输入长度为8-13的账号"></el-input>
          </el-form-item>
          <el-form-item prop="password" class="formItem" >
            <el-input show-password v-model="user.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item class="formItem">
            <el-button @click="handleLogin" size="large" style="width: 100%" type="primary">登录</el-button>
          </el-form-item>
          <el-form-item class="formItem">
            <div style="display: flex;justify-content: space-between;width: 100%">
              <el-link  :underline="false" @click="activeMenu=3">忘记密码</el-link>
              <el-link :underline="false" @click="activeMenu=2">注册账号</el-link>
            </div>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div v-if="activeMenu===2">
      <el-card class="card" body-style="width: 380px">
        <h1 style="font-size: 32px;margin-bottom: 16px">成为MOMO</h1>
        <el-form :model="user" label-position="right" style="width: 270px;margin: auto"
                 :rules="registerRule"
                 size="large"
                 ref="registerForm">
          <el-form-item prop="name" class="formItem">
            <el-input  v-model="user.name" placeholder="请输入昵称"></el-input>
          </el-form-item>
          <el-form-item prop="account" class="formItem">
            <el-input  v-model="user.account" placeholder="请输入长度为8-13的账号"></el-input>
          </el-form-item>
          <el-form-item prop="password" class="formItem" >
            <el-input show-password v-model="user.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item prop="passwordConfirmation" class="formItem">
            <el-input show-password v-model="user.passwordConfirmation" placeholder="请再次输入密码"></el-input>
          </el-form-item>
          <el-form-item class="formItem">
            <el-button @click="handleRegister" size="large" style="width: 100%" type="primary">注册账号</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div v-if="activeMenu===3">
      <el-card class="card" body-style="width: 380px">
        <h1 style="font-size: 32px;margin-bottom: 16px">找回密码</h1>
        <el-form :model="user" label-position="right" style="width: 270px;margin: auto"
                 :rules="registerRule"
                 size="large"
                 ref="forgetPassWordForm">
          <el-form-item prop="name" class="formItem">
            <el-input  v-model="user.name" placeholder="请输入昵称"></el-input>
          </el-form-item>
          <el-form-item prop="account" class="formItem">
            <el-input  v-model="user.account" placeholder="请输入长度为8-13的账号"></el-input>
          </el-form-item>
          <el-form-item prop="password" class="formItem" >
            <el-input show-password v-model="user.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item prop="passwordConfirmation" class="formItem">
            <el-input show-password v-model="user.passwordConfirmation" placeholder="请再次输入密码"></el-input>
          </el-form-item>
          <el-form-item class="formItem">
            <el-button @click="handleRegister" size="large" style="width: 100%" type="primary">注册账号</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </el-main>
</el-container>
</template>

<script setup>

import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import axios from "axios";
import {AXIOS_URL} from "@/common/axios_url.js";
import {resultPopMsg} from "@/hooks/resultPopMsg.js";
const axs=axios.create({
  baseURL:AXIOS_URL.baseURL,
})
const activeMenu=ref(1);
const user=reactive({
  name: null,
  account: null,
  password: null,
  passwordConfirmation: null,
})
const loginRule = reactive({
  account:[
    {required:true,message:'账号不能为空',trigger:'blur'},
    {min:8,max:13,message:'账号长度需在8到13之间',trigger:'blur'}
  ],
  password:[
    {required:true,message:'密码不能为空',trigger:'blur'},
    {min:6,message:'密码长度不得小于6',trigger:'blur'}
  ],
})
const loginForm=ref()
async function handleLogin(){
  const valid=await new Promise((resolve)=>{
    loginForm.value.validate((valid)=>{
      resolve(valid)
    })
  });
  if(!valid){
    ElMessage({
      type:"error",
      message:'表单填写不符合规则',
    });
  }else{
    await axs.post("/login",{
      account:user.account,
      password:user.password,
    })
        .then(res=>{
          if(res.data.code===1){
            resultPopMsg(1,'登录成功');
          }else{
            resultPopMsg(0,'登录失败',res.data.code,res.data.message);
          }
        })
        .catch(err=>{
          resultPopMsg(0,err);
        })

  }
}
////////////////////////////////////////////register
const registerRule = reactive({
  name:[
    {required:true,message:'昵称不能为空',trigger:'blur'},
    {max:12,message:'昵称长度需小于12',trigger:'blur'}
  ],
  account:[
    {required:true,message:'账号不能为空',trigger:'blur'},
    {min:8,max:13,message:'账号长度需在8到13之间',trigger:'blur'}
  ],
  password:[
    {required:true,message:'密码不能为空',trigger:'blur'},
    {min:6,message:'密码长度不得小于6',trigger:'blur'}
  ],
  passwordConfirmation:[
    {
      validator:
          (rule, value, callback) => {
        if(value==='') {
          callback(new Error('请再次输入密码'));
        }else if(value!==user.password){
          callback(new Error('两次密码不一致！'));
        }else {
          callback();
        }},
      trigger:'blur'},
  ],
})
const registerForm=ref(null);
async function handleRegister(){
  const valid=await new Promise((resolve)=>{
    registerForm.value.validate((valid)=>{
      resolve(valid)
    })
  });
  if(!valid){
    ElMessage({
      type:"error",
      message:'表单填写不符合规则',
    });
  }else{
    await axs.post("/user/register",{
      name:user.name,
      account:user.account,
      password:user.password,
    })
        .then(res=>{
          if(res.data.code===1){
            resultPopMsg(1,'注册成功');
          }else{
            resultPopMsg(0,'注册失败',res.data.code,res.data.message);
          }
        })
        .catch(err=>{
          resultPopMsg(0,err);
        })

  }
}
/////////////////////////////////////////

</script>

<style scoped>
.card{
  width: fit-content;
  margin: 0 auto;
}
.formItem{
  margin: 20px;
}
</style>