package com.shenni.lives.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 * function：
 */

public class MessageBean {

    /**
     * status : 1
     * info : 成功
     * list : [{"id":10000,"title":"恭喜注册！恭喜发财！","addtime":"2017-04-27"}]
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
         * id : 10000
         * title : 恭喜注册！恭喜发财！
         * addtime : 2017-04-27
         */

        private int id;
        private String title;
        private String addtime;

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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
