# YouJoin-Android
## 简介
这个repo是YouJoin社交平台的Android客户端，代码由本人独立编写，功能和特点包括：
 - 采用Material Design设计
 - 登录注册
 - 即时聊天
 - 个人中心（支持资料编辑、头像上传）
 - 心情动态（支持最多九张图片；支持点赞、评论）
 - 好友关注
 - 附近的人（采用**特征点匿名匹配算法**，服务器不了解客户端真实地理位置，保护用户隐私）
 - 支持emoji表情（原创emoji表情绘制中）
 - 插件扩展（开发中） 
  
项目因兴趣而生，非商业项目。目前还在开发中……  
 
## 运行截图
 - 欢迎页  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-welcome.png?imageView/2/w/400)  
 - 登录页  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-login.png?imageView/2/w/400)  
 - 动态列表  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-tweetslist.png?imageView/2/w/400)  
 - 动态详情  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-tweetdetail.png?imageView/2/w/400)  
 - 抽屉菜单  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-menu.png?imageView/2/w/400)  
 - 动态发表  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-publish.png?imageView/2/w/400)  
 - 个人资料  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-info.png?imageView/2/w/400)  
 - 资料编辑  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-infoedit.png?imageView/2/w/400)  
 - 消息列表  
 ![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-msglist.png?imageView/2/w/400)  
 - 好友列表  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-friendlist.png?imageView/2/w/400)  
 - 附近的人  
 ![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-around.png?imageView/2/w/400)  
 - 私信聊天  
![](http://7vzrj0.com1.z0.glb.clouddn.com/youjoin-android-1-chat.png?imageView/2/w/400)  
 
## 开源库依赖
 - [butterknife](https://github.com/JakeWharton/butterknife)
 - [circleimageview](https://github.com/hdodenhof/CircleImageView)
 - [eventbus](https://github.com/greenrobot/EventBus)
 - [gson](https://github.com/google/gson)
 - [leakcanary](https://github.com/square/leakcanary)
 - [materialdaterangepicker](https://github.com/borax12/MaterialDateRangePicker)
 - [material-drawer-library](https://github.com/mikepenz/MaterialDrawer)
 - [multi-image-selector](http://git.oschina.net/ant/MultiImageSelector)
 - [nineoldandroids](https://github.com/JakeWharton/NineOldAndroids)
 - [pinyin4j](https://sourceforge.net/projects/pinyin4j)
 - [picasso](https://github.com/square/picasso)
 - [stickylistheaders](https://github.com/emilsjolander/StickyListHeaders)
 - [spinkit](https://github.com/ybq/Android-SpinKit)
 - [systembartint](https://github.com/jgilfelt/SystemBarTint)
 - [volley](https://android.googlesource.com/platform/frameworks/volley)
 
## 即时通讯
 - LeanCloud（上述开源项目中未包含LeanCloud SDK中的jar）

## 联系方式
freedomzzq#tekbroaden.com

## DESIGN-LICENSE
Design by lianghongxue 不得以任何方式使用应用中的原创图片、图标资源，包括但不限于emoji以及引导图等。上述资源版权归YouJoin Studio所有。

## CODE-LICENSE
The MIT License (MIT)

Copyright (c) 2016 YouJoin Studio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
