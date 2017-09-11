package com.shenni.lives.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/22.
 * function：
 */

public class WXPayBean {

    /**
     * status : 1
     * info : 成功
     * url : {"nonceStr":"xoW7auwJxXNELPZ5","package":"Sign=WXPay","partnerId":"1407709002","prepayId":"wx201706221734208e1936b4040436383968","timeStamp":"1498124060","sign":"DAFA6CD471C58F12B161DB583FE5CEF2","appid":"wxcf4da3f522d41591"}
     */

    private int status;
    private String info;
    private UrlBean url;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public UrlBean getUrl() {
        return url;
    }

    public void setUrl(UrlBean url) {
        this.url = url;
    }

    public static class UrlBean {
        /**
         * nonceStr : xoW7auwJxXNELPZ5
         * package : Sign=WXPay
         * partnerId : 1407709002
         * prepayId : wx201706221734208e1936b4040436383968
         * timeStamp : 1498124060
         * sign : DAFA6CD471C58F12B161DB583FE5CEF2
         * appid : wxcf4da3f522d41591
         */

        private String nonceStr;
        @SerializedName("package")
        private String packageX;
        private String partnerId;
        private String prepayId;
        private String timeStamp;
        private String sign;
        private String appid;

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }
}
