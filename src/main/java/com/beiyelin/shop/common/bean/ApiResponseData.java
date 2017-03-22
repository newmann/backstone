package com.beiyelin.shop.common.bean;

import com.alibaba.fastjson.JSON;
import com.beiyelin.shop.common.config.ResultCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by newmann on 2017/3/22.
 */
public class ApiResponseData {
    private boolean result;
    private int resultCode;
    private String message;
    private Map<String,Object> data;

    public ApiResponseData(){
        result = false;
        resultCode = ResultCode.Failure;
        message = "";
        data  = new HashMap<String, Object>();
    }

    public void pushData(String label,Object obj){
        this.data.put(label,obj);
    }

    public void setErrorMessage(String msg){
        this.result =false;
        this.resultCode = ResultCode.Failure;
        this.message = msg;
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
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
