package com.shenni.lives.bean;

/**
 * Created by Administrator on 2017/5/23.
 * function：
 */

public class GoldBean {

    /**
     * status : 1
     * info : 成功
     * ordernum : 20170523130512570691
     * remark : 开通会员
     */

    private int status;
    private String info;
    private String ordernum;
    private String remark;

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

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
