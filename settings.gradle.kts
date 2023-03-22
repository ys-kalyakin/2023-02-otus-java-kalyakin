rootProject.name = "2023-02-otus-java-kalyakin"
include("hw01-gradle")
include("hw02-generics")
include("hw03-annotations")

pluginManagement {
    val johnrengelmanShadow: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
    }
}
