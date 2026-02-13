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

tasks.test {
    useJUnitPlatform()
}