package com.shenni.lives.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 * function：主播详情
 */

public class AnchorBean {

    /**
     * status : 0
     * info : 主播详情
     * list : {"aid":198802,"smallpic":"http://snsbao.com/zhibo/public/uploads/20170503/3c51b97dc7f891272328e7d530793013.jpeg","nickname":"这货色是？","address":"上海","source":"http://www.snsbao.com/zhibo.mp4","audience":1231231,"audience_start":1,"audience_end":5,"coin":3112323,"coin_start":1,"coin_end":5,"fun":123123,"fun_start":0,"fun_end":5,"qq":"12312312","wx":"123123123","is_focus":0}
     */

    private String status;
    private String info;
    private ListBean list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * aid : 198802
         * smallpic : http://snsbao.com/zhibo/public/uploads/20170503/3c51b97dc7f891272328e7d530793013.jpeg
         * nickname : 这货色是？
         * address : 上海
         * source : http://www.snsbao.com/zhibo.mp4
         * audience : 1231231
         * audience_start : 1
         * audience_end : 5
         * coin : 3112323
         * coin_start : 1
         * coin_end : 5
         * fun : 123123
         * fun_start : 0
         * fun_end : 5
         * qq : 12312312
         * wx : 123123123
         * is_focus : 0
         */

        private String aid;
        private String smallpic;
        private String nickname;
        private String address;
        private String source;
        private int audience;
        private int audience_start;
        private int audience_end;
        private int coin;
        private int coin_start;
        private int coin_end;
        private int fun;
        private int fun_start;
        private int fun_end;
        private String qq;
        private String wx;
        private String is_focus;
        private String ciku_id;
        private CikuBean ciku;


        public String getCiku_id() {
            return ciku_id;
        }

        public void setCiku_id(String ciku_id) {
            this.ciku_id = ciku_id;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getSmallpic() {
            return smallpic;
        }

        public void setSmallpic(String smallpic) {
            this.smallpic = smallpic;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getAudience() {
            return audience;
        }

        public void setAudience(int audience) {
            this.audience = audience;
        }

        public int getAudience_start() {
            return audience_start;
        }

        public void setAudience_start(int audience_start) {
            this.audience_start = audience_start;
        }

        public int getAudience_end() {
            return audience_end;
        }

        public void setAudience_end(int audience_end) {
            this.audience_end = audience_end;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getCoin_start() {
            return coin_start;
        }

        public void setCoin_start(int coin_start) {
            this.coin_start = coin_start;
        }

        public int getCoin_end() {
            return coin_end;
        }

        public void setCoin_end(int coin_end) {
            this.coin_end = coin_end;
        }

        public int getFun() {
            return fun;
        }

        public void setFun(int fun) {
            this.fun = fun;
        }

        public int getFun_start() {
            return fun_start;
        }

        public void setFun_start(int fun_start) {
            this.fun_start = fun_start;
        }

        public int getFun_end() {
            return fun_end;
        }

        public void setFun_end(int fun_end) {
            this.fun_end = fun_end;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWx() {
            return wx;
        }

        public void setWx(String wx) {
            this.wx = wx;
        }

        public String getIs_focus() {
            return is_focus;
        }

        public void setIs_focus(String is_focus) {
            this.is_focus = is_focus;
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
}
