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

##6.常用控件
[列表右滑删除](https://github.com/lijiazhicool/AndroidSwipeLayout)
