package com.sunc.car.lovecar.yun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/12/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiXiaokaModel<T> extends BaseBean {
    private boolean success;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
