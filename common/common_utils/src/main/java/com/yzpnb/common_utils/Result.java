package com.yzpnb.common_utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    /**
     * 当我们返回json字符串的时候，可以按以下规则
     * {
     * 	"sucess":布尔值, 	//表示是否响应成功
     * 	"code":数字,		//响应码
     * 	"message":字符串,	//返回信息
     * 	"data":HashMap,		//返回的数据
     * }
     *
     * 创建成员变量
     */

    @ApiModelProperty(value="是否成功")
    private Boolean success;

    @ApiModelProperty(value="状态码")
    private Integer code;

    @ApiModelProperty(value="返回消息")
    private String message;

    @ApiModelProperty(value="返回数据")
    private Map<String,Object> data=new HashMap<>();

    /**私有化构造方法:就是说除了Result类本身，其它类不能new Result类的对象了**/
    private Result(){}

    /**单例设计模式，获取一个私有的Result对象。Result本身是可以new 对象，无论私有与否**/
    private static Result result=new Result();

    /**处理响应成功的静态方法**/
    public static Result ok(){
        result.setSuccess(true);            //设置为true，表示处理响应成功
        result.setCode(ResultCode.SUCCESS); //调用我们提供状态码的接口，设置状态码
        result.setMessage("成功");          //设置提示信息
        return result;                      //返回对象，外键不能new，只能通过静态方法获取对象

    }
    /**处理响应失败的静态方法**/
    public static Result error(){
        result.setSuccess(false);         //设置为false，表示处理响应失败
        result.setCode(ResultCode.ERROR); //调用我们提供状态码的接口，设置状态码
        result.setMessage("失败");        //设置提示信息
        return result;
    }

    /**设置值方法*/
    public Result succes(Boolean b){
        this.success=b;
        return this;
    }

    public Result code(Integer i){
        this.code=i;
        return this;
    }

    public Result message(String str){
        this.message=str;
        return this;
    }

    public Result data(String key ,Object value){
        this.data.clear();//先清空，因为我这里是单例设计模式，data中数据永远都在
        this.data.put(key,value);
        return this;
    }

    public Result data(Map<String , Object> map){
        this.setData(map);
        return this;
    }
}
