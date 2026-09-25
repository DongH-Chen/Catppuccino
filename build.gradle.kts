plugins {
    kotlin("jvm") version "2.4.20"
    application
}

group = "dev.cdh"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass = "dev.cdh.MainKt"
    mainModule = "dev.cdh"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(25)
}

tasks.jar {
    group = "build"

    manifest {
        attributes(
            "Main-Class" to "dev.cdh.MainKt",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "dev.cdh",
            "Created-By" to "Gradle ${gradle.gradleVersion}",
            "Built-By" to System.getProperty("user.name"),
            "Build-Jdk" to System.getProperty("java.version")
        )
    }

    from(sourceSets.main.get().output)
    val dependencies = configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

tasks.test {
    useJUnitPlatform()
}
