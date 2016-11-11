package com.fuxiaosong.url2ioelastic;

/**
 * 对解析出的标题和正文内容进行二次处理
 * 如果需要对标题、正文内容进行二次处理则需要继承该类，
 * 并按需重写 BaseProcess 的 processTitle(...) 或 processContent(...) 方法
 *
 * @author fuxiaosong
 * @version 1.0.0 2016年11月10日 14:10:32
 */
public class BaseProcess {
    /**
     * 对解析出的标题进行二次处理
     *
     * @param title 解析出的标题
     * @return 二次处理之后的标题
     */
    public String processTitle(String title){
        return title;
    }

    /**
     * 对解析出的正文内容进行二次处理
     *
     * @param content 解析出的正文内容
     * @return 二次处理之后的正文内容
     */
    public String processContent(String content){
        return content;
    }
}