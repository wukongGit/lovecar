package com.sunc.car.lovecar.yun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/12/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDriverLicenseModel extends BaseBean {
    private String licensenumber;
    private String licenseid;
    private String score;

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public String getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
