# DiabetesTracer-Custom-Watchface
Android wear custom watchface for Diabetes Tracer App. 
This application creating a custom android watch face that helps people with diabetes. The watch face show the time left for the assigned next insulin dose. 
The final watch face will look like this. 

![screenshot_1480094157](https://cloud.githubusercontent.com/assets/4669285/20643861/d824843a-b41b-11e6-9aed-cff36381eef0.png) 
![screenshot_1480094564](https://cloud.githubusercontent.com/assets/4669285/20643862/d824c274-b41b-11e6-8240-4fcc5a0cbab4.png)

 Watchface in active mode                                                        Watchface in ambient mode. 
 
The watchfwace shows: 

1. Insulin Dose icon. (Green). 

2. Time left for next dose (Cyan). 

3. Current time. 

4. Current Date.


# IDE: 
The project runs on Android Studio 2.2.1. there are some info related to the project.

# Libraries and dependencies
In order for the watchface to run properly, it should be runing on google API 23.0 (with android wear) and with google play services installed. 
The following two lines should be added to build.gradle (of android wear) in dependancies section: 

    1. compile 'com.google.android.support:wearable:1.4.0' .
    
    2. compile 'com.google.android.gms:play-services-wearable:9.6.1'
    
Make sure that your that  targetSdkVersion and compileSdkVersion are 23 as well . 

# AndroidManifest.xml of android wear; 
Android watch is service that runs in the background of the android wear device, for that reason, it needs some permissions to be able to function. 

Make sure that manifest file is having the folloing lines in the application: 
### before application attribute;

       <uses-permission android:name="com.google.android.permission.PROVIDE_BACKGROUND" />
       
       <uses-permission android:name="android.permission.WAKE_LOCK" />
       
### Inside the application attribute; 
  
          <service
            android:name=".<name-of-your-class-service>"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_WALLPAPER">
            <meta-data
                android:name="android.service.wallpaper"
                android:resource="@xml/watch_face" />
            <meta-data
                android:name="com.google.android.wearable.watchface.preview"
                android:resource="@drawable/preview_rectangular" />

            <meta-data
                android:name="com.google.android.wearable.watchface.preview_circular"
                android:resource="@drawable/preview_circular" />

            <intent-filter>
                <action android:name="android.service.wallpaper.WallpaperService" />
                <category android:name="com.google.android.wearable.watchface.category.WATCH_FACE" />
            </intent-filter>
          </service>

The full manifest xml will look like this; 

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.omar.watchexample">

    <uses-feature android:name="android.hardware.type.watch" />

    <uses-permission android:name="com.google.android.permission.PROVIDE_BACKGROUND" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@android:style/Theme.DeviceDefault">
        <service
            android:name=".SimpleWatchFaceService"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_WALLPAPER">
            <meta-data
                android:name="android.service.wallpaper"
                android:resource="@xml/watch_face" />
            <meta-data
                android:name="com.google.android.wearable.watchface.preview"
                android:resource="@drawable/preview_rectangular" />

            <meta-data
                android:name="com.google.android.wearable.watchface.preview_circular"
                android:resource="@drawable/preview_circular" />

            <intent-filter>
                <action android:name="android.service.wallpaper.WallpaperService" />
                <category android:name="com.google.android.wearable.watchface.category.WATCH_FACE" />
            </intent-filter>
        </service>
    </application>


</manifest>

# Android Wear Watchface Core; 
As have mentioned before, Android were is a service that runs on the background of Android wear device.
CanvasWatchFaceService is the class service that can be extended and customized. 
It is the Base class for watch faces that draw on a Canvas. Provides an invalidate mechanism similar to invalidate().
In Project there are two main classes: 

1. Watchface.java 

  It contains all the methods of drawing and defining the position of the elements on canvas.
  
2. WatchfaceService.java 

  It extends the CanvasWatchFaceService and build the watchface Engine that responisle that overrides specific methods.

# Resources
## Images
Some images should be placed in res/drawable folder
1. dose_img.png (dose icon in the active mode) 

2. icon_dose_gray.png (dose icon in the gray mode ) 

3. preview_circular.png ( watch image in watchfaces studio for the circular shaped watches ) 

4. preview_rectangular.png ( watch image in the watchfaces studio for the rectangualr) . 

## xml files; 
you need place an xml file in res/xml to represent the watch face. layout of watch face will just contain the following: 

<?xml version="1.0" encoding="UTF-8"?>   <wallpaper />

## Values files;  
  you need to add dimens.xml file to res/values folder. It should conatain the following.
  
  
  
      <resources>
        <dimen name="dose_size">46dp</dimen>
        <dimen name="time_size">30dp</dimen>
        <dimen name="date_size">12dp</dimen>
      </resources>

end; 

 


