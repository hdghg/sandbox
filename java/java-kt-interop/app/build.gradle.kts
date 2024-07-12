import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("org.springframework.boot").version("3.3.1")
    id("org.jetbrains.kotlin.jvm").version("1.9.24")
    id("org.jetbrains.kotlin.plugin.spring").version("1.9.24")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    implementation(project(":jlib"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=ignore")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
