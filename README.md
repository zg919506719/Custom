# Custom and Learn
想做一些关于自定义view和动画的demo

#<a href="http://blog.csdn.net/guolin_blog/article/details/9316683">避免在Bitmap上浪费内存</a>
当我们读取一个Bitmap图片的时候，有一点一定要注意，就是千万不要去加载不需要的分辨率。
在一个很小的ImageView上显示一张高分辨率的图片不会带来任何视觉上的好处，但却会占用我们相当多宝贵的内存。
需要仅记的一点是，将一张图片解析成一个Bitmap对象时所占用的内存并不是这个图片在硬盘中的大小，
可能一张图片只有100k你觉得它并不大，但是读取到内存当中是按照像素点来算的，
比如这张图片是1500*1000像素，使用的ARGB_8888颜色类型，那么每个像素点就会占用4个字节，总内存就是1500*1000*4字节，
也就是5.7M，这个数据看起来就比较恐怖了。

#<a href="http://blog.csdn.net/guolin_blog/article/details/42238627"/>最佳性能实践

使用ProGuard简化代码
ProGuard相信大家都不会陌生，很多人都会使用这个工具来混淆代码

#<a href="http://www.cnblogs.com/cr330326/p/5534915.html">android的相关学习</a>

想要实现多进程的功能也非常简单，只需要在AndroidManifest文件的应用程序组件中声明一个android:process属性就可以了，
<service android:name=".PlaybackService"  
         android:process=":background" />
这里指定的进程名是background，你也可以将它改成任意你喜欢的名字。
需要注意的是，进程名的前面都应该加上一个冒号，表示该进程是一个当前应用程序的私有进程。

如果真的出现了内存泄露，我们应该怎么定位到具体是哪里出的问题呢？
这就需要借助一个内存分析工具了，叫做Eclipse Memory Analyzer（MAT）。
我们需要先将这个工具下载下来，下载地址是：http://eclipse.org/mat/downloads.php。

<a href="http://blog.csdn.net/guolin_blog/article/details/43376527">viewstub需要的时候才进行加载布局</a>
所加载的布局是不可以使用merge标签的，因此这有可能导致加载出来的布局存在着多余的嵌套结构

#<a href="https://mp.weixin.qq.com/s?__biz=MzI3MDE0NzYwNA==&amp;mid=2651434039&amp;idx=1&amp;sn=32ea2abdb5ebfd95e64199cf2050eb36&amp;chksm=f128854cc65f0c5a02f2ee310f4dd1bcf75616bc871c7a5714184398b43870a88d06041091ce&amp;scene=0#wechat_redirect">android混淆</a>

#<a href="http://blog.csdn.net/qidingquan/article/details/53714603">android px,dp转换，屏幕适配</a>

#<a href="http://blog.csdn.net/guolin_blog/article/details/50727753">drawable文件夹中，将对应手机的dpi适配好</a>
出现别的分辨率时候会先查找高分辨率的图片，找不到后找到低分辨率后会进行放大，容易造成OOM

<a href="http://blog.csdn.net/guolin_blog/article/details/49738023">反编译</a>


<a href="http://blog.csdn.net/limonzet/article/details/53328315">MVP架构</a>
model层处理数据，present获取model层处理完的数据，然后处理view成的方法，activity实现view层接口并实例化present，并调用方法


