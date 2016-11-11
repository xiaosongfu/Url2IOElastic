package com.fuxiaosong.url2ioelastic.plugins;

import com.fuxiaosong.url2ioelastic.BaseThread;
import java.io.*;

/**
 * 自定义结果保存线程示例类
 *
 * 将解析好的数据写入文件
 *
 * @author fuxiaosong
 * @version 1.0.0 2016年11月10日 14:10:32
 */
public class WriteToFileThread extends BaseThread {
    //聚合大小，决定将多少个网页中爬取的正文内容保存在同一个文件里
    public int mTogether = 5;
    //文件名中的章节范围
    private String mFileNameMid = "";
    //写文件对象
    private static  WriterContentToFile mWriterContentToFile = new WriterContentToFile();

    @Override
    public void run() {
        /*
         * 写标题
         */
        mWriterContentToFile.write("title.txt", response.getTitle() + "    " + response.getUrl() + "\n");

        /*
         * 写内容
         */
        int i = (index + mTogether - 1) / mTogether;
        mFileNameMid = ((i - 1) * mTogether +1) + "-" + (i * mTogether);
        mWriterContentToFile.write("content_" + mFileNameMid + ".txt", "\n\r\n" + response.getTitle() + "\n" + response.getContent());
    }

    /**
     * 文件写入器
     */
    private static class WriterContentToFile {
        //文件输出流
        private FileOutputStream mFileOutputStream = null;
        //带缓冲的输出流
        private BufferedOutputStream mBufferedOutputStream =null;

        /**
         * 将内容写入到指定的文件
         *
         * @param fileName 文件名
         * @param content 要写入的内容
         */
        public void write(String fileName , String content){
            try {
                //构造输出流
                mFileOutputStream = new FileOutputStream(new File(fileName) ,true);
                mBufferedOutputStream =new BufferedOutputStream(mFileOutputStream);
                //开始写入文件
                mBufferedOutputStream.write(content.getBytes());
                //刷新输出流并关闭输出流
                mBufferedOutputStream.flush();
                mBufferedOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}