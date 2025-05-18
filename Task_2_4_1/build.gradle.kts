plugins {
    id("java")
    id("antlr")
    id("jacoco")
}

group = "ru.nsu.chuvashov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    antlr("org.antlr:antlr4:4.13.2")
    testImplementation("org.antlr:antlr4-runtime:4.13.2")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testImplementation("org.jsoup:jsoup:1.17.2")
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/sources/antlr/main") // Explicitly add generated sources
        }
    }
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"     // Лимит памяти для ANTLR
    arguments = listOf("-visitor", "-package", "com.example.parser")
    outputDirectory = file("$buildDir/generated/sources/antlr/main/com/example/parser")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}