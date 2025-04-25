plugins {
    application
    java
    jacoco
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "ru.nsu.chuvashov"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("ru.nsu.chuvashov.MainKt") // Set the main class
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

javafx {
    version = "23.0.2"
    modules = listOf("javafx.controls")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")

    implementation("org.openjfx:javafx-fxml:21")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "--module-path", configurations.runtimeClasspath.get().asPath,
        "--add-modules", "javafx.controls,javafx.fxml"
    )
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}