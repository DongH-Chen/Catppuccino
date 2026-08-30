plugins {
    id("java")
    application
}

group = "dev.cdh"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass = "dev.cdh.Main"
    mainModule = "dev.cdh"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.jar {
    group = "build"

    manifest {
        attributes(
            "Main-Class" to "dev.cdh.Main",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "dev.cdh",
            "Created-By" to "Gradle ${gradle.gradleVersion}",
            "Built-By" to System.getProperty("user.name"),
            "Build-Jdk" to System.getProperty("java.version")
        )
    }

    from(sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

tasks.test {
    useJUnitPlatform()
}
