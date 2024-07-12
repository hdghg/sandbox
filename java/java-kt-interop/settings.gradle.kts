@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

rootProject.name = "java-kt-interop"

include("jlib", "app")
