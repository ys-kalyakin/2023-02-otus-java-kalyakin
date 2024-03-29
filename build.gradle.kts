import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.gradle.api.plugins.JavaPluginExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    idea
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
    id("org.springframework.boot") apply false
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(17)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

allprojects {
    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val guava: String by project
    val testcontainersBom: String by project
    val grpc: String by project
    val wiremock: String by project
    val r2dbcPostgresql: String by project
    val sockjs: String by project
    val stomp: String by project
    val bootstrap: String by project
    val springDocOpenapiUi: String by project
    val jsr305: String by project
    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
            }
            dependency("com.google.guava:guava:$guava")
            dependency("org.ow2.asm:asm-commons:9.2")
            dependency("com.google.protobuf:protobuf-java-util:3.22.3")
            dependency("org.glassfish:jakarta.json:2.0.1")
            dependency("javax.json:javax.json-api:1.1.4")
            dependency("commons-beanutils:commons-beanutils:1.9.4")
            dependency("org.reflections:reflections:0.10.2")
            dependency("io.grpc:grpc-netty:$grpc")
            dependency("io.grpc:grpc-protobuf:$grpc")
            dependency("io.grpc:grpc-stub:$grpc")
            dependency("com.github.tomakehurst:wiremock:$wiremock")
            dependency("io.r2dbc:r2dbc-postgresql:$r2dbcPostgresql")
            dependency("org.webjars:sockjs-client:$sockjs")
            dependency("org.webjars:stomp-websocket:$stomp")
            dependency("org.webjars:bootstrap:$bootstrap")
            dependency("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenapiUi")
            dependency("com.google.code.findbugs:jsr305:$jsr305")
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing", "-Werror"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }

    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    apply(plugin = "io.freefair.lombok")
}

