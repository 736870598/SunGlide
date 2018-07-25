# SunGlide
手写 Glide 核心框架

    Glide.with(parent.getContext()).load(url).into(imageView);

    其中：

    第一步：with 创建或查找fragment，通过fragment知晓使用图片的界面的生命周期，
            方便图片管理，如果没有任何在使用这张图片，那么就把它从活动缓存中拿出
            放到内存缓存中去。
            如果在with的时候使用的application的context，或者在非主线程中调用，
            那么是无法知晓生命周期的，这样的话图片一直在活动缓存中，而且引用个数
            不会减少。这样的图片多了可能导致oom

    第二步：load 封装请求地址，并且找到合适的modelLoader等待处理

    第三步：into 开始工作。先从缓存中读取图片，之后是内存，之后是本地文件缓存，最后是网络或服务。。。


    另外可以发现：recycleView的缓存比ListView的缓存要好一点，但是在只有简单的单
                一类型时候还是建议使用listview，效率要高一点点。。。。





