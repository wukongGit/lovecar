package com.sunc.car.lovecar.login;

import com.sunc.car.lovecar.yun.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class Guide extends BaseBean {
    private String title;
    private List<String> contents;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> content) {
        this.contents = content;
    }
}
