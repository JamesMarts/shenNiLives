package com.shenni.lives.bean;

/**
 * Created by Administrator on 2017/6/21.
 * function：
 */

public class PayBean {


    /**
     * status : 1
     * info : 成功
     * user : {"uid":"hdZljZoeaIEFgpPkBi","nickname":"程序猿","mobile":null,"headpic":"http://wx.qlogo.cn/mmopen/CfjnPckVOYD4VmZ9FAaokwXibBxv6rkRicB22Fmcr6iaUl2nuGrib4mYeUoDNOaJxpuF9ibG2H98cPnn4RgtTHJcXFXFibjY7mKtGT/0","sex":"1","wallet":0,"ali_user":"","ali_name":"","focus":0,"fun":0}
     */

    private int status;
    private String info;
    private UserBean user;
    /**
     * urls : http://meipa.b0.upaiyun.com/user/hdZljZoeaIEFgpPkBi/image/20170417/zffwt7oly1mcl02pe7l6.jpeg
     */

    private String urls;
    /**
     * vid : 465073523
     */

    private String vid;
    /**
     * url : weixin://app/wxcf4da3f522d41591/pay/?nonceStr=LQvYW2NrnpzAmGw3&package=Sign%3DWXPay&partnerId=1407709002&prepayId=wx20170421162149b8afa985a70229185683&timeStamp=1492762909&sign=AEBB69236D8B10EF630449E1FD5E1D4E&signType=SHA1
     */

    private String url;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class UserBean {
        /**
         * uid : hdZljZoeaIEFgpPkBi
         * nickname : 程序猿
         * mobile : null
         * headpic : http://wx.qlogo.cn/mmopen/CfjnPckVOYD4VmZ9FAaokwXibBxv6rkRicB22Fmcr6iaUl2nuGrib4mYeUoDNOaJxpuF9ibG2H98cPnn4RgtTHJcXFXFibjY7mKtGT/0
         * sex : 1
         * wallet : 0
         * ali_user :
         * ali_name :
         * focus : 0
         * fun : 0
         */

        private String uid;
        private String nickname;
        private Object mobile;
        private String headpic;
        private String sex;
        private String wallet;
        private String ali_user;
        private String ali_name;
        private int focus;
        private int fun;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getAli_user() {
            return ali_user;
        }

        public void setAli_user(String ali_user) {
            this.ali_user = ali_user;
        }

        public String getAli_name() {
            return ali_name;
        }

        public void setAli_name(String ali_name) {
            this.ali_name = ali_name;
        }

        public int getFocus() {
            return focus;
        }

        public void setFocus(int focus) {
            this.focus = focus;
        }

        public int getFun() {
            return fun;
        }

        public void setFun(int fun) {
            this.fun = fun;
        }
    }
}
