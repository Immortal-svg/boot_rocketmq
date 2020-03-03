package com.fzm.common.enums;

import java.text.MessageFormat;

/**
 * Created by Allen on 2016/8/31.
 */
public class Message {

    private int code;
    private String msg;
    private Object data = "";

    public Message() {
    }

    public Message(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Message(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public Message(ResponseEnum responseEnum, Object data) {
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
        this.data = data;
    }
    public Message(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
    }

    // msg动态填充
    public Message(ResponseEnum responseEnum, String msg){
        this.code = responseEnum.getCode();
        this.msg = MessageFormat.format(responseEnum.getMsg(),msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "Message [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
    
}
