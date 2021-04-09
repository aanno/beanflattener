plugins {
    java
    application
}

val autoServiceVersion = project.property("autoServiceVersion")

group = "com.github.aanno.beanflattener"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://www.baeldung.com/java-annotation-processing-builder
    // https://github.com/google/auto
    implementation("com.google.auto.service:auto-service:${autoServiceVersion}")
    annotationProcessor("com.google.auto.service:auto-service:${autoServiceVersion}")

    implementation("com.google.guava:guava:30.1.1-jre")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

// https://docs.gradle.org/current/userguide/gradle_wrapper.html
tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "6.8.3"
}

// https://docs.gradle.org/current/userguide/application_plugin.html
application {
    mainClass.set("com.github.aanno.jaws.S3Test")
}
