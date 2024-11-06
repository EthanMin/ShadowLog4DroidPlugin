ShadowLogPlugin
====

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