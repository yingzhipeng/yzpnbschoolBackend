package com.yzpnb.service_base_handler;

import com.yzpnb.common_utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice//全局异常处理、全局数据绑定、全局数据预处理
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)//表示此方法对所有异常都有效
    @ResponseBody//将对象封装成json数据返回
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error().message("请求失败，请重试！！！");
    }

    /**处理自定义异常*/
    @ExceptionHandler(CustomExceptionHandler.class)//表示此方法只对我们自己定义的这个异常有效
    @ResponseBody
    public Result customError(CustomExceptionHandler e){//参数就是我们自定义异常类的对象
        e.printStackTrace();//父类提供的方法
        return Result.error().code(e.getCode()).message(e.getMessage());//将统一返回结果的状态码和提示信息，换为自定以异常的
    }
}
