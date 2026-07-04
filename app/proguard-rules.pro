# Proguard rules for DharmaFeed

# Kotlin Serialization Rules
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep serializable classes and companion members
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
}
-keep class kotlinx.serialization.json.** { *; }
