import {defineStore} from "pinia";

export const useCommonStore =defineStore('commonStore',{
    state:()=>({
        seasonAnime: Array.from({length:7},()=>new Array)//{id,name}
    }),
})