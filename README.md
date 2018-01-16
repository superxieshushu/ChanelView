## ChanelView
废话不多说，请看效果图。

|              Chanel App效果图               |                  我实现的效果                  |
| :--------------------------------------: | :--------------------------------------: |
| ![Chanel App Gif](https://ws4.sinaimg.cn/large/006tNc79gy1fnhp10d938g304p08wawk.gif) | ![ Chanel View ](https://ws2.sinaimg.cn/large/006tNc79gy1fnhp4kt5zig304p08w40y.gif) |

有兴趣的可以自己去下载一下 Chanel 的 App 来把玩一下，里面有一些效果还是不错的；不过这个App 启动的时候很慢不知道是不是网络的问题。最开始我看到这个效果是一脸问号。效果是从这个 App 里面反编译出来的代码找出来的。

## 实现原理介绍
采用的是 ListView 来实现视差效果。
1. ListView 滑动的时候动态盖改变 Item 的高度来达到视差的效果。
2. ListView 设置 OnScrollListener 监听滚动事件，配合 item 的 parallax 方法实现滑动视差效果。
3. ListView 设置 OnTouchListener 来处理向上滑动或者向下滑动的判断，调用gotoPrev 或者 gotoNext 方法；最终出发 OnScrollListener 回调，完成 swipe 的视差效果。

## TODO
1. 点击 Item 视差效果 

## 求星星，求分享
有问题可以提issue，欢迎大家一起来完善效果以及交流想法。
