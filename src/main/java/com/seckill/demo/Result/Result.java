package com.seckill.demo.Result;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result<T>(CodeMsg.SUCCESS.getCode(),CodeMsg.SUCCESS.getMsg(),data);
    }

    public static <T> Result<T> error(CodeMsg msg){
        if(msg == null){
            return null;
        }
        return new Result<>(CodeMsg.SERVER_ERROR.getCode(),CodeMsg.SERVER_ERROR.getMsg(),null);
    }
}
