import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.stb.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies{
    compileOnly(libs.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("emptyPlugin") {
            id = "stb.emptyPlugin"
            implementationClass = "plugins.EmptyPlugin"
        }
    }
    plugins {
        register("appPlugin") {
            id = "stb.appPlugin"
            implementationClass = "plugins.AppPlugin"
        }
    }
}