package com.fuxiaosong.url2ioelastic.test;

import com.fuxiaosong.url2ioelastic.core.Url2IOElasticCore;
import com.fuxiaosong.url2ioelastic.plugins.WriteToFileThread;

/**
 * Url2IOElasticCore 的测试类 Url2IOElasticTest
 *
 * @author fuxiaosong
 * @version 1.0.0
 * @since 2016年12月17日
 */
public class Url2IOElasticTest {
    public static void main(String[] args){
        // case 1
        //最简单的使用方法，所有属性均使用默认值 index 为 1 total 为 10
        // beginUrl 为 http://www.qiushibaike.com/
        // sleep 为2000毫秒 extraProcessor 为 BaseExtraProcessor ,什么也不做
        // 结果处理线程为 BaseResultHandleThread ,同样什么也不做
        new Url2IOElasticCore.Builder()
                .token("4BNmeuIYRduW6S6p_J8hlw")
                .build()
                .article();

        // case 2
        //爬取小说，网页为：http://www.ybdu.com/xiaoshuo/13/13308/5819124.html
        //为一些属性设置了值，其中添加的一个结果处理线程为 WriteToFileThread ，他的作用是把结果保存到文件中
        new Url2IOElasticCore.Builder()
                .token("4BNmeuIYRduW6S6p_J8hlw")
                .beginUrl("http://www.ybdu.com/xiaoshuo/13/13308/5819124.html")
                .index(1)
                .total(3)
                .sleepTime(1000L)
                .addResultHandleThread(WriteToFileThread.class)
                .build()
                .article();

        // More
        //自定义 extraProcessor 请参考 -> https://github.com/xiaosongfu/Url2IOBase
    }
}