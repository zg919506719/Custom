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

#<a href="http://www.cnblogs.com/cr330326/p/5534915.html"/>android的相关学习

想要实现多进程的功能也非常简单，只需要在AndroidManifest文件的应用程序组件中声明一个android:process属性就可以了，
<service android:name=".PlaybackService"  
         android:process=":background" />
这里指定的进程名是background，你也可以将它改成任意你喜欢的名字。
需要注意的是，进程名的前面都应该加上一个冒号，表示该进程是一个当前应用程序的私有进程。

如果真的出现了内存泄露，我们应该怎么定位到具体是哪里出的问题呢？
这就需要借助一个内存分析工具了，叫做Eclipse Memory Analyzer（MAT）。
我们需要先将这个工具下载下来，下载地址是：http://eclipse.org/mat/downloads.php。

viewstub需要的时候才进行加载布局http://blog.csdn.net/guolin_blog/article/details/43376527
所加载的布局是不可以使用<merge>标签的，因此这有可能导致加载出来的布局存在着多余的嵌套结构

#<a href="https://mp.weixin.qq.com/s?__biz=MzI3MDE0NzYwNA==&amp;mid=2651434039&amp;idx=1&amp;sn=32ea2abdb5ebfd95e64199cf2050eb36&amp;chksm=f128854cc65f0c5a02f2ee310f4dd1bcf75616bc871c7a5714184398b43870a88d06041091ce&amp;scene=0#wechat_redirect"/>android混淆