package com.shenni.lives.bean;

/**
 * Created by xpf on 2016/12/25 :)
 * GitHub:xinpengfei520
 * Function:这个类很简单，构造时传进去一个字符串，然后可以通过getmMsg()获取出来
 */

public class LiveEvent {

    // 此类中的属性可根据实际情况进行定义(可以有多个)
    private boolean isLive;

    public LiveEvent(boolean mMsg) {
        this.isLive =  mMsg;
    }

    public boolean getmMsg() {
        return isLive;
    }
}
