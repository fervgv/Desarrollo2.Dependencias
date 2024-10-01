
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false // Firebase
    id("com.google.dagger.hilt.android") version "2.48" apply false // Hilt

}


