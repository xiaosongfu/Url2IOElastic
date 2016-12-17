package com.fuxiaosong.url2ioelastic.core;

import com.fuxiaosong.url2ioelastic.core.model.Url2IOResponse;

/**
 * 结果保存线程
 * 继承该类，自动继承以下的属性：
 * data  ：Url2IOResponse 类型，包含 url2io 服务器返回的所以字段，特别注意：网页正文内容在 content 属性里
 * index ：当前页索引
 * 在自定义的类中按需对他们进行处理，比如保存在文件，或者保存到数据库
 *
 * @author fuxiaosong
 * @version 1.0.0
 * @since 2016年12月17日
 */
public class BaseResultHandleThread extends Thread {
    //数据
    public Url2IOResponse data = null;
    //当前页索引
    public int index = 1;

    /**
     * 注入数据
     *
     * @param data
     * @param index
     */
    public void fillData(Url2IOResponse data , int index) {
        this.data = data;
        this.index = index;
    }

    @Override
    public void run() {
        super.run();
    }
}
