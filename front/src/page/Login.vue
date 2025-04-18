<template>
<el-container style="height: 100%">
  <el-main style="text-align: center;padding-top: 70px">
    <h1 style="font-size: 20px;margin-bottom: 20px">Momoyouta欢迎你</h1>
    <el-tabs ref="loginTabs" style="width: 450px;margin: 0 auto;"
             v-model="activeCard"
             @tab-click="handleTabChange">
      <el-tab-pane name="login" label="登录" >
        <el-card class="card">
          <el-form :model="user" label-position="right" style="margin-right:40px"
                   :rules="loginRule"
                   size="large"
                   label-width="80px"
                   ref="loginForm">
            <el-form-item prop="account" class="formItem" label="账号">
              <el-input  v-model="user.account" placeholder="请输入长度为8-13的账号"></el-input>
            </el-form-item>
            <el-form-item prop="password" class="formItem" label="密码">
              <el-input show-password v-model="user.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item class="formItem">
              <el-button @click="handleLogin" size="large" style="width: 100%;" type="primary">登录</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      <el-tab-pane name="register" label="注册" >
        <el-card class="card">
          <el-form :model="user" label-position="right" style="margin-right:40px"
                   :rules="registerRule"
                   size="large"
                   label-width="80px"
                   ref="registerForm">
            <el-form-item prop="name" class="formItem" label="昵称">
              <el-input  v-model="user.name" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item prop="account" class="formItem" label="账号">
              <el-input  v-model="user.account" placeholder="请输入长度为8-13的账号"></el-input>
            </el-form-item>
            <el-form-item prop="password" class="formItem" label="密码">
              <el-input show-password v-model="user.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item prop="passwordConfirmation" class="formItem" label="确认密码">
              <el-input show-password v-model="user.passwordConfirmation" placeholder="请再次输入密码"></el-input>
            </el-form-item>
            <el-form-item class="formItem">
              <el-button @click="handleRegister" size="large" style="width: 100%" type="primary">注册账号</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      <el-tab-pane name="forgotPassword" label="找回密码" >
        <el-card class="card" >
          <el-form :model="user" label-position="right" style="margin-right:40px"
                   :rules="forgotPasswordRule"
                   size="large"
                   label-width="80px"
                   ref="forgotPasswordForm">
            <el-form-item prop="account" class="formItem" label="账号">
              <el-input  v-model="user.account" placeholder="请输入账号"></el-input>
            </el-form-item>
            <el-form-item prop="email" class="formItem" label="邮箱">
              <el-input  v-model="user.email" placeholder="请输入账号绑定的邮箱"></el-input>
            </el-form-item>
            <el-form-item prop="password" class="formItem" label="新密码">
              <el-input show-password v-model="user.password" placeholder="请输入新密码"></el-input>
            </el-form-item>
            <el-form-item prop="passwordConfirmation" class="formItem" label="确认密码">
              <el-input show-password v-model="user.passwordConfirmation" placeholder="请再次输入新密码"></el-input>
            </el-form-item>
            <el-form-item prop="captcha" class="formItem" label="验证码">
              <el-input v-model="user.captcha" placeholder="请输入6位验证码">
                <template #append>
                  <el-button @click="sendCaptcha" v-if="!hadSendCaptcha">发送验证码</el-button>
                  <el-button disabled v-else>{{timeout}}s后重新发送</el-button>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item class="formItem">
              <el-button @click="handleForgotPassword" size="large" style="width: 100%" type="primary">找回密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </el-main>
</el-container>
</template>

<script setup>

import {reactive, ref, watch} from "vue";
import {ElMessage} from "element-plus";
import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import {resultPopMsg} from "@/hooks/resultPopMsg.js";
import {router} from "@/route/config.js";
const axs=axios.create({
  baseURL:apiUtils.BASIC,
})
const user=reactive({
  name: null,
  account: null,
  password: null,
  passwordConfirmation: null,
  email: null,
  captcha: null
})
const activeCard=ref('login');
const loginTabs=ref();
function handleTabChange(){
  for(const key in user){
    user[key]=null;
  }
}