# barrage
弹幕
# 防止内存溢出
1.明确调用System.gc();  
2.图片处理完成后回收内存。  请在调用BitMap进行图片处理后进行内存回收。  bitmap.recycle();  这样会把刚刚用过的图片占用的内存释放。  
3.图片处理时指定大小。  
# Android 基于进程中运行的组件及其状态规定了默认的五个回收优先级：  
IMPORTANCE_FOREGROUND:  
IMPORTANCE_VISIBLE:  
IMPORTANCE_SERVICE:  
IMPORTANCE_BACKGROUND:  
IMPORTANCE_EMPTY:  
这几种优先级的回收顺序是 Empty process、Background process、Service process、Visible process、Foreground process。  
# 避免内存泄露，主要要遵循以下几点：  
　　第一：不要为Context长期保存引用(要引用Context就要使得引用对象和它本身的生命周期保持一致)。  
　　第二：如果要使用到Context，尽量使用ApplicationContext去代替Context，因为ApplicationContext的生命周期较长，引用情况下不会造成内存泄露问题  
　　第三：在你不控制对象的生命周期的情况下避免在你的Activity中使用static变量。尽量使用WeakReference去代替一个static。  
　　第四：垃圾回收器并不保证能准确回收内存，这样在使用自己需要的内容时，主要生命周期和及时释放掉不需要的对象。尽量在Activity的生命周期结束时，在onDestroy中把我们做引用的其他对象做释放，比如：cursor.close()。  
# 良好的编码规范  
1、尽量不要new很大的object，大对象（>=85000Byte）直接归为G2代，GC回收算法从来不对大对象堆（LOH）进行内存压缩整理，因为在堆中下移85000字节或更大的内存块会浪费太多CPU时间  
2、不要频繁的new生命周期很短object，这样频繁垃圾回收频繁压缩有可能会导致很多内存碎片，可以使用设计良好稳定运行的对象池（ObjectPool）技术来规避这种问题  
3、使用更好的编程技巧，比如更好的算法、更优的数据结构、更佳的解决策略等等  
# 图片回收  
android的bitmap回收策略【使用ｒｅｃｙｃｌｅ】：  
bitmap的recycle函数的调用还是可以是有必要的，理由有：  
a. 垃圾回收虽然好使，但是有可能的话，我们还是让它少干点活吧。垃圾回收有很大的未来不确定性，会加重未来未知时间点的loading，若有大量bitmap需要垃圾回收处理，那必然垃圾回收需要做的次数就更多也发生地更频繁，小心会造成ANR。但是，若是自己recycle，就可以可控制地分散处理了这些回收任务了。  
b. 若是launcher那样一直运行的application，它的process一直存在，memory问题还是多多注意下比较好。  
－－  
和android版本相关的策略：  
android3.0以后不必调用recycle(),因为3.0之后图片资源是存放在java堆中的，这时直接置空即可，GC会回收，  
android3.0之前必须调用recycle()，3.0之前bitmap数据存放在c堆中，不调用recycle()如果是被强引用持有，就一直不会回收，要是直接置空，就无法对c堆中的资源进行回收了。要看具体版本。  
# 减少GC开销的措施
　　根据上述GC的机制,程序的运行会直接影响系统环境的变化,从而影响GC的触发。若不针对GC的特点进行设计和编码,就会出现内存驻留等一系列负面影响。为了避免这些影响,基本的原则就是尽可能地减少垃圾和减少GC过程中的开销。具体措施包括以下几个方面:
　　(1)不要显式调用System.gc()  
　　此函数建议JVM进行主GC,虽然只是建议而非一定,但很多情况下它会触发主GC,从而增加主GC的频率,也即增加了间歇性停顿的次数。  
　　(2)尽量减少临时对象的使用  
　　临时对象在跳出函数调用后,会成为垃圾,少用临时变量就相当于减少了垃圾的产生,从而延长了出现上述第二个触发条件出现的时间,减少了主GC的机会。  
　　(3)对象不用时最好显式置为Null  
　　一般而言,为Null的对象都会被作为垃圾处理,所以将不用的对象显式地设为Null,有利于GC收集器判定垃圾,从而提高了GC的效率。  
　　(4)尽量使用StringBuffer,而不用String来累加字符串(详见blog另一篇文章JAVA中String与StringBuffer)  
　　由于String是固定长的字符串对象,累加String对象时,并非在一个String对象中扩增,而是重新创建新的String对象,如 Str5=Str1+Str2+Str3+Str4,这条语句执行过程中会产生多个垃圾对象,因为对次作“+”操作时都必须创建新的String对象,但这些过渡对象对系统来说是没有实际意义的,只会增加更多的垃圾。避免这种情况可以改用StringBuffer来累加字符串,因StringBuffer 是可变长的,它在原有基础上进行扩增,不会产生中间对象。
　　(5)能用基本类型如Int,Long,就不用Integer,Long对象  
　　基本类型变量占用的内存资源比相应对象占用的少得多,如果没有必要,最好使用基本变量。  
　　(6)尽量少用静态对象变量  
　　静态变量属于全局变量,不会被GC回收,它们会一直占用内存。  
　　(7)分散对象创建或删除的时间  

# Frame动画
http://blog.csdn.net/feng88724/article/details/6320507
# Tween动画
http://blog.csdn.net/feng88724/article/details/6318430

# 隐藏桌面图标
apk安装后不显示图标会造成应用无法启动。
设置方式：
1.打开Activity的配置，在Intent处增加
 <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
               	<data android:host="类名" android:scheme="当前应用包名"/>
            </intent-filter>
2.在Activity标签中增加android:excludeFromRecents="true" android:theme="@android:style/Theme.NoDisplay" 

以上两步即可实现应用程序不创建桌面图标。

