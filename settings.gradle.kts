rootProject.name = "2023-02-otus-java-kalyakin"

include("hw01-gradle")
include("hw02-generics")
include("hw03-annotations")
include("hw04-gc")
include("hw05-bytecode")
include("hw06-oop")
include("hw07-patterns")
include("hw08-serialization")
include("hw09-jdbc:demo")
include("hw09-jdbc:homework")
include("hw10-jpql")
include("hw11-cache")
include("hw12-webserver")
include("hw13-ioc")
include("hw14-spring-data")
include("hw15-concurrent-collections")
include("hw16-grpc")

pluginManagement {
    val johnrengelmanShadow: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val lombok: String by settings
    val protobufVer: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
        id("io.freefair.lombok") version lombok
        id("com.google.protobuf") version protobufVer
    }
}