package com.momoyouta.web_ani_common.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code;//1成功
    private String msg;
    private T data;

    public static <T>Result<T> success(){
        Result<T> result=new Result<T>();
        result.code=1;
        return result;
    }

    public static <T>Result<T> success(T object){
        Result<T> result=new Result<T>();
        result.data=object;
        result.code=1;
        return result;
    }

    public static <T>Result<T> error(int code,String msg){
        Result<T> result=new Result<T>();
        result.code=code;
        result.msg=msg;
        return result;
    }
}
