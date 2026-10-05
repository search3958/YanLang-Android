-keep public class com.sentaro.yanlang.MainActivity

-keep class com.google.firebase.analytics.connector.** { *; }

-keep class com.google.android.libraries.ads.mobile.sdk.** { *; }
-dontwarn com.google.android.libraries.ads.mobile.sdk.**

-keep class com.android.billingclient.api.** { *; }
-dontwarn com.android.billingclient.api.**

-keep class io.github.jan-tennert.supabase.auth.** { *; }
-dontwarn io.github.jan-tennert.supabase.auth.**

-keep class io.ktor.client.engine.** { *; }
-dontwarn io.ktor.**

-keep class coil.compose.** { *; }
-keep class coil.** { *; }
-dontwarn coil.**

-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keepclassmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class com.sentaro.yanlang.data.** { <init>(...); }
-keepclassmembers class com.sentaro.yanlang.ui.** { <init>(...); }
