# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn

# 3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
# 定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
# 搜索
-keep   class com.amap.api.services.**{*;}
# 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

#bmob
#-libraryjars libs/BmobPay_v3.2.3_171113.jar
#-keepclasseswithmembers class c.b.** { *; }
#-keep interface c.b.PListener{ *; }
#-keep interface c.b.QListener{ *; }

-ignorewarnings

-keepattributes Signature,*Annotation*

# keep BmobSDK
-dontwarn cn.bmob.v3.**
-keep class cn.bmob.v3.** {*;}

# 确保JavaBean不被混淆-否则gson将无法将数据解析成具体对象
-keep class * extends cn.bmob.v3.BmobObject {
    *;
}

# keep BmobPush
-dontwarn  cn.bmob.push.**
-keep class cn.bmob.push.** {*;}

# keep okhttp3、okio
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.**

# keep rx
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# 如果你需要兼容6.0系统，请不要混淆org.apache.http.legacy.jar
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}

# otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements java.io.Serializable { *; }
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#The below is used for AdView SDK settings,only instead for your app
-dontwarn

#-keep public class com.kyview.** {*;}
#-keep public class com.kuaiyou.** {*;}

-keepclassmembers class * {public *;}
-keep public class * {public *;}
-keep public class com.adwo.adsdk.AdwoAdBrowserActivity
-keep public class com.wooboo.** {*;}
-keep public class cn.aduu.android.**{*;}
-keep public class com.wqmobile.** {*;}
-keep class com.baidu.mobads.** {
  public protected *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.ads.** {
   public *;
}
-keep public class com.millennialmedia.android.* {
	<init>(...);
	public void *(...);
	public com.millennialmedia.android.MMJSResponse *(...);
}

-keep class com.qq.e.** {
   public protected *;
   }
   -keep class com.tencent.gdt.**{
   public protected *;
   }

-dontoptimize
-dontwarn
-keep class com.mobisage.android.** {*;}
-keep interface com.mobisage.android.** {*;}
-keep class com.msagecore.** {*;}
-keep interface com.msagecore.** {*;}

-keep class com.five.adwoad.** {*;}

-keep class com.emar.escore.sdk.ui.**{*;}

-keep class com.inmobi.**{ *; }

-keep public class cn.waps.** {*;}
-keep public interface cn.waps.** {*;}

-keep class com.adzhidian.** { *; }

-keep public class cn.immob.sdk.** {*;}
-keep public class cn.immob.sdk.controller.** {*;}

-keep class com.guohead.mix.*{ *; }

-keep public class cn.aduu.android.**{*;}

-keep class com.otomod.ad.** {*;}
-keep class org.OpenUDID.** {*;}

#使用 触控 广告时使用
-keepattributes Exceptions
-keepattribute Signature
-keepattribute Deprecated
-keepattributes *Annotation*
#-dontwarn com.chance.**
#-dontwarn com.android.volley.NetworkDispatcher
-flattenpackagehierarchy com.chance.v4

-keep class * extends com.chance.ads.Ad {
public *;
}
-keep class com.chance.ads.AdActivity {
public *;
}
-keep class com.chance.recommend.RecommendActivity {
public *;
}
-keep class com.chance.ads.AdRequest {
public *;
}
-keep class com.chance.ads.MoreGameButton {
public *;
}
-keep class com.chance.ads.OfferWallButton {
public *;
}
-keep class com.chance.ads.RecommendButton {
public *;
}
-keep class com.chance.ads.OfferWallData {
public *;
}
-keep class com.chance.ads.OfferWallAdInfo {
public *;
}
-keep class com.chance.ads.OfferWallAdDetail {
public *;
}
-keep class com.chance.response.TaskInfo {
public *;
}
-keep class com.chance.exception.PBException {
public *;
}
-keep class com.chance.listener.AdListener {
public *;
}
-keep class com.chance.listener.PointsChangeListener {
public *;
}
-keep class com.chance.listener.QueryPointsListener {
public *;
}
-keep class com.chance.listener.GetAdDetailListener {
public *;
}
-keep class com.chance.listener.GetAdListListener {
public *;
}
-keep class * extends android.app.Service {
public *;
}
-keep class com.chance.report.ReportData {
public *;
}
-keep class com.chance.engine.DownloadData {
public *;
}
-keep class com.chance.engine.DownloadSubData {
public *;
}
-keep class com.chance.data.DownloadCompleteInfo {
public *;
}
-keep class com.chance.data.AppInfo {
public *;
}
-keep class com.chance.util.PBLog {
public *;
}
-keep class com.chance.recommend.** {*;}
-keep class com.chukong.android.crypto.** {*;}
-keep class com.chance.d.** {*;}

#触控 混淆结束

-keep class com.suizong.mobile.** {*;}
-keep class com.go2map.mapapi.** {*;}

-keep public class cn.Immob.sdk.** {*;}
-keep public class cn.Immob.sdk.controller.** {*;}

-keep class net.youmi.android.** {*;}

-keeppackagenames cn.smartmad.ads.android
-keeppackagenames I
-keep class cn.smartmad.ads.android.* {*;}
-keep class I.* {*;}


-keep public class MobWin.*
-keep public class MobWin.cnst.*
-keep class com.tencent.lbsapi.*
-keep class com.tencent.lbsapi.core.*
-keep class LBSAPIProtocol.*
-keep class com.tencent.lbsapi.core.QLBSJNI {
*;
}

-keeppackagenames com.adchina.android.ads
-keeppackagenames com.adchina.android.ads.controllers
-keeppackagenames com.adchina.android.ads.views
-keeppackagenames com.adchina.android.ads.animations
-keep class com.adchina.android.ads.*{*;}
-keep class com.adchina.android.ads.controllers.*{*;}
-keep class com.adchina.android.ads.views.*{*;}
-keep class com.adchina.android.ads.animations.*{*;}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class com.mediav.** {*;}
-keep class org.adver.score.**{*;}

-keep class com.easou.ecom.mads.** {
  public protected *;
}

-keep class com.adroi.sdk.** {
  public protected *;
}
-keep class com.mopanspot.sdk.** {
*;
}
-keep class com.imopan.plugin.spot.** {
*;
}
-keep class com.jd.**{
*;
}
-keep class cn.pro.ad.sdk.*
#讯飞
-keep class com.iflytek.voiceads.**{*;}
