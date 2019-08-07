## Android曝光统计
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)

曝光统计：对在屏幕中出现的view进行统计，便于分析用户偏好，提高用户留存率
  
## 主要特点
1. 调用者简单的配置曝光规则，即可以拿到曝光的view集合
2. 可设置有效曝光视图显示的范围，范围：0-100  例：80代表该视图显示自身总面积的80%以上都可以算作是有效曝光的视图
3. 可设置曝光延时，即view出现在屏幕中一段时间后可触发统计事件
4. 可设置曝光去重
5. 可设置自动曝光，即打开了页面但用户未执行其他任何操作情况下会自动执行一次延时曝光任务
6. 支持曝光动态插入的view
7. 统计结果在类库初始化时以回调的方式交由调用者处理
## 使用
1. App下的build.gradle添加依赖类库  [ ![Download](https://api.bintray.com/packages/kwunyamshan/maven/Android-Exposure-Stat/images/download.svg) ](https://bintray.com/kwunyamshan/maven/Android-Exposure-Stat/_latestVersion)
```Java
    //版本号:x.x.x替换Download中的版本号
    implementation 'com.wh.repo:Android-Exposure-Stat:x.x.x'
```
2. Project下的build.gradle添加仓库地址
```Java
buildscript {
    repositories {
        maven { url"https://dl.bintray.com/kwunyamshan/maven" }    
        ...
    }
    dependencies {
        //gradle版本与相应插件版本的对应关系 3.4.0+  —>  5.1.1+
        classpath 'com.android.tools.build:gradle:3.4.1'
        ...
    }
}

allprojects {
    repositories {
        maven { url"https://dl.bintray.com/kwunyamshan/maven" }
        ...
    }
}
```
3. 在Application类中初始化,需要实现com.wh.stat.lifecycle.IContext接口
```Java
public class App extends Application implements IContext {
    private static final String TAG = App.class.getSimpleName();
    public static int mStatTagId = R.id.mark;
    
    @Override
    public void onCreate() {
        super.onCreate();
        //Application中初始化，Application需要实现IContext
        new StatBuilder(this)
                //对每个需要曝光统计的View以setTag的方式进行标识，标识需自行定义
                .setTagId(mStatTagId)
                //设置时长:view显示在页面中x毫秒后算一次有效曝光
                .setDuration(5000)
                //设置有效曝光视图显示的范围，范围：0-100  例：80代表该视图显示自身总面积的80%以上都可以算作是有效曝光的视图
                .setValidRange(80)
                //设置是否为线上版本，目前的区别就是是否需要打日志
                .setDebugModle(true)
                //是否需要自动执行曝光任务
                .setAutoStat(true)
                //是否可以被重复曝光
                .setRepeat(true)
                //返回已曝光的view集合
                .setViewResultListener(new HBHStatistical.ViewResultListener() {
                    @Override
                    public void onViewResult(ArrayList<View> displayViews) {
                        //displayViews不需要做非空判断
                        Iterator iterator = displayViews.iterator();
                        while (iterator.hasNext()) {
                            View view = (View) iterator.next();
                           
                            String mark = (String) view.getTag(mStatTagId);
                            
                            Log.e(TAG, "有效曝光：id:" + view.getId() + "     , 数据:" + mark);
                           
                            Toast.makeText(App.this, "曝光事件被触发，在控制台搜索“曝光统计”查看log", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
    }
}

```
4. 给需要上报的view设置tag
```Java
TextView tvHello = findViewById(R.id.tv_hello);

tvHello.setTag(App.mStatTagId,"Hello World");
```
5. 结果返回在onViewResult方法中，自行处理
```Java
public void onViewResult(ArrayList<View> displayViews) {

                        //不需要非空判断
                         Iterator iterator = displayViews.iterator();

                        while (iterator.hasNext()) {

                            View view = (View) iterator.next();

                            String mark = (String) view.getTag(mStatTagId);

                            Log.e(TAG, "有效曝光：id:" + view.getId() + "     , 数据:" + mark);
                            
                            Toast.makeText(App.this, "曝光事件被触发，在控制台搜索“曝光统计”查看log", Toast.LENGTH_SHORT).show();
                        }

                    }
```
