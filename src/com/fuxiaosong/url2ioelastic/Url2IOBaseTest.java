package com.fuxiaosong.url2ioelastic;

import com.fuxiaosong.url2ioelastic.plugins.WriteToFileThread;

/**
 * Url2IOBase 的测试类 Url2IOBaseTest
 *
 * @author fuxiaosong
 * @version 1.0.0 2016年11月10日 14:10:32
 */
public class Url2IOBaseTest {
    public static void main(String[] args){
        /*
         * 爬取默认的网址：糗事百科 http://www.qiushibaike.com/
         */
        //最简单的使用方法，所有属性均使用默认值 index为1 total为10 baseUrl为http://www.qiushibaike.com/
        //      sleep为2000毫秒 Precess为BaseProcess,什么也不做 结果保存方法线程为BaseProcess,什么也不做
        new Url2IOBase.Builder().token("4BNmeuIYRduW6S6p_J8hlw").build().process();

        //为一些属性设置了值，并指定了结果保存线程为 WriteToFileThread ，他是一个结果保存线程示例，效果是把结果保存到文件中
        //爬取的数据为：小说网页http://www.ybdu.com/xiaoshuo/13/13308/5819124.html
//        new Url2IOBase.Builder().token("4BNmeuIYRduW6S6p_J8hlw").beginUrl("http://www.ybdu.com/xiaoshuo/13/13308/5819124.html").index(1).total(5).sleepTime(1000L).thread(WriteToFileThread.class).build().process();

        //自定义Process请参考 -> Url2IOBase：https://github.com/xiaosongfu/Url2IOBase
    }
}