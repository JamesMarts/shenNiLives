package com.shenni.lives.bean;

import java.util.List;

/**
 * Created by Ocean on 6/22/16.
 */
public class SearchBean {
    /**
     * status : 1
     * info : 搜索列表
     * list : [{"aid":198802,"nickname":"这货色是？","sex":"1","smallpic":"http://snsbao.com/zhibo/public/uploads/20170503/3c51b97dc7f891272328e7d530793013.jpeg","address":"上海","audience":1231231,"level":2,"freetime":0,"is_focus":0},{"aid":198801,"nickname":"草妮马玩意","sex":"0","smallpic":"http://snsbao.com/zhibo/public/uploads/20170503/78a277f0a0c08a0762a798094b6b4400.jpeg","address":"北京","audience":123213,"level":1,"freetime":60,"is_focus":0},{"aid":198800,"nickname":"橘子⊱无人知我梦易醒","sex":"0","smallpic":"http://snsbao.com/zhibo/public/uploads/20170428/09526cacba15433359b819c2bd4202d5.jpg","address":"杭州","audience":105097,"level":0,"freetime":60,"is_focus":0}]
     */

    private int status;
    private String info;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * aid : 198802
         * nickname : 这货色是？
         * sex : 1
         * smallpic : http://snsbao.com/zhibo/public/uploads/20170503/3c51b97dc7f891272328e7d530793013.jpeg
         * address : 上海
         * audience : 1231231
         * level : 2
         * freetime : 0
         * is_focus : 0
         */

        private int aid;
        private String nickname;
        private String sex;
        private String smallpic;
        private String address;
        private int audience;
        private int level;
        private int freetime;
        private int is_focus;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSmallpic() {
            return smallpic;
        }

        public void setSmallpic(String smallpic) {
            this.smallpic = smallpic;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAudience() {
            return audience;
        }

        public void setAudience(int audience) {
            this.audience = audience;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getFreetime() {
            return freetime;
        }

        public void setFreetime(int freetime) {
            this.freetime = freetime;
        }

        public int getIs_focus() {
            return is_focus;
        }

        public void setIs_focus(int is_focus) {
            this.is_focus = is_focus;
        }
    }
}
