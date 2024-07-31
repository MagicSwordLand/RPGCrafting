import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

group = "net.brian"
version = "0.0.1"

val kotlinVersion = "1.5.20"

plugins {
    java
    kotlin("jvm") version "1.5.20"
        id("com.github.johnrengelman.shadow") version "7.0.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
    kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=compatibility")
}

repositories {
    mavenCentral()
    maven { url  = URI.create("https://jitpack.io") }
    maven { url = URI.create("https://hub.spigotmc.org/nexus/content/repositories/snapshots") }
    maven { url = URI.create("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven {
        name = ("nyaacat-repo")
        url = URI.create("https://ci.nyaacat.com/maven/")
    }
}

dependencies {
    compileOnly("dev.reactant:reactant:0.2.3")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    // Load jar libraries from "libs" folder
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    /** The external library you would like to use */
    /** implementation("...")    */
}


val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
val shadowJar = (tasks["shadowJar"] as ShadowJar).apply {
	exclude("kotlin*")
    //relocate("org.from.package", "org.target.package")
}

val deployPlugin by tasks.registering(Copy::class) {
    dependsOn(shadowJar)
    System.getenv("PLUGIN_DEPLOY_PATH")?.let {
        from(shadowJar)
        into(it)
    }
}

val build = (tasks["build"] as Task).apply {
    arrayOf(
            sourcesJar
                , shadowJar
                , deployPlugin
    ).forEach { dependsOn(it) }
}

