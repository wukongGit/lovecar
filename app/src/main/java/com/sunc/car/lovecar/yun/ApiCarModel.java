package com.sunc.car.lovecar.yun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiCarModel extends BaseBean {
    private String id;
    private String name;
    private String initial;
    private String parentid;
    private String logo;
    private String depth;
    private List<ApiCarModel> carlist;
    private List<ApiCarModel> list;
    private String price;
    private String yeartype;
    private String productionstate;
    private String salestate;
    private String sizetype;
    private Map<String, String> basic;
    private Map<String, String> body;
    private Map<String, String> engine;
    private Map<String, String> gearbox;

    public Map<String, String> getBasic() {
        return basic;
    }

    public void setBasic(Map<String, String> basic) {
        this.basic = basic;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getEngine() {
        return engine;
    }

    public void setEngine(Map<String, String> engine) {
        this.engine = engine;
    }

    public Map<String, String> getGearbox() {
        return gearbox;
    }

    public void setGearbox(Map<String, String> gearbox) {
        this.gearbox = gearbox;
    }

    public String getPriceDescription() {
        return "价格：" + price;
    }

    public String getYearTypeDescription() {
        return "年份：" + yeartype;
    }

    public List<ApiCarModel> getCarlist() {
        return carlist;
    }

    public void setCarlist(List<ApiCarModel> carlist) {
        this.carlist = carlist;
    }

    public List<ApiCarModel> getList() {
        return list;
    }

    public void setList(List<ApiCarModel> list) {
        this.list = list;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYeartype() {
        return yeartype;
    }

    public void setYeartype(String yeartype) {
        this.yeartype = yeartype;
    }

    public String getProductionstate() {
        return productionstate;
    }

    public void setProductionstate(String productionstate) {
        this.productionstate = productionstate;
    }

    public String getSalestate() {
        return salestate;
    }

    public void setSalestate(String salestate) {
        this.salestate = salestate;
    }

    public String getSizetype() {
        return sizetype;
    }

    public void setSizetype(String sizetype) {
        this.sizetype = sizetype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

}
