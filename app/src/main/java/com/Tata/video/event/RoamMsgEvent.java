package com.Tata.video.event;

import com.Tata.video.bean.ChatUserBean;

/**
 * Created by cxf on 2018/7/20.
 * 漫游消息事件
 */

public class RoamMsgEvent {

    private ChatUserBean mBean;

    public RoamMsgEvent(ChatUserBean bean){
        mBean=bean;
    }

    public ChatUserBean getChatUserBean() {
        return mBean;
    }
}
