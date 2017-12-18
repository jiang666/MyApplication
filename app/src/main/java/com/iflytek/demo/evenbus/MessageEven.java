package com.iflytek.demo.evenbus;

/**
 * Created by jianglei on 2017/9/11.
 */

public class MessageEven {
    public final long message;
    public MessageEven(long message) {
        this.message = message;
    }
    public long getMsg(){
        return message;
    }
}
