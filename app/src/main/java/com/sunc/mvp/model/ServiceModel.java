package com.sunc.mvp.model;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sunc.car.lovecar.App;
import com.sunc.car.lovecar.bmob.Order;
import com.sunc.car.lovecar.bmob.User;
import com.sunc.car.lovecar.yun.ApiBaseModel;
import com.sunc.car.lovecar.yun.ApiCarModel;
import com.sunc.car.lovecar.yun.ApiCityModel;
import com.sunc.car.lovecar.yun.ApiDriverLicenseModel;
import com.sunc.car.lovecar.yun.ApiViolationModel;
import com.sunc.car.lovecar.yun.ApiXiaokaModel;
import com.sunc.car.lovecar.yun.ApiYiyuanModel;
import com.sunc.mvp.contract.ServiceContract;
import com.sunc.utils.Constants;
import com.sunc.utils.HttpUtils;
import com.sunc.utils.SerializeUtil;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/12/11.
 */

public class ServiceModel implements ServiceContract.Model {
    //private static final String TAG = "ServiceModel";

    @Inject
    ServiceModel() {}

    @Nullable
    @Override
    public List<ApiCarModel> getCarModel() {
        String host = Constants.INSTANCE.getAPI_CAR_QUERY_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/car/brand";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            ApiBaseModel<List<ApiCarModel>> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<List<ApiCarModel>>>() {
            });
            if (model.isSuccess()) {
                return model.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public List<ApiCarModel> getCarModelContain(String parentid) {
        String host = Constants.INSTANCE.getAPI_CAR_QUERY_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/car/carlist";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("parentid", parentid);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "result==" +result);
            ApiBaseModel<List<ApiCarModel>> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<List<ApiCarModel>>>() {
            });
            if (model.isSuccess()) {
                List<ApiCarModel> returnList = new ArrayList<>();
                List<ApiCarModel> resultList = model.getResult();
                if (resultList != null) {
                    for (ApiCarModel resultItem : resultList) {
                        List<ApiCarModel> carList = resultItem.getCarlist();
                        if (carList != null) {
                            for (ApiCarModel carItem : carList) {
                                String logo = carItem.getLogo();
                                List<ApiCarModel> list = carItem.getList();
                                if (list != null) {
                                    for (ApiCarModel item : list) {
                                        String itemLogo = item.getLogo();
                                        if (itemLogo == null || !itemLogo.startsWith("http")) {
                                            item.setLogo(logo);
                                        }
                                        returnList.add(item);
                                    }
                                }
                            }
                        }
                    }
                }
                return returnList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ApiCarModel> getCarDetail(String carid) {
        String host = Constants.INSTANCE.getAPI_CAR_QUERY_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/car/detail";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("carid", carid);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "result==" +result);
            ApiBaseModel<ApiCarModel> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<ApiCarModel>>() {
            });
            if (model.isSuccess()) {
                List<ApiCarModel> returnList = new ArrayList<>();
                returnList.add(model.getResult());
                return returnList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Override
    public List<ApiCityModel> getCity() {
        String host = Constants.INSTANCE.getAPI_VEHICLE_LIMITE_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/vehiclelimit/city";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getCity==" +result);
            ApiBaseModel<List<ApiCityModel>> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<List<ApiCityModel>>>() {
            });
            if (model.isSuccess()) {
                List<ApiCityModel> returnList = model.getResult();
                return returnList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Override
    public ApiCityModel getCityLimit(@NotNull String city, @NotNull String date) {
        String host = Constants.INSTANCE.getAPI_VEHICLE_LIMITE_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/vehiclelimit/query";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("city", city);
        querys.put("date", date);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getCityLimit==" +result);
            ApiBaseModel<ApiCityModel> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<ApiCityModel>>() {
            });
            if (model.isSuccess()) {
                return model.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Override
    public Map<String, String> getVinInfo(@NotNull String vin) {
        String host = Constants.INSTANCE.getAPI_VIN_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/vin";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("vin", vin);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getVinInfo==" +result);
            ApiYiyuanModel<Map<String, String>> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiYiyuanModel<Map<String, String>>>() {
            });
            if (model.isSuccess()) {
                return model.getShowapi_res_body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Override
    public Map<String, String> getOilPrice(@NotNull String province) {
        String host = Constants.INSTANCE.getAPI_OIL_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/oil/query";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("province", province);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getOilPrice==" +result);
            ApiBaseModel<Map<String, String>> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<Map<String, String>>>() {
            });
            if (model.isSuccess()) {
                return model.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Override
    public List<String> getOilProvince() {
        String host = Constants.INSTANCE.getAPI_OIL_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/oil/province";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getOilProvince==" +result);
            ApiBaseModel<List<String>> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<List<String>>>() {
            });
            if (model.isSuccess()) {
                return model.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ApiViolationModel.ApiViolationProvince> getViolationCity() {
        String host = Constants.INSTANCE.getAPI_VIOLATION_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/violation/condition";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getViolationCity==" +result);
            ApiXiaokaModel<List<ApiViolationModel.ApiViolationProvince>> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiXiaokaModel<List<ApiViolationModel.ApiViolationProvince>>>() {
            });
            if (model.isSuccess()) {
                return model.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ApiViolationModel getViolations(@NotNull String plateNumber, @NotNull String vin, @NotNull String engineNo, @NotNull String city) {
        String host = Constants.INSTANCE.getAPI_VIOLATION_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/violation/query";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        JSONObject object = new JSONObject();
        try {
            object.put("plateNumber", plateNumber);
            object.put("vin", vin);
            object.put("engineNo", engineNo);
            object.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String bodys = "{\"plateNumber\":\"鲁FE5026\"(车牌号，必填),\"vin\":\"167786\"(车架号，视城市规则是否必填),\"engineNo\":\"013166\"(发动机号，视城市规则是否必填),\"carType\":\"02\"(车辆类型01大车02小车,不必填,默认小车),\"city\":\"烟台市\"(查询城市,不必填,默认查归属地)}";
        String bodys = object.toString();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getViolations==" +result);
            ApiXiaokaModel<ApiViolationModel> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiXiaokaModel<ApiViolationModel>>() {
            });
            if (model.isSuccess()) {
                return model.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Override
    public ApiDriverLicenseModel getDriverLicense(@NotNull String licenseid, @NotNull String licensenumber) {
        String host = Constants.INSTANCE.getAPI_LICENCE_HOST();
        String appcode = Constants.INSTANCE.getAPI_APPCODE();
        String path = "/driverlicense/query";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("licenseid", licenseid);
        querys.put("licensenumber", licensenumber);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            //Log.d(TAG, "getDriverLicense==" +result);
            ApiBaseModel<ApiDriverLicenseModel> model = SerializeUtil.getInstance().deserialize(result, new TypeReference<ApiBaseModel<ApiDriverLicenseModel>>() {
            });
            if (model.isSuccess()) {
                return model.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void moneyMinus(final int price, final String product, final PaySuccessListener listener) {
        if (price <= 0) {
            listener.onSuccess();
            return;
        }
        User current = App.instance.getUser();
        if (current == null) {
            listener.onFail(0);
            return;
        }
        int money = current.getMoney();
        if (money < 10) {
            listener.onFail(1);
            return;
        }
        User p = new User();
        p.setMoney(money - price);
        p.update(current.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    if (listener != null) {
                        listener.onSuccess();
                    }
                    addOrder(price, product);
                }
            }
        });
    }

    private void addOrder(int price, String product) {
        Order p = new Order();
        p.setPrice(price);
        p.setProduct(product);
        p.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {

            }
        });
    }

    interface PaySuccessListener {
        void onSuccess();
        void onFail(int code);
    }
}
