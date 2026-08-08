plugins {
    id("java")
}

group = "me.zortex"
version = project.version

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("com.github.retrooper:packetevents-spigot:2.13.0")
}

val targetJavaVersion = 21
val buildJavaVersion = 25

java {
    val buildVersion = JavaVersion.toVersion(buildJavaVersion)
    sourceCompatibility = buildVersion
    targetCompatibility = buildVersion

    if (JavaVersion.current() < buildVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(buildJavaVersion))
    }

    disableAutoTargetJvm()
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)

            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }
}