package com.shenni.lives.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/21.
 */

public class WeChatPayBean {


    /**
     * nonceStr : rrxQzx1KC8io4P7q
     * package : Sign=WXPay
     * partnerId : 1407709002
     * prepayId : wx201704211658090a768faf1c0861995397
     * timeStamp : 1492765089
     * sign : 2EA75C65023C20F9DA1F89FAB6D69B3F
     */

    private String nonceStr;
    @SerializedName("package")
    private String packageX;
    private String partnerId;
    private String prepayId;
    private String timeStamp;
    private String sign;
    /**
     * appid : wxcf4da3f522d41591
     */

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
