import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.diffplug.gradle.spotless.SpotlessExtension

val projectMainClass = "io.craigmiller160.craigbuild.gradle.tool.RunnerKt"

plugins {
    kotlin("jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.6.1"
    `maven-publish`
    application
}

group = "io.craigmiller160"
version = "1.0.4"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases")
    }
    maven {
        url = uri("https://nexus-craigmiller160.ddns.net/repository/maven-public")
    }
}

application {
    mainClass.set(projectMainClass)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            from(components["kotlin"])
        }
    }
    repositories {
        maven {
            val repo = if (project.version.toString().endsWith("-SNAPSHOT")) "maven-snapshots" else "maven-releases"
            url = uri("https://nexus-craigmiller160.ddns.net/repository/$repo")
            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
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
    val craigBuildGradlePluginVersion = "1.0.1"
    val arrowKtVersion = "1.1.2"

    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.gradle:gradle-tooling-api:$gradleToolingApiVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("io.craigmiller160:craig-build-gradle-plugin:$craigBuildGradlePluginVersion")
    implementation("io.arrow-kt:arrow-core:$arrowKtVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
            "Main-class" to projectMainClass
        ))
    }

    from(configurations.compileClasspath.get()
        .map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
