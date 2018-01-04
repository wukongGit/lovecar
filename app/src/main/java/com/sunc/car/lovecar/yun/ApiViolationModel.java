package com.sunc.car.lovecar.yun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiViolationModel extends BaseBean {
    private String token;
    private String totalFine;
    private String totalPoints;
    private String untreated;
    private String amount;
    private List<ApiViolationDetail> violations;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(String totalFine) {
        this.totalFine = totalFine;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getUntreated() {
        return untreated;
    }

    public void setUntreated(String untreated) {
        this.untreated = untreated;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<ApiViolationDetail> getViolations() {
        return violations;
    }

    public void setViolations(List<ApiViolationDetail> violations) {
        this.violations = violations;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ApiViolationDetail extends BaseBean {
        private String code;
        private String time;
        private String fine;
        private String address;
        private String reason;
        private String point;
        private String violationCity;
        private String province;
        private String city;
        private String serviceFee;
        private String markFee;
        private String canSelect;
        private int processStatus;
        private String violationNum;
        private String paymentStatus;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFine() {
            return fine;
        }

        public String getFineDes() {
            return "罚款：" + fine;
        }

        public void setFine(String fine) {
            this.fine = fine;
        }

        public String getAddress() {
            return address;
        }

        public String getAddressDes() {
            return province + city + address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getReason() {
            return reason;
        }

        public String getReasonDes() {
            return "原因：" + reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getViolationCity() {
            return violationCity;
        }

        public void setViolationCity(String violationCity) {
            this.violationCity = violationCity;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(String serviceFee) {
            this.serviceFee = serviceFee;
        }

        public String getMarkFee() {
            return markFee;
        }

        public void setMarkFee(String markFee) {
            this.markFee = markFee;
        }

        public String getCanSelect() {
            return canSelect;
        }

        public void setCanSelect(String canSelect) {
            this.canSelect = canSelect;
        }

        public int getProcessStatus() {
            return processStatus;
        }

        public void setProcessStatus(int processStatus) {
            this.processStatus = processStatus;
        }

        public String getViolationNum() {
            return violationNum;
        }

        public void setViolationNum(String violationNum) {
            this.violationNum = violationNum;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getProcessStatusDes() {
            String status = "";
            switch (processStatus) {
                case 1:
                    status = "未处理";
                    break;
                case 2:
                    status = "处理中";
                    break;
                case 3:
                    status = "已处理";
                    break;
            }
            return code + "：" + status;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ApiViolationProvince extends BaseBean {
        private String provinceId;
        private String province;
        private List<ApiViolationCity> list;

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<ApiViolationCity> getList() {
            return list;
        }

        public void setList(List<ApiViolationCity> list) {
            this.list = list;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ApiViolationCity extends BaseBean {
        private String cityId;
        private String city;
        private int engineNoLength;
        private int vinLength;
        private String cityPrefix;
        private String carTypes;

        public String getVinDes() {
            switch (vinLength) {
                case -1:
                    return "请输入VIN码";
                case 0:
                    return "请输入VIN码(非必填)";
                default:
                    return "请输入VIN码后" + vinLength + "位";
            }
        }

        public String getEngineDes() {
            switch (engineNoLength) {
                case -1:
                    return "请输入发动机号";
                case 0:
                    return "请输入发动机号(非必填)";
                default:
                    return "请输入发动机号" + vinLength + "位";
            }
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getEngineNoLength() {
            return engineNoLength;
        }

        public void setEngineNoLength(int engineNoLength) {
            this.engineNoLength = engineNoLength;
        }

        public int getVinLength() {
            return vinLength;
        }

        public void setVinLength(int vinLength) {
            this.vinLength = vinLength;
        }

        public String getCityPrefix() {
            return cityPrefix;
        }

        public void setCityPrefix(String cityPrefix) {
            this.cityPrefix = cityPrefix;
        }

        public String getCarTypes() {
            return carTypes;
        }

        public void setCarTypes(String carTypes) {
            this.carTypes = carTypes;
        }
    }
}
