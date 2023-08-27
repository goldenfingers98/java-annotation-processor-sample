plugins {
    id("java-library")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    annotationProcessor(group = "com.google.auto.service", name = "auto-service", version = "1.0")
    compileOnly(group = "com.google.auto.service", name = "auto-service", version = "1.0")
}