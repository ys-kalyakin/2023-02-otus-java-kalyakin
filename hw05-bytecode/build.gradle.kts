plugins {
    java
}

dependencies {
    implementation("org.ow2.asm:asm-commons")
}

// необходимо для получения названий параметров метода через рефлексию
//tasks.withType<JavaCompile> {
//    val compilerArgs = options.compilerArgs
//    compilerArgs.addAll(listOf("-parameters"))
//}