import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    kotlin("jvm") version "1.6.20"
    id("com.diffplug.spotless") version "6.6.1"
}

group = "io.craigmiller160"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases")
    }
}

configure<SpotlessExtension> {
    kotlin {
        ktfmt()
    }
}

dependencies {
    val jacksonVersion = "2.13.3"
    val slf4jVersion = "1.7.36"
    val gradleToolingApiVersion = "7.4.2"

    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.gradle:gradle-tooling-api:$gradleToolingApiVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}