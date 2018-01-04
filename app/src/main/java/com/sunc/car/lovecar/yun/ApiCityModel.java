package com.sunc.car.lovecar.yun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiCityModel extends BaseBean {
    private String city;
    private String cityname;
    private String date;
    private String week;
    private List<String> time;
    private String area;
    private String summary;
    private String numberrule;
    private String number;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTimeDes() {
        if (time == null) {
            return week;
        }
        if (time.size() == 1) {
            return week + " " + time.get(0);
        }
        return week + " " + time.get(0) + "-" + time.get(1);
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public String getAreaDes() {
        return "限行区域：\n" + area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNumberrule() {
        return numberrule;
    }

    public String getNumberruleDes() {
        return "限行规则：\n" + numberrule;
    }


    public void setNumberrule(String numberrule) {
        this.numberrule = numberrule;
    }

    public String getNumber() {
        return number;
    }

    public String getNumberDes() {
        return "限行号码：\n" + numberrule;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
