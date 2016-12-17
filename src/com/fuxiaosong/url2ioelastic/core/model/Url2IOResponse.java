package com.fuxiaosong.url2ioelastic.core.model;

/**
 * Url2IOResponse 服务器返回值的模型类
 *
 * @author fuxiaosong
 * @version 1.0.0
 * @since 2016年12月17日
 */
public class Url2IOResponse {
    /*
     * 请求成功
     */
    private String title;
    private String content;
    private String url;
    private String date;
    private String text;
    private String next;

    /*
     * 请求失败
     */
    private String error;
    private String msg;
    private String code;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}