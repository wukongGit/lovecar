package com.sunc.car.lovecar.yun;

import com.sunc.utils.SerializeUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/11.
 */

public class BaseBean implements Serializable {
    @Override
    public String toString() {
        return SerializeUtil.getInstance().serialize(this);
    }
}