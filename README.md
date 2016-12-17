> [Url2IOBase 库](https://github.com/xiaosongfu/Url2IOBase) 仅支持将爬取到的标题和内容写入文件，且文件所在的目录是项目的根目录，它不支持其他任何操作，如保存到数据库等。  
> [Url2IOElastic 库](https://github.com/xiaosongfu/Url2IOElastic) 则是采用了插件机制，可以自定义类对结果进行自定义处理，如保存到数据库、上传到服务器等。  

---  

### #1 简介  
项目使用建造者模式，有7个属性可以定制，其中的 token 和 beginUrl 这2个属性是必须指定值的，其余参数是可选的。各个参数的介绍如下：  
>  
1. index : 索引，标识当前爬取到第多少页，在控制台打印信息的时候会使用，此外也会传递到 BaseResultHandleThread 类里，该属性的默认值为1  
2. total : 指定共需要爬取多少页的数据，该属性的默认值为10  
3. token : 必须要设置，到官网 url2io 注册帐号就可以拿到 token  
4. beginUrl : 开始爬取的第一个网页的 url，必须要设置!    
5. sleepTime : 我也不知道 url2io 的抗压能力怎么样，不过爬一页休息2秒也没什么关系吧，该属性的默认值为2秒  
6. setExtraProcessor : 把标题、正文内容和时间爬出来了，但是可能包含一些我们不想要的字符，这时候就可以通过额外的处理，将那些多余的字符去掉，默认不做任何处理  
7. addResultHandleThread : 爬取数据的最终目的是为我所用，通过自定义类继承 BaseResultHandleThread 即可实现数据的自定义处理，可以添加多个，默认不做任何处理  

### #2 用法
>  
1. 引入 jar 包( jar 包需要去 [release](https://github.com/xiaosongfu/Url2IOElastic/releases) 界面下载，当然也可以自行打包)  
2. 创建一个 Url2IOElasticCore.Builder 实例，并按需给各个属性赋值  
3. 调用 Url2IOElasticCore.Builder 的 build() 方法构造一个 Url2IOElasticCore 实例  
4. 调用其 Url2IOElasticCore 实例的 article() 方法。  

---  
示例代码：  
```  
// step 1
Url2IOElasticCore.Builder builder = new Url2IOElasticCore.Builder()
        .token("4BNmeuIYRduW6S6p_J8hlw")
        .beginUrl("http://www.ybdu.com/xiaoshuo/13/13308/5819124.html")
        .index(1)
        .total(3)
        .sleepTime(1000L);
// setExtraProcessor 和 addResultHandleThread 属性未指定值

// step 2
Url2IOElasticCore elasticCore = builder.build();

// step 3
elasticCore.article();
```  

---  

**现对setExtraProcessor 和 addResultHandleThread 属性做详细说明**  

setExtraProcessor  
>  
通过自定义一个类，继承 BaseExtraProcessor ，并按需重载它的 processTitle(String title) 、 processContent(String content) 、 processDate(String date) 这3个方法来实现对爬取到的正文标题、正文内容和时间进行额外的处理，比如去掉一些多余的字符等等。  


addResultHandleThread  
>  
通过自定义一个类，继承 BaseResultHandleThread ， BaseResultHandleThread  是一个继承自 Thread 的类，继承它就可用自动继承 一个 int 类型的 index 属性和一个 Url2IOResponse 类型的 data 对象，index 属性标识了 data 数据是哪一个 index 对应的数据， Url2IOResponse 类型是 url2io 服务器返回的 json 字符串对应的 model ，所有需要的数据都可用在里面找到。  
代码内使用 ArrayList 来承载 BaseResultHandleThread 的 Class 类型，这意味着你可以添加多个 BaseResultHandleThread 的子类，他们的 run 方法都会被执行。例如你既可以将结果写入本地文件，还可以保存到数据库，还可以保存到第三方服务器，等等。

### #3 视频
[传送门 >>>]()
### #4 Over
欢迎提 issue，提 PR。  
