package com.shenni.lives.bean;

/**
 * Created by Ocean on 6/22/16.
 */
public class ShareBean {
    private String text;
    private int img;


    public ShareBean() {
    }

    public ShareBean(String text, int img) {
        this.text = text;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
