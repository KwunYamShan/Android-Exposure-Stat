## Android曝光统计
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/kwunyamshan/maven/Android-Exposure-Stat/images/download.svg) ](https://bintray.com/kwunyamshan/maven/Android-Exposure-Stat/_latestVersion)

曝光统计：对在屏幕中出现的view进行统计，便于分析用户偏好，提高用户留存率
  
## 主要特点
1. 调用者简单的配置曝光规则，即可以拿到曝光的view集合
2. 可设置曝光的屏幕范围
3. 可以在view的Visible状态，并且该view与屏幕有交集时触发统计事件
4. 可设置曝光时长，曝光多久后触发统计事件
5. 统计结果在类库初始化时以回调的方式交由调用者处理
## 使用
1. App下的build.gradle添加依赖类库
```Java
    implementation 'com.wh.repo:Android-Exposure-Stat:1.0.1'
```
2. Project下的build.gradle添加仓库地址
```Java
buildscript {
    repositories {
        maven { url"https://dl.bintray.com/kwunyamshan/maven" }    
        ...
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.4.1'
        
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        maven { url"https://dl.bintray.com/kwunyamshan/maven" }
        ...
    }
}
```
3. 在Application类中初始化,需要继承IContext
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

                //设置曝光的屏幕范围
                 .setSubRange(0,0,screenWidth,screenHeight)

                //设置是否为线上版本，目前的区别就是是否需要打日志
                 .setDebugModle(true)

                //返回有效曝光的view集合
                 .setViewResultListener(new HBHStatistical.ViewResultListener() {

                    @Override

                    public void onViewResult(ArrayList<View> displayViews) {

                        //不需要非空判断
                         Iterator iterator = displayViews.iterator();

                        while (iterator.hasNext()) {

                            View view = (View) iterator.next();

                            String mark = (String) view.getTag(mStatTagId);

                            Log.e(TAG, "有效曝光：id:" + view.getId() + "     , 数据:" + mark);

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

                        }

                    }
```
