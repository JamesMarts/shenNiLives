package com.shenni.lives.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 * function：
 */

public class GiftBean {


    /**
     * status : 1
     * info : 礼物列表
     * list : [{"id":980550344,"title":"一箭穿心","img":"http://snsbao.com/zhibo/public/uploads/20170503/b8875c849e372eaaa27eb413b8327b69.gif","price":"666","type":0},{"id":980550345,"title":"红粉钻戒","img":"http://snsbao.com/zhibo/public/uploads/20170508/f454553b4edc970272ef2d900556ffc2.png","price":"1000","type":1},{"id":980550346,"title":"爱心热气球","img":"http://snsbao.com/zhibo/public/uploads/20170508/23729b0b5c4e3073826d1ac123ab5eed.png","price":"888","type":2},{"id":980550347,"title":"棒棒糖","img":"http://snsbao.com/zhibo/public/uploads/20170508/9c44fd353f6396d60cf1a192fa828b4d.png","price":"100","type":3},{"id":980550348,"title":"么么哒","img":"http://snsbao.com/zhibo/public/uploads/20170508/d0776b6ece6553d49c848eaab8afd280.png","price":"188","type":4},{"id":980550349,"title":"老司鸡","img":"http://snsbao.com/zhibo/public/uploads/20170507/34eeb228146ae97fcc48b6adcbbfa7bc.png","price":"30","type":5},{"id":980550350,"title":"火箭来了","img":"http://snsbao.com/zhibo/public/uploads/20170508/fac63bf1225e5c980ddfe082031a1299.png","price":"33","type":6},{"id":980550351,"title":"这什么玩意","img":"http://snsbao.com/zhibo/public/uploads/20170508/09836471495e0fdca801d375bfbb3662.png","price":"111111","type":7},{"id":980550352,"title":"我要主播吻我","img":"http://snsbao.com/zhibo/public/uploads/20170508/bacb06d76c43f1976bacf15925bff76f.png","price":"0.1","type":8}]
     */

    private int status;
    private String info;
    private List<ListBean> list;
    private String sendUserId;
    private String sendUserName;
    private String sendUserPic;
    private String hitCombo;
    private Long sendGiftTime;


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

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserPic() {
        return sendUserPic;
    }

    public void setSendUserPic(String sendUserPic) {
        this.sendUserPic = sendUserPic;
    }

    public String getHitCombo() {
        return hitCombo;
    }

    public void setHitCombo(String hitCombo) {
        this.hitCombo = hitCombo;
    }

    public Long getSendGiftTime() {
        return sendGiftTime;
    }

    public void setSendGiftTime(Long sendGiftTime) {
        this.sendGiftTime = sendGiftTime;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 980550344
         * title : 一箭穿心
         * img : http://snsbao.com/zhibo/public/uploads/20170503/b8875c849e372eaaa27eb413b8327b69.gif
         * price : 666
         * type : 0
         */

        private String id;
        private String title;
        private String img;
        private String price;
        private int type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
