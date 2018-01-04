package com.sunc.car.lovecar.yun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/12/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBaseModel<T> extends BaseBean {
    private String status;
    private String msg;
    private T result;

    public boolean isSuccess() {
        return "0".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
