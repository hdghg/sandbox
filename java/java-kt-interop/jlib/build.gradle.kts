plugins {
    `java-library`
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

dependencies {
    implementation("org.jetbrains:annotations:13.0")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
}
