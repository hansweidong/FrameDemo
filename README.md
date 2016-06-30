# FrameDemo
android项目框架


##1.实体生成
Gson、FastJson<br/>
注：
   + 在Android Studio中可采用GsonFomat插件快速生成Model
   + 使用 fastJSON 后,App 四处起火,主要表现为: 
      + 加了符号 Annotation 的实体属性,一使用就崩溃。 
      + 当有泛型属性时,一使用就崩溃。
    在调试的时候没事,可是每次打签名混淆包,就会出现上述问题。<br/>
    解决办法：<br/>
    混淆文件缺了以下两行代码导致的:<br/>
    ```
    -keepattributes Signature // 避免混淆泛型
    -keepattributes *Annotation* // 不混淆注解
    ```
    
##2.Android混淆
每次发布新版本都要写 mapping.txt。每发布一个版本，如果用户遇到一个bug，同时提交了一个混淆过的堆栈跟踪。 通过保留mapping.txt文件，来确定你可以调试的问题。<br/>
[Android混淆相关](http://www.jianshu.com/p/6a9247829b92)
##3.Google开源的FlexboxLayout
[Google开源的FlexboxLayout](https://zhuanlan.zhihu.com/p/20908345)
##4.编程指南
+ [Android 开发最佳实践](https://github.com/futurice/android-best-practices)
+ [Android6.0等新功能兼容包使用](http://www.csdn.net/article/2015-10-05/2825847/1)
+ [Android开发：Translucent System Bar 的最佳实践](http://www.jianshu.com/p/0acc12c29c1b)
+ [Android Studio advanced configuration](http://liukun.engineer/2016/04/10/Android-Studio-advanced-configuration/)

##5.提升小工具
+ [LeakCanary](https://github.com/square/leakcanary)是一个开源的在debug版本中检测内存泄漏的java库。
+ 当程序发生bug时请用户调出隐藏页面，上传本地db文件（File dbFile = getDatabasePath（path）），给用户发一个链接，让用户点击即可。
 [Android实现通过浏览器点击链接打开本地应用（APP）并拿到浏览器传递的数据](http://blog.csdn.net/jiangwei0910410003/article/details/23940445)<br/>
 [Android入门：隐式Intent](http://blog.csdn.net/xiazdong/article/details/7764865)
+ [借助Stetho在Chrome上调试Android网络&数据库](http://www.jianshu.com/p/03da9f91f41f)


##6.常用控件
+ [列表右滑删除](https://github.com/lijiazhicool/AndroidSwipeLayout)
+ [tab切换列表SmartTabLayout](https://github.com/lijiazhicool/SmartTabLayout)
+ [基于JavaCV库实现Android端的音视频录制，支持断点录制、片段回删、定点聚焦、闪光灯、摄像头切换和视频水印等。
](https://github.com/wzystal/MediaRecorder)
+ [android视频录制，模仿微视，支持按下录制、抬起暂停。进度条断点显示](https://github.com/qdrzwd/VideoRecorder)
+ [Android Form EditText](https://github.com/lijiazhicool/android-edittext-validator)可进行电话、邮箱等验证
+ [通用对话框dialogplus](https://github.com/orhanobut/dialogplus)
