package com.beiyelin.shop.common.bean;

import com.alibaba.fastjson.JSON;
import com.beiyelin.shop.common.config.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by newmann on 2017/3/22.
 */
@ApiModel
@Data
public class ApiResponseData {
    @ApiModelProperty(value = "是否正确：true表示正确返回，false表示出现错误。", example = "")
    private boolean result;
    @ApiModelProperty(value = "状态码：返回不同的状态码，0表示错误，1表示正确，还有其他的定义", example = "")
    private int resultCode;
    @ApiModelProperty(value = "信息：返回的信息", example = "")
    private String message;
    @ApiModelProperty(value = "信息：返回的数据", example = "")
    private Map<String,Object> data;

    /**
     * 重置所有信息
     */
    public void reset(){
        result = false;
        resultCode = ResultCode.Failure;
        message = "";
        data  = new HashMap<String, Object>();

    }
    public ApiResponseData(){
        this.reset();
    }

    public void pushData(String label,Object obj){
        this.data.put(label,obj);
    }

    public void setErrorMessage(String msg){
        this.result =false;
        this.resultCode = ResultCode.Failure;
        this.message = msg;
    }

    public void setInvalidApp(){
        this.result =false;
        this.resultCode = ResultCode.Failure;
        this.message = "亲~您还没获得授权，请更新APP后访问";
    }


    public void setSuccessMessage(String msg){
        this.result =true;
        this.resultCode = ResultCode.Success;
        this.message = msg;
    }

    public String toString(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", result);
        map.put("resultCode", resultCode);
        map.put("message", message);
        map.put("data", data);
        return JSON.toJSONString(map);

    }
//    public boolean isResult() {
//        return result;
//    }
//
//    public void setResult(boolean result) {
//        this.result = result;
//    }
//
//    public int getResultCode() {
//        return resultCode;
//    }
//
//    public void setResultCode(int resultCode) {
//        this.resultCode = resultCode;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Map<String, Object> getData() {
//        return data;
//    }
//
//    public void setData(Map<String, Object> data) {
//        this.data = data;
//    }
}
