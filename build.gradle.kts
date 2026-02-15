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
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.register<Jar>("fatJar") {
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

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")

//    archiveClassifier.set("all")
}

//tasks.build {
//    dependsOn("fatJar")
//}

tasks.test {
    useJUnitPlatform()
}