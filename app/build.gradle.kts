plugins {
    id("com.android.application")
    //Added the google services gradle plug in
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.spotifyapp2340"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.spotifyapp2340"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        defaultConfig.manifestPlaceholders["redirectSchemeName"] = "com.example.spotifyapp2340"
        defaultConfig.manifestPlaceholders["redirectHostName"] = "auth"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.squareup.picasso:picasso:2.71828")
    //implement picasso to load images from url
    implementation ("androidx.cardview:cardview:1.0.0")
    //implement CardView for layout
    implementation ("com.spotify.android:auth:2.1.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    //TODO: Add the dependencies for Firebase products you want to use
    //When using the BoM, don't specify versions in Firebase dependencies

    // implementation("com.google.firebase:firebase-analytics")

    implementation("androidx.appcompat:appcompat:1.6.1")
    //implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.firebase:firebase-firestore:24.11.0")
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("com.google.android.material:material:1.11.0")
    //updated version from 10.3 to 11.0
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}