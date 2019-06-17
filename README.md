# Android曝光统计
曝光统计：对在屏幕中出现的view进行统计
    优点:
    耦合度极低，在Application中注册与移除，不影响项目正常运行
    对封装的数据不关心，只按配置的规则返回绑定过并且曝光了的view，至于数据交由调用者自己去解析并上报
# 主要特点
1. 调用者简单的配置曝光规则，即可以拿到曝光的view集合
2. 曝光的view数据交由调用者自行解析处理
3. 可以在view的Visible状态，并且该view与屏幕有交集时触发
4. 可设置曝光时长，曝光多久后触发统计事件
5. 可设置曝光的屏幕范围
# 使用
1. App下的build.gradle添加依赖类库
```Java
    implementation 'com.wh.repo:statistical:1.2.8'
```
2. Project下的build.gradle添加仓库地址
```Java
buildscript {
    repositories {
        maven { url"http://192.168.11.215:8081/repository/android2/" }    
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
        maven { url"http://192.168.11.215:8081/repository/android2/" }
        ...
    }
}
```
1. 在Application类中初始化,需要继承IContext
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
2. 给需要上报的view设置tag
```Java
TextView tvHello = findViewById(R.id.tv_hello);

tvHello.setTag(App.mStatTagId,"Hello World");
```
3. 结果返回在onViewResult方法中，自行处理
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
