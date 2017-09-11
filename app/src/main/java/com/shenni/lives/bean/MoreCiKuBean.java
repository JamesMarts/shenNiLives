package com.shenni.lives.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 * function：
 */

public class MoreCiKuBean {

    /**
     * status : 1
     * ciku : {"time":["00:00:02","00:00:03","00:00:04","00:00:05","00:00:06","00:00:09","00:00:15","00:00:17","00:00:18","00:00:19","00:00:20","00:00:21"],"con":[{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":70,"title":"啪啪啪","price":"50","img":"http://swdtq.com/zhibo123/public/uploads/20170606/7eb0d9318823b984f8bfd13a7b7a3353.png","num":"1"},"txt":null},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":70,"title":"啪啪啪","price":"50","img":"http://swdtq.com/zhibo123/public/uploads/20170606/7eb0d9318823b984f8bfd13a7b7a3353.png","num":"1"},"txt":null},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":70,"title":"啪啪啪","price":"50","img":"http://swdtq.com/zhibo123/public/uploads/20170606/7eb0d9318823b984f8bfd13a7b7a3353.png","num":"1"},"txt":null},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":70,"title":"啪啪啪","price":"50","img":"http://swdtq.com/zhibo123/public/uploads/20170606/7eb0d9318823b984f8bfd13a7b7a3353.png","num":"1"},"txt":null},{"user":{"uid":177995,"nickname":"二少","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/5.png","sex":"1"},"gift":{},"txt":"你是独眼龙吗"},{"user":{"uid":177994,"nickname":"云冰秋","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{},"txt":"在撸啦"},{"user":{"uid":177988,"nickname":"甄贞韵","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{},"txt":"最大尺度就是QQ现场那阶段"},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":25,"title":"玫瑰","price":"20","img":"http://swdtq.com/zhibo123/public/uploads/20170606/3cda10cf4ec8f49404ddf3b7b18d9e22.png","num":"1"},"txt":null},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":25,"title":"玫瑰","price":"20","img":"http://swdtq.com/zhibo123/public/uploads/20170606/3cda10cf4ec8f49404ddf3b7b18d9e22.png","num":"1"},"txt":null},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":25,"title":"玫瑰","price":"20","img":"http://swdtq.com/zhibo123/public/uploads/20170606/3cda10cf4ec8f49404ddf3b7b18d9e22.png","num":"1"},"txt":null},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":25,"title":"玫瑰","price":"20","img":"http://swdtq.com/zhibo123/public/uploads/20170606/3cda10cf4ec8f49404ddf3b7b18d9e22.png","num":"1"},"txt":null},{"user":{"uid":177996,"nickname":"国旗","level":2,"headpic":"http://swdtq.com/zhibo123/public/uploads/face/morenlipin.gif","sex":"1"},"gift":{"id":25,"title":"玫瑰","price":"20","img":"http://swdtq.com/zhibo123/public/uploads/20170606/3cda10cf4ec8f49404ddf3b7b18d9e22.png","num":"1"},"txt":null}]}
     */

    private int status;
    private CikuBean ciku;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CikuBean getCiku() {
        return ciku;
    }

    public void setCiku(CikuBean ciku) {
        this.ciku = ciku;
    }

    public static class CikuBean {
        private List<String> time;
        private List<ConBean> con;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<ConBean> getCon() {
            return con;
        }

        public void setCon(List<ConBean> con) {
            this.con = con;
        }

        public static class ConBean {
            /**
             * user : {"nickname":"用户FmvgbOcSTf","level":0,"sex":"1"}
             * gift : {"id":980550350,"title":"火箭来了","price":"33","img":"http://snsbao.com/zhibo/public/uploads/20170508/fac63bf1225e5c980ddfe082031a1299.png"}
             * txt : null
             */

            private UserBean user;
            private GiftBean gift;
            private String txt;

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public GiftBean getGift() {
                return gift;
            }

            public void setGift(GiftBean gift) {
                this.gift = gift;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public static class UserBean {
                /**
                 * nickname : 用户FmvgbOcSTf
                 * level : 0
                 * sex : 1
                 */

                private String uid;
                private String nickname;
                private int level;
                private String sex;
                private String headpic;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getHeadpic() {
                    return headpic;
                }

                public void setHeadpic(String headpic) {
                    this.headpic = headpic;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }
            }

            public static class GiftBean {
                /**
                 * id : 980550350
                 * title : 火箭来了
                 * price : 33
                 * img : http://snsbao.com/zhibo/public/uploads/20170508/fac63bf1225e5c980ddfe082031a1299.png
                 */

                private int id;
                private String title;
                private String price;
                private String img;
                private int num;

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }
            }
        }
    }
}
