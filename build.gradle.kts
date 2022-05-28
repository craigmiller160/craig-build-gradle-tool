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
}

configure<SpotlessExtension> {
    kotlin {
        ktfmt()
    }
}

dependencies {
    val jacksonVersion = "2.13.3"

    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}