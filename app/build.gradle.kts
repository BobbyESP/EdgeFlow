plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.bobbyesp.edgeflow"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bobbyesp.edgeflow"
        minSdk = 26
        targetSdk = 36

        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    applicationVariants.all {
        val variantName = name
        sourceSets {
            getByName("main") {
                java.srcDir(File("build/generated/ksp/$variantName/kotlin"))
            }
        }
        outputs.all {
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                "EdgeFlow-${defaultConfig.versionName}-${name}.apk"
        }
    }
}

ksp {
    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
    arg("KOIN_CONFIG_CHECK", "true")
}


secrets {
    propertiesFileName = "secrets.properties"
}

dependencies {
    implementation(libs.bundles.core)
    implementation(libs.bundles.coroutines)
    implementation(project(":imagingedge"))

    implementation(libs.bundles.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.scrollbar)
    implementation(libs.sonner)
    implementation(libs.haze)
    implementation(libs.multiplatform.markdown.renderer.android)
    implementation(libs.multiplatform.markdown.renderer.m3)
    implementation(libs.bobbyesp.foundation)
    implementation(libs.splashscreen)

    implementation(libs.bundles.nav3)

    implementation(libs.landscapist.coil)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    annotationProcessor(libs.room.compiler)

    implementation(libs.bundles.dependencyInjection)

    implementation(libs.bundles.ktor)

    implementation(libs.bundles.serialization)
    implementation(libs.xmlutil)

    implementation(libs.qrcode.kotlin.android)

    //Testing and tooling
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

class RoomSchemaArgProvider(
    @get:InputDirectory @get:PathSensitive(PathSensitivity.RELATIVE) val schemaDir: File
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        if (!schemaDir.exists()) {
            schemaDir.mkdirs()
        }
        return listOf("room.schemaLocation=${schemaDir.path}")
    }
}