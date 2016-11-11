package com.fuxiaosong.url2ioelastic;

/**
 * 结果保存线程
 *
 * 继承该类，获得结果，并按需对结果进行处理，比如保存在文件中，或者写道数据库
 *
 * @author fuxiaosong
 * @version 1.0.0 2016年11月10日 14:10:32
 */
public class BaseThread extends Thread {
    public Response response = null;
    public int index = 1;

    public void fillData(Response response , int index) {
        this.response = response;
        this.index = index;
    }

    @Override
    public void run() {
        super.run();
    }
}
