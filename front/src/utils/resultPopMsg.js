import {ElMessage} from "element-plus";

export function resultPopMsg(state,msg,code,serverMsg){
    if(state===1){
        ElMessage({
            type:"success",
            message:`${msg}`,
        });
    }else {
        ElMessage({
            type:"error",
            message:`${msg}`,
        });
        if(code!==undefined){
            ElMessage({
                type:"error",
                message:`错误码: ${code},${serverMsg}`,
            });
        }
    }
}