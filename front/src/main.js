import { createApp } from 'vue'
import './style.css'
import './assets/styles/base.css'
import './assets/styles/color.css'
import '@/assets/icon/fonticon/iconfont.css'
import '@/assets/icon/fonticon/iconfont.js'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import {router} from "./route/config.js";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {createPinia} from "pinia";
const app=createApp(App);
const pinia=createPinia();
app.use(pinia);
app.use(router);
app.use(ElementPlus);
app.mount('#app');


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}