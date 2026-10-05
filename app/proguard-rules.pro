# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
#
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
# -keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
# }
#
# Keep line numbers for crash reports only where needed.
-keepattributes Exceptions,RuntimeVisibleAnnotations,AnnotationDefault,InnerClasses
-keepattributes Signature
-keepclassmembers class * extends androidx.lifecycle.ViewModel { *; }
# Keep model classes used by serialization and Firebase
-keep class com.example.model.** { *; }
-keep class com.example.data.** { *; }
-keep class com.example.network.** { *; }
# Keep generated app classes for debugging and diagnostics
-keep class com.example.** { *; }
