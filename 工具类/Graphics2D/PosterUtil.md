## 海报生成工具类

### 背景

前端生成图片较为麻烦，采用后端生成一张图片返回前端

### 使用技术

java原生类：Graphics2D

### 使用

封装了几个使用到的方法在该目录下的java类中

### 遇到的问题

1. 小程序码方图截取圆形图缺失右下角

   解决方案:在原图加个透明背景 加border

2. 字体问题，中文字在服务器商无法画出

   解决方案：在服务器上安装字体包（1.字体文件放到jre/lib/fonts/下          2.服务器安装字体）

### 服务器安装字体

cd /usr/share/fonts/

把字体文件放到该目录

mkfontscale

mkfontdir

fc-cache

验证       fc-list :lang=zh





