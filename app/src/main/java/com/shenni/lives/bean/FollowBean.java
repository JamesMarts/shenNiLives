package com.shenni.lives.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 * function：
 */

public class FollowBean {


    /**
     * status : 1
     * info : 广告信息
     * list : [{"img":"http://snsbao.com/zhibo/public/uploads/20170502/95ea5c0796a60db16a7370f98ea4046c.jpg","link":"www.baidu.com"},{"img":"http://snsbao.com/zhibo/public/uploads/20170502/d7626720fa2d2185c64cfae3380f1f35.jpg","link":"www.baidu.com"},{"img":"http://snsbao.com/zhibo/public/uploads/20170502/11cab613aac990e7780ffd0f9d00881c.jpg","link":"www.baidu.com"},{"img":"http://snsbao.com/zhibo/public/uploads/20170502/e7e7141d20e5454cb6d8d7474a4fbc40.jpg","link":"www.baidu.com"},{"img":"http://snsbao.com/zhibo/public/uploads/20170502/b1f0ae64c8b7c4b90bf0d9a29bfe126d.jpg","link":"www.baidu.com"}]
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
         * img : http://snsbao.com/zhibo/public/uploads/20170502/95ea5c0796a60db16a7370f98ea4046c.jpg
         * link : www.baidu.com
         */

        private String img;
        private String link;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
