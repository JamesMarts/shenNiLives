package com.shenni.lives.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 * function：
 */

public class MessDetailBean {


    /**
     * status : 0
     * info : 消息详情
     * msg : {"id":10001,"title":"213213","content":{"txtlist":["正文内容"],"pics":["http://snsbao.com/zhibo/public/uploads/20170525/088218f91b6021e81fbe84ddd17cb436.jpg"]},"addtime":"2017-05-25 11:13:53"}
     */

    private int status;
    private String info;
    private MsgBean msg;

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

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * id : 10001
         * title : 213213
         * content : {"txtlist":["正文内容"],"pics":["http://snsbao.com/zhibo/public/uploads/20170525/088218f91b6021e81fbe84ddd17cb436.jpg"]}
         * addtime : 2017-05-25 11:13:53
         */

        private int id;
        private String title;
        private ContentBean content;
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

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public static class ContentBean {
            private List<String> txtlist;
            private List<String> pics;

            public List<String> getTxtlist() {
                return txtlist;
            }

            public void setTxtlist(List<String> txtlist) {
                this.txtlist = txtlist;
            }

            public List<String> getPics() {
                return pics;
            }

            public void setPics(List<String> pics) {
                this.pics = pics;
            }
        }
    }
}