const loginRule = reactive({
  account:[
    {required:true,message:'账号不能为空',trigger:'blur'},
    {min:8,max:13,message:'账号长度需在8到13之间',trigger:'blur'},
    {pattern:/^[0-9]+$/,message: '账号必须为数字',trigger:'blur'},
  ],
  password:[
    {required:true,message:'密码不能为空',trigger:'blur'},
    {min:6,message:'密码长度不得小于6',trigger:'blur'},
    {pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+$/, message: '不允许输入空格等特殊符号',trigger:'blur'},
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
    await axs.get(apiUtils.login,{
      params:{
        account:user.account,
        password:user.password,
      }
    })
        .then(res=>{
          if(res.data.code===1){
            resultPopMsg(1,'登录成功');
            const data=res.data.data;
            localStorage.setItem('accessToken',data.accessToken);
            localStorage.setItem('refreshToken',data.refreshToken);
            localStorage.setItem('userInfo',JSON.stringify(data.userBaseInfo));
            localStorage.setItem('roles',data.roles);
            router.push('/home');
          }else{
            resultPopMsg(0,'登录失败',res.data.code,res.data.msg);
          }
        })
        .catch(err=>{
          resultPopMsg(0,err);
        })
  }
}

const registerRule = reactive({
  name: [
    {required: true, message: '昵称不能为空', trigger: 'blur'},
    {max: 12, message: '昵称长度需小于12', trigger: 'blur'},
  ],
  account: [
    {required: true, message: '账号不能为空', trigger: 'blur'},
    {min: 8, max: 13, message: '账号长度需在8到13之间', trigger: 'blur'},
    {pattern:/^[0-9]+$/,message: '账号必须为数字',trigger:'blur'},

  ],
  password: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 6, message: '密码长度不得小于6', trigger: 'blur'},
    {pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+$/, message: '不允许输入空格等特殊符号',trigger:'blur'},
  ],
  passwordConfirmation: [
    {
      validator:
          (rule, value, callback) => {
            if (value === '') {
              callback(new Error('请再次输入密码'));
            } else if (value !== user.password) {
              callback(new Error('两次密码不一致！'));
            } else {
              callback();
            }
          },
      trigger: 'blur'
    },
  ],
})
const registerForm = ref(null);
async function handleRegister() {
  const valid = await new Promise((resolve) => {
    registerForm.value.validate((valid) => {
      resolve(valid)
    })
  });
  if (!valid) {
    ElMessage({
      type: "error",
      message: '表单填写不符合规则',
    });
  } else {
    await axs.post("/user/register", {
      name: user.name,
      account: user.account,
      password: user.password,
    })
        .then(res => {
          if (res.data.code === 1) {
            resultPopMsg(1, '注册成功');
            activeCard.value='login'
          } else {
            resultPopMsg(0, '注册失败', res.data.code, res.data.msg);
          }
        })
        .catch(err => {
          resultPopMsg(0, err);
        })

  }
}

const forgotPasswordRule=reactive({
  name: [
    {required: true, message: '昵称不能为空', trigger: 'blur'},
    {max: 12, message: '昵称长度需小于12', trigger: 'blur'}
  ],
  email: [
    {required:true,message:'邮箱不能为空',trigger:'blur'},
    {pattern:/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,message: '邮箱格式不正确',trigger:'blur'},
  ],
  account: [
    {required: true, message: '账号不能为空', trigger: 'blur'},
    {min: 8, max: 13, message: '账号长度需在8到13之间', trigger: 'blur'},
    {pattern:/^[0-9]+$/,message: '账号必须为数字',trigger:'blur'},
  ],
  password: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 6, message: '密码长度不得小于6', trigger: 'blur'},
    {pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+$/, message: '不允许输入空格等特殊符号',trigger:'blur'},
  ],
  passwordConfirmation: [
    {
      validator:
          (rule, value, callback) => {
            if (value === '') {
              callback(new Error('请再次输入密码'));
            } else if (value !== user.password) {
              callback(new Error('两次密码不一致！'));
            } else {
              callback();
            }
          },
      trigger: 'blur'
    },
  ],
  captcha:[
    {required: true, message: '验证码不能为空', trigger: 'blur'},
    {max: 6,min:6, message: '验证码长度应为6', trigger: 'blur'},
  ]
});
const forgotPasswordForm=ref();
const hadSendCaptcha=ref(false)
const timeout=ref(60)
let timer=null;
async function handleForgotPassword(){
  const valid=await new Promise((resolve)=>{
    forgotPasswordForm.value.validate((valid)=>{
      resolve(valid)
    })
  });
  if(!valid){
    ElMessage({
      type:"error",
      message:'表单填写不符合规则',
    });
  }else{
    await axs.post(apiUtils.forgotPassword,{
      account:user.account,
      password:user.password,
    })
        .then(res=>{
          if(res.data.code===1){
            resultPopMsg(1,'重置密码成功');
            router.push('/login');
          }else{
            resultPopMsg(0,'找回失败',res.data.code,res.data.msg);
          }
        })
        .catch(err=>{
          resultPopMsg(0,err);
        })
  }
}
async function sendCaptcha(){
  let check=false
  forgotPasswordForm.value.validateField('email',(valid)=>{
    check=valid;
  })
  if(!check){return;}
  await axs.get(apiUtils.getCaptcha,{
    params:{
      email:user.email
    }
  }).then((res)=>{
    if(res.data.code===1){
      resultPopMsg(res.data.code,res.data.message);
      hadSendCaptcha.value=true;
      timer=setInterval(()=>{
            timeout.value--;
          }
          ,1000);
    }else{
      resultPopMsg(0,'验证码发送失败',res.data.code,res.data.msg);
    }
  }).catch(err=>{
    resultPopMsg(0,err);
  })
}
watch(timeout,(time)=>{
  if(time===0){
    hadSendCaptcha.value=false;
    clearInterval(timer);
    timeout.value=60;
  }
})

</script>

<style scoped>
.card{
  width: 450px;
  margin: 0 auto;
}
.formItem{
  margin-bottom: 30px;
}
</style>