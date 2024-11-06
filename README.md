ShadowLogPlugin
====

FileLogger是把日志写入文件的工具类


使用方式有两种


第一种 
直接拷贝FileLogger到项目中，在application的onCreate时调用init方法传入context初始化
然后就可以调用相应的方法向文件写入日志了，调用方法和android.log.Log类似
文件目录在context.getExternalCacheDir



第二种 以插件的方式集成（开发中）
此方式不用调用FileLogger,开发着依旧调用android.log.Log
插件会在编译期识别android.log.Log自动在相应处以AOP方式织入FileLogger调用代码


第一步
在根build.gradle中加入下面代码

```groovy
buildscript {
  dependencies {
    classpath 'com.invensun.seven:shadow-log:1.0.0'
  }
}

```
第二步
在根build.gradle中添加依赖
```groovy
buildscript {
    dependencies {
        classpath 'com.invensun.seven:shadow-log:1.0.0'
    }
}

apply plugin: 'com.android.application'
apply plugin: 'com.seveninvensun.log.lib.ShadowLogPlugin'
```