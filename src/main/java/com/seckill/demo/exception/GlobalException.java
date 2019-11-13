package com.seckill.demo.exception;

import com.seckill.demo.Result.CodeMsg;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

}
