package com.fuxiaosong.url2ioelastic;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Url2IOBase 类
 *
 * @author fuxiaosong
 * @version 1.0.0 2016年11月10日 14:10:32
 */
public final class Url2IOBase {
    //索引，标识当前爬取到了第几页
    private int mIndex = 1;
    //总共爬取多少页数据
    private int mTotal = 10;
    //基础url
    private String mBaseUrl = "";
    //起始的url
    private String mNextUrl = "";
    //爬取线程休眠的时间，单位毫秒
    private Long mSleepTime = 2000L;

    //标题和正文内容的二次处理对象
    private BaseProcess mBaseProcess = null;

    //保存线程
    private Class<? extends BaseThread> mClassName = null;

    //保存各种错误信息
    private static HashMap<String , String> mErrorInfoMap = null;
    //错误类型
    private static final String PERMISSION_ERROR = "PermissionError";
    private static final String HTTP_ERROR = "HTTPError";
    private static final String URL_ERROR = "URLError";
    private static final String TYPE_ERROR = "TypeError";
    private static final String UN_KNOW_ERROR = "UnknowError";

    /*
     * 初始化各种错误信息
     */
    static {
        mErrorInfoMap = new HashMap<>();
        mErrorInfoMap.put(PERMISSION_ERROR, "token认证错误；已超出使用配额");
        mErrorInfoMap.put(HTTP_ERROR, "抓取需要提取正文的网页时发生HTTP请求错误，如：404 Not Found");
        mErrorInfoMap.put(URL_ERROR, "抓取需要提取正文的网页时发生网址错误，如：Name or service not known");
        mErrorInfoMap.put(TYPE_ERROR, "请求的资源不是html文档或xhtml文档，无法提取正文");
        mErrorInfoMap.put(UN_KNOW_ERROR, "未知错误，很可能是服务器内部错误。具体看错误消息");
    }

    /**
     * 构造方法
     *
     * @param index 索引，标识当前爬取到了第几页
     * @param total 总共爬取多少页数据
     * @param token token
     * @param beginUrl 基础url
     * @param sleepTime 爬取线程休眠的时间，单位毫秒
     */
    public Url2IOBase(int index , int total , String token , String beginUrl, Long sleepTime , BaseProcess baseProcess , Class<? extends BaseThread> className) {
        this.mIndex = index;
        this.mTotal = total;
        this.mBaseUrl = "http://api.url2io.com/article?token="+token+"&fields=next,text&url=";
        this.mNextUrl = beginUrl;
        this.mSleepTime = sleepTime;
        this.mBaseProcess = baseProcess;
        this.mClassName = className;
    }

