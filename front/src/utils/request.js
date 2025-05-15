import axios from "axios";
import {apiUtils} from "@/common/apiUtils.js";
import {router} from "@/route/config.js";
import {resultPopMsg} from "@/utils/resultPopMsg.js";
import {responseCode} from "@/utils/responseCode.js";

const service=axios.create({
    baseURL:apiUtils.BASIC,
    timeout:5000,
});
service.interceptors.request.use(config => {
    //添加jwt
    const accessToken=localStorage.getItem("accessToken");
    config.headers['token'] = accessToken;
    return config;
}, function (error) {
    return Promise.reject(error);
});

async function request(originConfig) {
    //header是否存在
    originConfig.headers= originConfig.headers||{};
    try{
        const response = await service(originConfig)
        const {data} = response;
        if(data.code===responseCode.TOKEN_EXPIRED){// 认证过期处理
            handleTokenFailure();
            router.push("/login");
            resultPopMsg(0,data.msg);
            return;
        }
        if(data.code===responseCode.TOKEN_INVALID){//无权限访问
            resultPopMsg(0,data.msg);
            return;
        }
        return response;
    }catch(error){
        return Promise.reject(error);
    }
}
function handleTokenFailure(){
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("roles");
    localStorage.removeItem("uesrInfo");
}
export default request;