package com.iflytek.demo.evenbus;

/**
 * Created by jianglei on 2017/9/11.
 */

public class MessageEven {
    public final String message;
    public MessageEven(String message) {
        this.message = message;
    }
    public String getMsg(){
        return message;
    }
}
