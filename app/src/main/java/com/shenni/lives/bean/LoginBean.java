package com.shenni.lives.bean;

/**
 * Created by Administrator on 2017/5/3.
 * function：
 */

public class LoginBean {

    /**
     * status : 1
     * info : 成功
     * user : {"uid":176179,"nickname":"用户JDMSvRXoNP","headpic":"http://snsbao.com/zhibo/public/img/moface.png","sex":"0","level":0,"wallet":0,"status":1,"focus":0}
     */

    private int status;
    private String info;
    private UserBean user;

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

    public static class UserBean {
        /**
         * uid : 176179
         * nickname : 用户JDMSvRXoNP
         * headpic : http://snsbao.com/zhibo/public/img/moface.png
         * sex : 0
         * level : 0
         * wallet : 0
         * status : 1
         * focus : 0
         */

        private String uid;
        private String nickname;
        private String headpic;
        private String sex;
        private String level;
        private String wallet;
        private String status;
        private String focus;

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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }
    }
}
