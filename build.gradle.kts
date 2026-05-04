plugins {
    kotlin("jvm") version "2.3.20"
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

kotlin {
    jvmToolchain(25)
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

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")

}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Copy>("copyAllDependencies") {
    description = "copy dependencies to build directory"
    from(configurations.getByName("runtimeClasspath").map { if (it.isFile) it else it })

    into(layout.buildDirectory.dir("libs/dependencies"))

    rename { filename ->
        val originalFile = File(filename)
        val depName = originalFile.nameWithoutExtension
        "${depName}.${originalFile.extension}"
    }
}

val mergedClassesDir = layout.buildDirectory.dir("classes/kotlin/main")

tasks.register<Copy>("mergeKotlinClasses"){
    description = "merge module info file to destination"
    dependsOn(tasks.compileKotlin)
    from(tasks.compileKotlin.flatMap { it.destinationDirectory })
    into(mergedClassesDir)
    outputs.dir(mergedClassesDir)
}

tasks.compileJava {
    dependsOn(tasks.compileKotlin)
    destinationDirectory = mergedClassesDir
    inputs.dir(mergedClassesDir).withPropertyName("compileKotlin")
}

tasks.jar {
    dependsOn(tasks.compileJava, tasks.compileKotlin)
    from(mergedClassesDir)
}