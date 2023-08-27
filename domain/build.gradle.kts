plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    annotationProcessor(project(":processor"))
    implementation(project(":processor"))
}


tasks.withType(JavaCompile::class) {
    doFirst {
        println("AnnotationProcessorPath for $name is ${options.annotationProcessorPath?.files})")
    }
}