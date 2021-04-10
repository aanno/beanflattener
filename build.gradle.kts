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
    testAnnotationProcessor(files("build/libs/beanflattener-${version}.jar"))

    // All  included with auto-service
    // implementation("com.google.auto:auto-common:1.0")
    // implementation("com.google.guava:guava:30.1.1-jre")

    // https://www.baeldung.com/java-poet
    implementation("com.squareup:javapoet:1.13.0")
    // https://commons.apache.org/proper/commons-lang/
    // https://commons.apache.org/proper/commons-lang/apidocs/index.html
    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

// https://www.baeldung.com/gradle-fat-jar
// https://stackoverflow.com/questions/41794914/how-to-create-the-fat-jar-with-gradle-kotlin-script
tasks.getByName<Jar>("jar") {
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
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
