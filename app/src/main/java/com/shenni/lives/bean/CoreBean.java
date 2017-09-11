package com.shenni.lives.bean;

/**
 * Created by Administrator on 2017/5/12.
 * function：
 */

public class CoreBean {


    /**
     * status : 1
     * info : 成功
     * con : {"dfh":1,"gw":1,"zs":300,"qq":"11634222"}
     * {"status":1,"info":"成功","con":{"dfh":49,"gw":98,"zs":400,"qq":"2070706356","ttk":49}}
     * {
     "status": 1,
     "info": "成功",
     "con": {
     "id": 1,
     "dfh": 1,
     "gw": 1,
     "zs": 400,
     "ttk": 1,
     "qq": "2070706356",
     "ios_version": "1.0",
     "ios_address": "https://fir.im/634z",
     "ad_version": "",
     "ad_address": "",
     "content": "",
     "status": 0,
     "addtime": 1478306296
     }
     }
     *
     */

    private int status;
    private String info;
    private ConBean con;

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

    public ConBean getCon() {
        return con;
    }

    public void setCon(ConBean con) {
        this.con = con;
    }

    public static class ConBean {
        /**
         * dfh : 1
         * gw : 1
         * zs : 300
         * qq : 11634222
         */

        private String dfh;
        private String gw;
        private String zs;
        private String qq;
        private String ttk;
        private String ios_version;
        private String ios_address;
        private String ad_version;
        private String ad_address;

        public String getIos_version() {
            return ios_version;
        }

        public void setIos_version(String ios_version) {
            this.ios_version = ios_version;
        }

        public String getIos_address() {
            return ios_address;
        }

        public void setIos_address(String ios_address) {
            this.ios_address = ios_address;
        }

        public String getAd_version() {
            return ad_version;
        }

        public void setAd_version(String ad_version) {
            this.ad_version = ad_version;
        }

        public String getAd_address() {
            return ad_address;
        }

        public void setAd_address(String ad_address) {
            this.ad_address = ad_address;
        }

        public String getTtk() {
            return ttk;
        }

        public void setTtk(String ttk) {
            this.ttk = ttk;
        }

        public String getDfh() {
            return dfh;
        }

        public void setDfh(String dfh) {
            this.dfh = dfh;
        }

        public String getGw() {
            return gw;
        }

        public void setGw(String gw) {
            this.gw = gw;
        }

        public String getZs() {
            return zs;
        }

        public void setZs(String zs) {
            this.zs = zs;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }
}
