import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.PredefinedKonanTargets.getByName

plugins {
    kotlin("jvm") version "1.3.31"
}

group = "uk.co.davwil00"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

//    register("resolveDependencies") {
//        doLast {
//            fun resolve(configurations: ConfigurationContainer) {
//                configurations.filter { it.isCanBeResolved }
//                    .forEach { it.resolve() }
//            }
//            project.rootProject.allprojects.forEach {
//                resolve(it.buildscript.configurations)
//                resolve(it.configurations)
//            }
//        }
//    }
}