    /**
     * 核心方法
     */
    public void process() {
        //StringBuffer，用来承接服务器返回值
        StringBuffer sb = null;
        int i = 0;
        while (i < mTotal) {
            /*
             * 如果下一页的url为空，则停止爬取动作
             */
            if(null == mNextUrl || "".equals(mNextUrl)){
                System.out.println("**************************************");
                System.out.println("**** 要爬取的url为空,爬取动作停止 *****");
                System.out.println("**************************************");
                return;
            }
            //实例化StringBuffer
            sb = new StringBuffer();
            /*
             * 发起网络请求
             */
            try {
                URL url = new URL(mBaseUrl + mNextUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String data;
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
                br.close();
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
             * 使用Gson解析服务器返回的数据为Response对象
             */
            Response response = new Gson().fromJson(sb.toString(), Response.class);

            /*
             * 看看是不是出问题了
             */
            if(null != response.getMsg() && null != response.getError()){
                System.out.println("**************************************");
                System.out.println("********** duang 出问题了 *************");
                System.out.println("***> msg：" + response.getMsg());
                System.out.println("***> error：" + mErrorInfoMap.get(response.getError()));

                /*
                 * 发生 HTTPError 多半是在爬取网页的时候遇到了下一页连接是一个广告的情况，这时只需要重试即可
                 * 要重试，需要保证：
                 * mIndex没有改变，mNextUrl没有改变，i也没有改变
                 *
                 * 如果是其他类型的错误，就没辙了
                 */
                if(HTTP_ERROR.equals(response.getError())){
                    System.out.println("***> (可能试遇到广告了)开始重试... ***");
                    System.out.println("**************************************");
                    continue;
                }else{
                    System.out.println("***> (我没辙了)需要你自己解决... ***");
                    System.out.println("**************************************");
                    return;
                }
            }

            /*
             * 如果没有自定义处理类，就不用再处理了
             */
            if(! ("BaseProcess".equals(mBaseProcess.getClass().getSimpleName()))) {
                response.setTitle(mBaseProcess.processTitle(response.getTitle()));
                response.setText(mBaseProcess.processContent(response.getText()));
            }

            /*
             * 将Text的内容设置道Content中，并将Text设置为空字符串
             */
            response.setContent(response.getText());
            response.setText("");

            /*
             * 启动线程进行自定义保存操作
             */
            try {
                BaseThread baseThread = mClassName.newInstance();
                baseThread.fillData(response , mIndex);
                baseThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            /*
             * 保存下一页的url
             */
            mNextUrl = response.getNext();

            /*
             * 打印信息
             */
            System.out.println("**************************************");
            System.out.println("***> 爬取第 " + mIndex + " 页");
            System.out.println("***> 标题： " + response.getTitle());
            System.out.println("***> 开头几个字：" + response.getContent().substring(0, 20));
            System.out.println("**************************************");
            System.out.println("");

            /*
             * i++
             */
            i++;

             /*
             * mIndex自增
             */
            mIndex++;

            /*
             * 不可用高频的请求服务
             */
            try {
                Thread.sleep(mSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(i == mTotal){
            System.out.println("**************************************");
            System.out.println("********** HaHa 爬完了!!! *************");
            System.out.println("**************************************");
        }
    }

    /**
     * 建造者类
     */
    public static final class Builder {
        //索引，标识当前爬取到了第几页
        private int mIndex = 1;
        //总共爬取多少页数据
        private int mTotal = 10;
        //mToken
        private String mToken = "????";
        //起始的url
        private String mNextUrl = "http://www.qiushibaike.com/";
        //爬取线程休眠的时间，单位毫秒
        private Long mSleepTime = 2000L;
        //标题和正文内容的二次处理对象
        private BaseProcess mBaseProcess = null;
        //保存线程
        private Class<? extends BaseThread> mClassName = null;

        /**
         * 构造方法
         * 需要在构造方法内实例化 mBaseProcess 对象
         */
        public Builder(){
            mBaseProcess = new BaseProcess();
            mClassName = BaseThread.class;
        }

        /**
         * 索引，标识当前爬取到了第几页
         * 该属性的默认值为1，仅仅是标识当前爬取到了多少页，在控制台打印信息的时候显示用，其他并没有什么用
         *
         * @param index
         * @return Builder实例
         */
        public Builder index(int index){
            this.mIndex = index;
            return this;
        }

        /**
         * 总共需要爬取多少页
         *
         * @param total 总共需要爬取多少页
         * @return Builder实例
         */
        public Builder total(int total){
            this.mTotal = total;
            return this;
        }

        /**
         * 设置token，该函数必须调用
         *
         * @param token token
         * @return Builder实例
         */
        public Builder token(String token){
            this.mToken = token;
            return this;
        }

        /**
         * 爬取动作开始的第一页的url
         *
         * @param beginUrl
         * @return Builder实例
         */
        public Builder beginUrl(String beginUrl){
            this.mNextUrl = beginUrl;
            return this;
        }

        /**
         * 爬取线程爬取动作不能太高频，每爬取一次，休息 mSleepTime 毫秒
         *
         * @param sleepTime 爬取线程休眠的时间，单位毫秒
         * @return Builder实例
         */
        public Builder sleepTime(Long sleepTime){
            this.mSleepTime = sleepTime;
            return this;
        }

        /**
         * 如果需要对标题、正文内容进行二次处理则需要继承该类，
         * 并按需重写 BaseProcess 的 processTitle(...) 或 processContent(...) 方法
         *
         * @param process 标题和正文内容的二次处理对象
         * @return Builder实例
         */
        public Builder process(BaseProcess process){
            this.mBaseProcess = process;
            return this;
        }

        /**
         *
         *
         * @param className
         * @return
         */
        public Builder thread(Class<? extends BaseThread> className){
            this.mClassName = className;
            return this;
        }

        /**
         * build 方法，构造出 Url2IOBase 实例
         *
         * @return Url2IOBase 实例
         */
        public Url2IOBase build(){
            return new Url2IOBase(mIndex , mTotal , mToken, mNextUrl, mSleepTime, mBaseProcess, mClassName);
        }
    }
}
